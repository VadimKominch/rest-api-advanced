package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class TagDao {

    private SessionFactory sessionFactory;

    @Autowired
    public TagDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Tag> getAll() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
        Root<Tag> rootEntry = cq.from(Tag.class);
        CriteriaQuery<Tag> all = cq.select(rootEntry);
        TypedQuery<Tag> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        return allQuery.getResultList();
    }

    public Tag getByTagName(String name) {
           return sessionFactory.getCurrentSession().get(Tag.class,name);
    }

    @Transactional
    public boolean delete(Integer id) {
            Tag tag = getById(id);
        if(tag == null) {
            return false;
        } else  {
            sessionFactory.getCurrentSession().delete(tag);
            sessionFactory.getCurrentSession().flush();
            return true;
        }
    }

    @Transactional
    public Tag save(Tag tag) {
        sessionFactory.getCurrentSession().save(tag);
        return tag;
    }

    public Tag getById(Integer id) {
        return sessionFactory.getCurrentSession().get(Tag.class,id);
    }

}
