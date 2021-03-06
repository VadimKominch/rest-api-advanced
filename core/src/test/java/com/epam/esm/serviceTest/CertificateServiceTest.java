package com.epam.esm.serviceTest;

import com.epam.esm.TestConfig;
import com.epam.esm.dao.GiftDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.generator.CertificateGenerator;
import com.epam.esm.generator.OrderGenerator;
import com.epam.esm.generator.TagGenerator;
import com.epam.esm.generator.UserGenerator;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CertificateServiceTest {
    public static final int CERTIFICATE_AMOUNT = 100;
    @Autowired
    private GiftService giftService;

    @Autowired
    private GiftDao giftDao;
    @Autowired
    private TagService tagRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService repository;
    private int checkedId = CERTIFICATE_AMOUNT - 5;

    @Before
    public void setup() {
        List<Tag> tags = TagGenerator.getTags(CERTIFICATE_AMOUNT);
        List<GiftCertificate> certificates = CertificateGenerator.getTags(CERTIFICATE_AMOUNT);
        List<User> users = UserGenerator.getUsers(CERTIFICATE_AMOUNT);
        List<Order> orders = OrderGenerator.getOrders(100);
        repository.saveAll(users);
        tagRepository.saveAll(tags);
        for(int i =1;i<CERTIFICATE_AMOUNT+1;i++) {
            GiftCertificate giftCertificate = certificates.get(i-1);
            User savedUSer = users.get(i-1);
            savedUSer.setId(i);
            int startIndex = ThreadLocalRandom.current().nextInt(1, CERTIFICATE_AMOUNT-1);
            List<Tag> tagsSet = new ArrayList<>();
            Tag tag1 = tags.get(startIndex-1);
            Tag tag2 = tags.get(startIndex);
            tag1.setId(startIndex);
            tag2.setId(startIndex+1);
            tagsSet.add(tag1);
            tagsSet.add(tag2);
            giftCertificate.setTags(tagsSet);
        }
        giftService.saveAll(certificates);
        for (int i = 1; i < 100; i++) {
            Order order = orders.get(i-1);
            User user = users.get(i-1);
            GiftCertificate giftCertificate = certificates.get(i-1);
            GiftCertificate giftCertificate2 = certificates.get(i);
            List<GiftCertificate> certificateList = new ArrayList<>();
            for(int j = 0; j< i;j++)
                certificateList.add(giftCertificate);
            certificateList.add(giftCertificate2);
            order.setCertificate(certificateList);
            order.getCertificate().forEach(el->el.getOrderList().add(order));
            order.setUser(user);
            user.getOrders().add(order);
            orderService.save(order);
        }
    }

    /**
     * Test case for check for existing in db entity with strict id.
     * */

    @Test
    public void checkIfPresentEntityWithIdWillBeFound() {
        GiftCertificate certificate = giftService.getById(checkedId);
        Assert.assertNotNull(certificate);
    }

    @Test
    public void checkIfNotPresentEntityWillNotBeFound() {
        GiftCertificate certificate = giftService.getById(CERTIFICATE_AMOUNT+1);
        Assert.assertNull(certificate);
    }

    @Test
    public void checkIfAllCertificatesInDB() {
        Assert.assertEquals(CERTIFICATE_AMOUNT,giftService.getAll().size());
    }

    @Test
    public void checkIfDeletedEntityWillNotStoredInDb() {
        giftService.delete(checkedId);
        Assert.assertNull(giftService.getById(checkedId));
    }

    @Test
    public void checkIfNullWillNotChangeDb() {
        int expected = giftService.getAll().size();
        giftService.delete(CERTIFICATE_AMOUNT+1);
        Assert.assertEquals(expected,giftService.getAll().size());
    }

    @Test
    public void checkIfUpdateMethodChangesExistingEntity() {
        GiftCertificate certificate = giftService.getById(2);
        certificate.setDuration((short)50);
        certificate.setPrice(666666);
        giftService.update(2,certificate);
        Assert.assertEquals(certificate,giftService.getById(2));
    }

    @Test
    public void checkIfCertificateWithExistingPairOfTagsWillBeFound() {
        GiftCertificate actual = giftService.getById(2);
        List<GiftCertificate> certificate = giftService.getCertificatesByTagNames(actual.getTags());
        Assert.assertTrue(certificate.indexOf(actual)!=-1);
    }

    @Test
    public void checkIfTheMostUsableTagWillBeFound() {
        Assert.assertNotNull(giftDao.getMostUSedTag());
    }

    @Test
    public void checkIfSaveOperationStoreEntityInDatabase() {
        List<GiftCertificate> certificates = CertificateGenerator.getTags(1);
        List<Tag> tags = new ArrayList<Tag>() {
            {
                add(tagRepository.getById(0));
                add(tagRepository.getById(1));
                add(tagRepository.getById(2));
            }
        };
        GiftCertificate giftCertificate = certificates.get(0);
        giftCertificate.setTags(tags);
        giftService.save(giftCertificate);
        Assert.assertEquals(giftService.getAll().size(),CERTIFICATE_AMOUNT+1);
    }

    @Test
    public void getFirstPageOfCertificates() {
        Assert.assertEquals(10,giftDao.getPageOfCertificates(1,10, Optional.empty()).size());
    }

    @Test
    public void checkIfSortWillWorkForDescOrder() {
        Assert.assertEquals("Certificate №99",giftDao.getPageOfCertificates(1,10, Optional.of("desc")).get(0).getName());
    }

    @Test
    public void checkIfAllCertificatesWillBeCounted() {
        Assert.assertEquals(CERTIFICATE_AMOUNT, giftService.getCount().longValue());
    }
}
