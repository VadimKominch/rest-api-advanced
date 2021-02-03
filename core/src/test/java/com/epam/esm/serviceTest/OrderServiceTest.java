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
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class OrderServiceTest {
    public static final int CERTIFICATE_AMOUNT = 100;
    @Autowired
    private GiftService giftService;
    @Autowired
    private TagService tagRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService repository;

    private int checkedId = 5;

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

    @Test
    public void checkIfExistingOrderWillBeGotAsResult() {
        Assert.assertNotNull(orderService.getById(checkedId));
    }

    @Test
    public void checkIfAllStoredEntitiesWillBeGot() {
        Assert.assertEquals(99,orderService.getAll().size());
    }

    @Test
    public void checkIfDeletedEntityWillNotStoredInDb() {
        orderService.delete(checkedId);
        Assert.assertNull(orderService.getById(checkedId));
    }

    @Test
    public void checkIfSavedEntityWillBeStoredInDatabase() {
        Order order = OrderGenerator.getOrders(1).get(0);
        GiftCertificate giftCertificate = giftService.getById(2);
        order.setCertificate(new ArrayList<GiftCertificate>(){
            {
            add(giftCertificate);
        }
        });
        orderService.save(order);
        Assert.assertEquals(100,giftService.getAll().size());
    }

    @Test
    public void checkIFNullEntityWillNotBeStoredInDatabase() {
        orderService.save(null);
        Assert.assertEquals(99,orderService.getAll().size());
    }

    @Test
    public void checkIfUsersOrdersCountWillBeCorrectlyCounted() {
        Assert.assertEquals(1,orderService.getUserOrdersCount(1).longValue());
    }

    @Test
    public void checkIfUsersOrdersPageCountWillBeCorrectlyCounted() {
        Assert.assertEquals(1,orderService.getOrderByUserId(1,1,5).size());
    }

}
