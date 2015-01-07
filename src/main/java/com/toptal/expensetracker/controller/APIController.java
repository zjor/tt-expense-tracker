package com.toptal.expensetracker.controller;

import com.toptal.expensetracker.dao.ExpenseDao;
import com.toptal.expensetracker.model.Expense;
import com.toptal.expensetracker.model.User;
import com.toptal.expensetracker.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Sergey Royz
 * Date: 31.12.2014
 */
@Controller
@RequestMapping("api")
public class APIController {

    @Inject
    @Named("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Inject
    private ExpenseDao expenseDao;

    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authResult = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "expenses", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ExpenseDTO create(
            @RequestParam("description") String description,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("timestamp") Long timestamp) {

        User user = AuthService.getUser();
        Expense expense = expenseDao.create(user, description, comment, amount, new Date(timestamp));

        return ExpenseDTO.fromModel(expense);
    }

    @RequestMapping(value = "expenses/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<ExpenseDTO> edit(
            @PathVariable("id") String id,
            @RequestParam("description") String description,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("timestamp") Long timestamp) {

        User user = AuthService.getUser();

        Expense expense = expenseDao.get(id);
        if (expense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!expense.getUserId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        expense.setDescription(description);
        expense.setComment(comment);
        expense.setAmount(amount);
        expense.setTimestamp(new Date(timestamp));
        expenseDao.update(expense);

        return new ResponseEntity<>(ExpenseDTO.fromModel(expense), HttpStatus.OK);
    }

    @RequestMapping(value = "expenses/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") String id) {
        User user = AuthService.getUser();

        Expense expense = expenseDao.get(id);
        if (expense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!expense.getUserId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        expenseDao.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "expenses", method = RequestMethod.GET)
    @ResponseBody
    public List<ExpenseDTO> fetch(
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "filter", required = false) String filter) {
        String userId = AuthService.getUser().getId();
        if (filter != null && filter.length() > 0) {
            filter = '%' + filter + '%';
        }
        return ExpenseDTO.fromModel(expenseDao.fetch(userId, null, null, limit, filter));
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseDTO implements Serializable {
        private String id;
        private String description;
        private String comment;
        private BigDecimal amount;
        private long timestamp;

        public static ExpenseDTO fromModel(Expense model) {
            return new ExpenseDTO(
                    model.getId(),
                    model.getDescription(),
                    model.getComment(),
                    model.getAmount(),
                    model.getTimestamp().getTime());
        }

        public static List<ExpenseDTO> fromModel(List<Expense> modelList) {
            List<ExpenseDTO> result = new ArrayList<>(modelList.size());
            for (Expense e : modelList) {
                result.add(fromModel(e));
            }
            return result;
        }
    }
}
