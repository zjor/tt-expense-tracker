package com.toptal.expensetracker.controller;

import com.toptal.expensetracker.model.User;
import com.toptal.expensetracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;

/**
 * @author: Sergey Royz
 * Date: 28.12.2014
 */
@Controller
public class RegistrationController {

    @Inject
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registrationForm() {
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("email") String email, @RequestParam("password") String password) {
        User user = userService.create(email, password);
        //TODO: add flash to response
        return "redirect:/";
    }

}
