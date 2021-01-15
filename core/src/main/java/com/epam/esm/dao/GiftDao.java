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

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Transactional
@Component
public class GiftDao {
    private SessionFactory sessionFactory;

    @Autowired
    public GiftDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<GiftSertificate> getAll() {
        return null;
    }


    public GiftSertificate getById(Integer id) {
        return null;
    }

    public boolean save(GiftSertificate entity) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        currentSession.persist(entity);
        currentSession.getTransaction().commit();
        return true;
    }


    public boolean delete(Integer id) {
        return true;
    }


    public GiftSertificate update(Integer id, GiftSertificate newObj) {
        return null;
    }

}
