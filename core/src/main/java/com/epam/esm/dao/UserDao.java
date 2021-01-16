package com.epam.esm.dao;

import com.epam.esm.entity.GiftSertificate;
import com.epam.esm.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> rootEntry = cq.from(User.class);
        CriteriaQuery<User> all = cq.select(rootEntry);
        TypedQuery<User> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        return allQuery.getResultList();
    }


    public User getById(Integer id) {
        return sessionFactory.getCurrentSession().get(User.class,id);
    }

    public User getByTagName(String userName) {
        return sessionFactory.getCurrentSession().get(User.class,userName);
    }


    public User save(User entity) {
        sessionFactory.getCurrentSession().persist(entity);
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
