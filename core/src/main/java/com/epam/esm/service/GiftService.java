package com.epam.esm.service;

import com.epam.esm.dao.GiftDao;
import com.epam.esm.entity.GiftSertificate;
import org.springframework.beans.factory.annotation.Autowired;
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
        return giftDao.getById(id);
    }

    public boolean save(GiftSertificate entity) {
        return giftDao.save(entity);
    }

    public boolean delete(Integer id) {
        return giftDao.delete(id);
    }

    public GiftSertificate update(Integer id, GiftSertificate newObj) {
        return giftDao.update(id,newObj);
    }

}
