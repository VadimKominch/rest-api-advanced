package com.epam.esm.serviceTest;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
public class CertificateServiceTest {
    public static final int CERTIFICATE_AMOUNT = 100;
    @Autowired
    private GiftService giftService;
    private int checkedId = CERTIFICATE_AMOUNT - 5;
    @Before
    public void setup() {
    }

    /**
     * Test case for check for existing in db entity with strict id.
     * */

    @Test
    public void checkIfPresentEntityWithIdWillBeFound() {
        GiftCertificate certificate = giftService.getById(checkedId);
        Assert.assertNotNull(certificate);
    }
}
