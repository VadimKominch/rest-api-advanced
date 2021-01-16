package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagService {
    private TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }


    public List<Tag> getAll() {
        try {
            return tagDao.getAll();
        }catch (DataAccessException e) {
            return null;
        }
    }


    public Tag getById(Integer id) {
        try {
            Tag tag =  tagDao.getById(id);
            return tag;
        }catch (DataAccessException e) {
            return null;
        }
    }


    public Tag save(Tag entity) {
        tagDao.save(entity);
        return entity;
    }


    public boolean delete(Integer id) {
        return tagDao.delete(id);
    }

    public Tag getByName(String name) {
        return tagDao.getByTagName(name);
    }

    public void saveAll(List<Tag> tags) {
        tags.forEach(this::save);
    }
}
