package com.epam.esm.dao;


import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Component
@EnableTransactionManagement
public class OrderDao {

    private SessionFactory sessionFactory;

    @Autowired
    public OrderDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void save(Order order) {
        order.setBuyDate(new Date());
        order.setPrice(order.getCertificate().stream().map(GiftCertificate::getPrice).reduce(Double::sum).orElse(0.0));
        sessionFactory.getCurrentSession().save(order);
    }

    @Transactional
    public List<Order> getAllOrders() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> rootEntry = cq.from(Order.class);
        CriteriaQuery<Order> all = cq.select(rootEntry);
        TypedQuery<Order> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        return allQuery.getResultList();
    }
    /*Pagination*/

    @Transactional
    public Order getOrderById(Integer id) {
        return sessionFactory.getCurrentSession().get(Order.class,id);
    }

    @Transactional
    public List<Order> getOrderByUserId(Integer userId,Integer pageNumber,Integer pageSize) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> rootEntry = cq.from(Order.class);
        CriteriaQuery<Order> all = cq.select(rootEntry);
        all.where(cb.equal(rootEntry.get("user_id"),userId));
        return sessionFactory.getCurrentSession().createQuery(all)
                .setFirstResult((pageNumber-1)*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public Long getUserOrdersCount(int userId) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Order> from = cq.from(Order.class);
        CriteriaQuery<Long> select = cq.select(cb.count(from));
        select.where(cb.equal(from.get("user_id"),userId));
        return sessionFactory.getCurrentSession().createQuery(select).getSingleResult();
    }

    @Transactional
    public boolean delete(Integer id) {
        Order order = getOrderById(id);
        if(order == null) {
            return false;
        } else  {
            order.setCertificate(null);
            order.setUser(null);
            sessionFactory.getCurrentSession().delete(order);
            sessionFactory.getCurrentSession().flush();
            return true;
        }
    }
}
