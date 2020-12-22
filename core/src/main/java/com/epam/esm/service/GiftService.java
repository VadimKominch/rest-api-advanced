package com.epam.esm.service;

import com.epam.esm.dao.GiftDao;
import com.epam.esm.entity.GiftSertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class GiftService {
    private GiftDao giftDao;

    @Autowired
    public GiftService(GiftDao giftDao) {
        this.giftDao = giftDao;
    }

    public List<GiftSertificate> getAll() {
        return giftDao.getAll();
    }

    public GiftSertificate getById(Integer id) {
        try {
            return giftDao.getById(id);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean save(GiftSertificate entity) {
        if(entity == null) {
            return false;
        }
        return giftDao.save(entity);
    }

    public boolean delete(Integer id) {
        return giftDao.delete(id);
    }

    public GiftSertificate update(Integer id, GiftSertificate newObj) {
        return giftDao.update(id,newObj);
    }

    public void saveAll(List<GiftSertificate> certificates) {
        certificates.forEach(el->giftDao.save(el));
    }
}
