package com.epam.esm.servicetest;

import com.epam.esm.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.epam.esm")
public class TagServiceTest {

    @Resource
    @Qualifier(value = "test")
    private TagService tagService;

    @Test
    public void checkif() {
        assertEquals(0,tagService.getAll().size());
    }
}
