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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    public ResponseEntity<ExpenseDTO> create(
            @RequestParam("description") String description,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("timestamp") Long timestamp) {

        User user = AuthService.getUser();
        Expense expense = expenseDao.create(user, description, comment, amount, new Date(timestamp));

        return new ResponseEntity<>(ExpenseDTO.fromModel(expense), HttpStatus.CREATED);
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
    }
}
