package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public List<Tag> getAll() {
        try {
            return ((List<Tag>) tagRepository.findAll());
        }catch (DataAccessException e) {
            return null;
        }
    }


    public Tag getById(Integer id) {
        try {
            return tagRepository.findById(id).get();
        }catch (DataAccessException e) {
            return null;
        }
    }


    public Tag save(Tag entity) {
        return tagRepository.save(entity);
    }


    public void delete(Integer id) {
        tagRepository.deleteById(id);
    }
}
