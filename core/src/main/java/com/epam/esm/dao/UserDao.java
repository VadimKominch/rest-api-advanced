package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
@EnableTransactionManagement
public class UserDao {


    private SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<User> getAll() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> rootEntry = cq.from(User.class);
        CriteriaQuery<User> all = cq.select(rootEntry);
        TypedQuery<User> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        return allQuery.getResultList();
    }

    @Transactional
    public User getById(Integer id) {
        return sessionFactory.getCurrentSession().get(User.class,id);
    }

    @Transactional
    public User getByTagName(String userName) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> rootEntry = cq.from(User.class);
        cq
                .select(rootEntry)
                .where(cb.like(rootEntry.get("name"),"%"+userName+"%"));
        TypedQuery<User> nameQuery = sessionFactory.getCurrentSession().createQuery(cq);
        return nameQuery.getSingleResult();
    }

    @Transactional
    public User save(User entity) {
        sessionFactory.getCurrentSession().persist(entity);
        return null;
    }

    @Transactional
    public boolean delete(Integer id) {
        User user = getById(id);
        if(user == null) {
            return false;
        } else  {
            sessionFactory.getCurrentSession().delete(user);
            sessionFactory.getCurrentSession().flush();
            return true;
        }
    }

    /**
     *
     * Pageable method for pagination*/
    @Transactional
    public List<User> findAll(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
