package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class UserDao {


    private SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<User> getAll() {
        return null;
    }


    public User getById(Integer id) {
        return null;
    }

    public User getByTagName(String userName) {
        return null;
    }


    public User save(User entity) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();
        return null;
    }


    public boolean delete(Integer id) {
        return true;
    }

    /**
     *
     * Pageable method for pagination*/
    public List<User> findAll(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
