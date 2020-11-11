package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends PagingAndSortingRepository<GiftCertificate,Integer> {
}
