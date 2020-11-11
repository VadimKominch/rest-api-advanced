package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class GiftService {
    private CertificateRepository certificateRepository;

    @Autowired
    public GiftService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public List<GiftCertificate> getAll() {
        return (List<GiftCertificate>) certificateRepository.findAll();
    }

    public GiftCertificate getById(Integer id) {
        return certificateRepository.findById(id).get();
    }

    public void save(GiftCertificate entity) {
        certificateRepository.save(entity);
    }

    public void delete(Integer id) {
        certificateRepository.deleteById(id);
    }

    public GiftCertificate update(Integer id, GiftCertificate newObj) {
        newObj.setId(id);
        return certificateRepository.save(newObj);
    }

}
