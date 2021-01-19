package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@EnableTransactionManagement
public class GiftDao {
    private SessionFactory sessionFactory;

    @Autowired
    public GiftDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<GiftCertificate> getAll() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> rootEntry = cq.from(GiftCertificate.class);
        CriteriaQuery<GiftCertificate> all = cq.select(rootEntry);
        TypedQuery<GiftCertificate> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        return allQuery.getResultList();
    }

    @Transactional
    public GiftCertificate getById(Integer id) {
        return sessionFactory.getCurrentSession().get(GiftCertificate.class,id);
    }
    @Transactional
    public boolean save(GiftCertificate entity) {
        entity.setLastUpdateDate(new Date());
        entity.setCreationDate(new Date());
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

    @Transactional
    public GiftCertificate update(Integer id, GiftCertificate newObj) {
        GiftCertificate certificate = getById(id);
        if(certificate == null) {
            return null;
        } else {
            newObj.getTags().forEach(el -> {
                if (!certificate.getTags().contains(el)) {
                    certificate.getTags().add(el);
                    certificate.setLastUpdateDate(new Date());
                }
            });

            if(newObj.getDescription()!=null) {
                certificate.setDescription(newObj.getDescription());
                certificate.setLastUpdateDate(new Date());
            }
            if(newObj.getName()!=null) {
                certificate.setName(newObj.getName());
                certificate.setLastUpdateDate(new Date());
            }
            if(newObj.getDuration()!=null) {
                certificate.setDuration(newObj.getDuration());
                certificate.setLastUpdateDate(new Date());
            }
            if(newObj.getDescription()!=null) {
                certificate.setDescription(newObj.getDescription());
                certificate.setLastUpdateDate(new Date());
            }
            if(newObj.getPrice()!=null) {
                certificate.setPrice(newObj.getPrice());
                certificate.setLastUpdateDate(new Date());
            }
            if(newObj.getDescription()!=null) {
                certificate.setDescription(newObj.getDescription());
                certificate.setLastUpdateDate(new Date());
            }

            System.out.println(certificate);
            return (GiftCertificate) sessionFactory.getCurrentSession().merge(certificate);
        }
    }

}
