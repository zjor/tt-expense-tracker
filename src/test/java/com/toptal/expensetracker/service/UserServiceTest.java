package com.toptal.expensetracker.service;

import com.toptal.expensetracker.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.inject.Inject;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * @author: Sergey Royz
 * Date: 29.12.2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@TransactionConfiguration(defaultRollback = false)
public class UserServiceTest {

    @Inject
    private UserService userService;

    @Test
    public void createUser() {
        String email = "user@host.com";
        userService.create(email, "s3cr3t");
        User user = userService.findByEmail(email);
        assertNotNull(user);
    }

    @Test
    public void shouldRemove() {
        String email = "user@host.com";
        userService.create(email, "s3cr3t");
        User user = userService.findByEmail(email);
        assertNotNull(user);

        userService.remove(user);
        user = userService.findByEmail(email);
        assertNull(user);
    }


}
