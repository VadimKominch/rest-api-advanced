package com.epam.esm.serviceTest;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.generator.TagGenerator;
import com.epam.esm.service.TagService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.jupiter.api.TestTemplate;
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
public class TagServiceTest {
    public static final int TAG_AMOUNT = 100;
    @Autowired
    private TagService tagService;

    private int checkedId = TAG_AMOUNT - 5;

    @Before
    public void setup() {
        List<Tag> tags = TagGenerator.getTags(100);
        tags.forEach(el->tagService.save(el));
    }

    @Test
    public void getTagIfIdISPresent() {
        Tag tag = tagService.getById(checkedId);
        Assert.assertNotNull(tag);
    }

    @Test
    public void getTagIfIsNotPresent() {
        Tag tag = tagService.getById(TAG_AMOUNT + 5);
        Assert.assertNull(tag);
    }

    @Test
    public void checkIfAllTagsWillBeFound() {
        Assert.assertEquals(TAG_AMOUNT,tagService.getAll().size());
    }
}
