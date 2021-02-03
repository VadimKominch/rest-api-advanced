package com.epam.esm.service;

import com.epam.esm.dao.GiftDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
public class GiftService {
    private GiftDao giftDao;

    @Autowired
    public GiftService(GiftDao giftDao) {
        this.giftDao = giftDao;
    }

    public List<GiftCertificate> getAll() {
        return giftDao.getAll();
    }

    public GiftCertificate getById(Integer id) {
        return giftDao.getById(id);
    }

    @Transactional
    public List<GiftCertificate> getCertificatesByTagNames(List<Tag> tags) {
        return giftDao.getCertificatesByTagNames(tags);
    }

    public boolean save(GiftCertificate entity) {
        if(entity == null) {
            return false;
        }
        return giftDao.save(entity);
    }

    public Tag getMostUsableTagInMostExpensiveCostOFOrdersByOneUser() {
        return giftDao.getMostUSedTag();
    }

    public boolean delete(Integer id) {
        return giftDao.delete(id);
    }

    public GiftCertificate update(Integer id, GiftCertificate newObj) {
        return giftDao.update(id,newObj);
    }

    public void saveAll(List<GiftCertificate> certificates) {
        certificates.forEach(el -> giftDao.save(el));
    }

    public List<GiftCertificate> getPageOfCertificates(int pageNumber, int pageSize, Optional<String> sort) {
        return giftDao.getPageOfCertificates(pageNumber,pageSize,sort);
    }

    public Long getCount() {
        return giftDao.getCount();
    }
}
