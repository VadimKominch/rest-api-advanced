package com.epam.esm.service;

import com.epam.esm.controller.TagController;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.server.core.WebHandler.linkTo;

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
