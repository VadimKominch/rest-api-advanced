package com.epam.esm.serviceTest;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.User;
import com.epam.esm.generator.UserGenerator;
import com.epam.esm.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService service;

    @BeforeEach
    public void setUp() {
        List<User> users = UserGenerator.getUsers(100);
        users.forEach(el->service.save(el));
    }

    @Test
    public void checkIfAllUsersContainsInDatabase() {
        Assert.assertEquals(100,service.getAll().size());
    }

    @Test
    public void checkDeleteOperation() {

    }

    @Test
    public void checkUpdateOperation() {

    }
}
