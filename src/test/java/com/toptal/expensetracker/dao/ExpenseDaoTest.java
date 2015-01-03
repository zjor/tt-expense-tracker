package com.toptal.expensetracker.dao;

import com.toptal.expensetracker.model.Expense;
import com.toptal.expensetracker.model.User;
import com.toptal.expensetracker.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author: Sergey Royz
 * Date: 31.12.2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@TransactionConfiguration(defaultRollback = true)
public class ExpenseDaoTest {

    @Inject
    private UserService userService;

    @Inject
    private ExpenseDao expenseDao;

    @Test
    public void createAndFind() {
        User user = userService.create("user@host.com", "s3cr3t");
        String id = expenseDao.create(user, "description", null, BigDecimal.TEN, new Date()).getId();
        assertEquals("description", expenseDao.get(id).getDescription());
    }

    @Test
    public void createAndRemove() {
        User user = userService.create("user@host.com", "s3cr3t");
        String id = expenseDao.create(user, "description", null, BigDecimal.TEN, new Date()).getId();
        expenseDao.delete(id);
        assertNull(expenseDao.get(id));
    }

    @Test
    public void createAndUpdate() {
        User user = userService.create("user@host.com", "s3cr3t");
        String id = expenseDao.create(user, "description", null, BigDecimal.TEN, new Date()).getId();
        Expense expense = new Expense(user, "new description", null, BigDecimal.ONE, new Date());
        expense.setId(id);
        expenseDao.update(expense);
        assertEquals("new description", expenseDao.get(id).getDescription());
    }

    @Test
    public void fetchInRange() {
        User user = userService.create("user@host.com", "s3cr3t");
        expenseDao.create(user, "description", null, BigDecimal.TEN, new Date(1L)).getId();
        expenseDao.create(user, "description", null, BigDecimal.TEN, new Date(2L)).getId();
        expenseDao.create(user, "description", null, BigDecimal.TEN, new Date(3L)).getId();
        List<Expense> expenses = expenseDao.fetch(user.getId(), new Date(3L), new Date(1L), null, null);
        assertEquals(2, expenses.size());
    }

    @Test
    public void fetchInRangeWithFilter() {
        User user = userService.create("user@host.com", "s3cr3t");
        expenseDao.create(user, "description", "McDouglas", BigDecimal.TEN, new Date(1L)).getId();
        expenseDao.create(user, "McDonalds", null, BigDecimal.TEN, new Date(2L)).getId();
        expenseDao.create(user, "description", null, BigDecimal.TEN, new Date(3L)).getId();
        List<Expense> expenses = expenseDao.fetch(user.getId(), new Date(3L), new Date(1L), null, "%Mc%");
        assertEquals(2, expenses.size());
    }

}
