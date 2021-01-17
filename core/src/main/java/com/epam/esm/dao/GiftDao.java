package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class GiftDao {
    private SessionFactory sessionFactory;

    @Autowired
    public GiftDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<GiftCertificate> getAll() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> rootEntry = cq.from(GiftCertificate.class);
        CriteriaQuery<GiftCertificate> all = cq.select(rootEntry);
        TypedQuery<GiftCertificate> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        return allQuery.getResultList();
    }


    public GiftCertificate getById(Integer id) {
        return sessionFactory.getCurrentSession().get(GiftCertificate.class,id);
    }
    @Transactional
    public boolean save(GiftCertificate entity) {

        sessionFactory.getCurrentSession().persist(entity);
        return true;
    }

    @Transactional
    public boolean delete(Integer id) {
        GiftCertificate certificate = getById(id);
        if(certificate == null) {
            return false;
        } else  {
            certificate.getTags().forEach(el->el.getCertificates().remove(certificate));
            certificate.getUser().getCertificates().remove(certificate);
            certificate.setTags(null);
            certificate.setUser(null);
            sessionFactory.getCurrentSession().delete(certificate);
            sessionFactory.getCurrentSession().flush();
            return true;
        }
    }


    public GiftCertificate update(Integer id, GiftCertificate newObj) {
        return null;
    }

}
