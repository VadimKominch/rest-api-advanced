package com.epam.esm.serviceTest;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.generator.UserGenerator;
import com.epam.esm.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService service;

    @Before
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
        service.delete(5);
        Assert.assertNull(service.getById(5));
    }

    @Test
    public void getNullIfTryToGetWithNonExistingId() {
        Assert.assertNull(service.getById(101));
    }

    @Test
    public void getUserWithExistingName() {
        Assert.assertNotNull(service.getByName("User №0"));
    }
}
