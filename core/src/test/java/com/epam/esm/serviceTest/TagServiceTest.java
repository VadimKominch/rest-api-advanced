package com.epam.esm.serviceTest;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ObjectNotFoundException;
import com.epam.esm.generator.TagGenerator;
import com.epam.esm.generator.UserGenerator;
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

import javax.persistence.NoResultException;
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

    private int checkedId = 5;

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
    public void checkIfTagWithValidNameWillBeReturn() {
        Assert.assertNotNull(tagService.getByName("Tag №"+checkedId));
    }

    @Test
    public void checkIfAllTagsWillBeFound() {
        Assert.assertEquals(TAG_AMOUNT,tagService.getAll().size());
    }

    @Test
    public void checkIfDeleteOperationForExistingTagWillBeCorrect() {
        tagService.delete(checkedId);
        Assert.assertNull(tagService.getById(checkedId));
    }

    @Test
    public void checkIfNonExistingElementThrowsException() {
        Assert.assertNull(tagService.getById(TAG_AMOUNT*2));
    }

    @Test
    public void checkIfObjectWillBeSavedInDB() {
        Tag tag = tagService.save(TagGenerator.getTags(1).get(0));
        Assert.assertEquals(tagService.getById(TAG_AMOUNT+1),tag);
    }

    @Test
    public void checkIfGetFirstPageWillBeCorrectAmount() {
        List<Tag> tags = tagService.getPageOfList(1,10);
        Assert.assertEquals(10,tags.size());
    }

    @Test
    public void checkIfTagWithNonExistingNameWillNotBeFound() {
        Assert.assertNull(tagService.getByName("test"));
    }

    @Test
    public void checkIfTagCountWillBeCorrectlyCounted() {
        Assert.assertEquals(TAG_AMOUNT,tagService.getTagCount().longValue());
    }
}
