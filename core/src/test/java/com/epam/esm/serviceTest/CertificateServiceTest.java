package com.epam.esm.serviceTest;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.generator.CertificateGenerator;
import com.epam.esm.generator.UserGenerator;
import com.epam.esm.service.GiftService;
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
public class CertificateServiceTest {
    public static final int CERTIFICATE_AMOUNT = 100;
    @Autowired
    private GiftService giftService;
    private int checkedId = CERTIFICATE_AMOUNT - 5;

    @Before
    public void setup() {
        List<GiftCertificate> gifts = CertificateGenerator.getTags(100);
        gifts.forEach(el->giftService.save(el));
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
}
