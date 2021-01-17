package com.epam.esm.generator;

import com.epam.esm.entity.GiftCertificate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CertificateGenerator {
    public static List<GiftCertificate> getTags(int count) {
        return IntStream.range(0,count).mapToObj(el->{
            GiftCertificate certificate = new GiftCertificate();
            certificate.setName("Certificate №"+el);
            certificate.setDescription("Description for certificate №"+el);
            certificate.setPrice(Math.round(Math.random()*30)+10);
            certificate.setDuration((short) (Math.round(Math.random()*30)+10));
            certificate.setLastUpdateDate(new Date());
            certificate.setCreationDate(new Date());
            return certificate;
        }).collect(Collectors.toList());
    }
}
