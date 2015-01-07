package com.toptal.expensetracker.service;

import com.toptal.expensetracker.dao.ExpenseDao;
import com.toptal.expensetracker.model.Expense;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * @author: Sergey Royz
 * Date: 27.12.2014
 */
@Slf4j
@Service
public class ExpenseService {

    @Inject
    private ExpenseDao expenseDao;

    public Date getNearestWeekStart(Date date) {
        DateTime dt = new DateTime(date);
        return dt
                .dayOfWeek().setCopy(1)
                .hourOfDay().setCopy(0)
                .minuteOfHour().setCopy(0)
                .secondOfMinute().setCopy(0)
                .millisOfSecond().setCopy(0).toDate();
    }

    public Date getNearestWeekEnd(Date date) {
        DateTime dt = new DateTime(date);
        return dt
                .dayOfWeek().setCopy(7)
                .hourOfDay().setCopy(23)
                .minuteOfHour().setCopy(59)
                .secondOfMinute().setCopy(59)
                .millisOfSecond().setCopy(99).toDate();
    }

    /**
     * Fetches expenses within a week specified by given date
     * @param userId
     * @param date
     * @return
     */
    public List<Expense> getExpensesOfWeek(String userId, Date date) {
        long start = getNearestWeekStart(date).getTime() - 1;
        long end = getNearestWeekEnd(date).getTime() + 1;
        return expenseDao.fetch(userId, new Date(end), new Date(start), null, null);
    }

}
