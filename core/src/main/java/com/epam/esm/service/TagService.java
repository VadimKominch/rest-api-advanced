package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import java.util.List;

@Component
public class TagService {
    private TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }


    public List<Tag> getAll() {
        return tagDao.getAll();
    }

    public List<Tag> getPageOfList(int pageNumber,int pageSize) {
        return tagDao.getPageOfTags(pageNumber,pageSize);
    }

    public Tag getById(Integer id) {
        try {
            Tag tag =  tagDao.getById(id);
            return tag;
        }catch (ObjectNotFoundException e) {
            return null;
        }
    }

    public Long getTagCount() {
        return tagDao.getTagCount();
    }

    public Tag save(Tag entity) {
        tagDao.save(entity);
        return entity;
    }


    public boolean delete(Integer id) {
        return tagDao.delete(id);
    }

    public Tag getByName(String name) {
        try{
            return tagDao.getByTagName(name);
        } catch(NoResultException e) {
            return null;
        }
    }

    public void saveAll(List<Tag> tags) {
        tags.forEach(this::save);
    }
}
