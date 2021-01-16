package com.epam.esm.dao;

import com.epam.esm.converter.DateConverter;
import com.epam.esm.entity.GiftSertificate;
import com.epam.esm.entity.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GiftDao {
    private SessionFactory sessionFactory;

    @Autowired
    public GiftDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<GiftSertificate> getAll() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<GiftSertificate> cq = cb.createQuery(GiftSertificate.class);
        Root<GiftSertificate> rootEntry = cq.from(GiftSertificate.class);
        CriteriaQuery<GiftSertificate> all = cq.select(rootEntry);
        TypedQuery<GiftSertificate> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        return allQuery.getResultList();
    }


    public GiftSertificate getById(Integer id) {
        return sessionFactory.getCurrentSession().get(GiftSertificate.class,id);
    }
    @Transactional
    public boolean save(GiftSertificate entity) {
        sessionFactory.getCurrentSession().persist(entity);
        return true;
    }


    public boolean delete(Integer id) {
        return true;
    }


    public GiftSertificate update(Integer id, GiftSertificate newObj) {
        return null;
    }

}
