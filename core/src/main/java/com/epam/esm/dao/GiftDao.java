package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.*;

@Component
@EnableTransactionManagement
public class GiftDao {
    private SessionFactory sessionFactory;
    private TagDao tagDao;

    @Autowired
    public GiftDao(SessionFactory sessionFactory, TagDao tagDao) {
        this.sessionFactory = sessionFactory;
        this.tagDao = tagDao;
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
    public List<GiftCertificate> getPageOfCertificates(int pageNumber,int pageSize,Optional<String> sort) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> rootEntry = cq.from(GiftCertificate.class);
        CriteriaQuery<GiftCertificate> all = cq.select(rootEntry);
        if(sort.isPresent()) {
            if(sort.get().equalsIgnoreCase("asc")) {
                cq.orderBy(cb.asc(rootEntry.get("name")));
            }
            if(sort.get().equalsIgnoreCase("desc")) {
                cq.orderBy(cb.desc(rootEntry.get("name")));
            }
        }
        TypedQuery<GiftCertificate> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        allQuery.setFirstResult((pageNumber-1)*pageSize);
        allQuery.setMaxResults(pageSize);
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
    public Long getCount() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<GiftCertificate> from = cq.from(GiftCertificate.class);
        CriteriaQuery<Long> select = cq.select(cb.count(from));
        return sessionFactory.getCurrentSession().createQuery(select).getSingleResult();
    }

    @Transactional
    public boolean delete(Integer id) {
        GiftCertificate certificate = getById(id);
        if(certificate == null) {
            return false;
        } else  {
            certificate.getTags().forEach(el->el.getCertificates().remove(certificate));
            sessionFactory.getCurrentSession().delete(certificate);
            return true;
        }
    }

    @Transactional
    public List<GiftCertificate> getCertificatesByTagNames(List<Tag> tags) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        List<GiftCertificate> resultList = new ArrayList<>();
        Root<GiftCertificate> cert = cq.from(GiftCertificate.class);
        Join<GiftCertificate, Tag> res = cert.join("tags",JoinType.LEFT);

        for(int i = 0;i<tags.size();i++) {
            cq
                    .select(cert)
                    .where(cb.equal(res.get("name"),tags.get(i).getName()));
            TypedQuery<GiftCertificate> nameQuery = sessionFactory.getCurrentSession().createQuery(cq);
            //nameQuery.setFirstResult();
            if(i == 0) {
                resultList.addAll(nameQuery.getResultList());
            }
                resultList.retainAll(nameQuery.getResultList());
        }
        return resultList;
    }

    @Transactional
    public Tag getMostUSedTag() {
        String sqlQuery = "select count(certificate_tags.tag_id) as count,t.id,t.name from certificate_tags\n" +
                "left join tags t on certificate_tags.tag_id = t.id\n" +
                "left join certificates c on certificate_tags.certificate_id = c.id\n" +
                "left join certificate_orders co on c.id = co.order_id\n" +
                "left join orders o on co.certificate_id = o.id\n" +
                "where user_id =\n" +
                "      (select sui.user_id from (select sum(price) as sm,user_id\n" +
                "                                from orders\n" +
                "                                group by user_id\n" +
                "                                order by sm desc\n" +
                "                                limit 1\n" +
                "                               ) as sui)\n" +
                "group by t.name\n" +
                "order by count desc\n" +
                "limit 1;\n";
        Session session = sessionFactory.getCurrentSession();
        Object objects = session.createSQLQuery(sqlQuery).addScalar("count", IntegerType.INSTANCE).addScalar("id",IntegerType.INSTANCE).addScalar("name", StringType.INSTANCE).uniqueResult();
        Object[] results = (Object[])objects;
        Tag tag = new Tag((String)results[2],(int)results[1]);
        return tag;
    }

    @Transactional
    public GiftCertificate update(Integer id, GiftCertificate newObj) {
        GiftCertificate certificate = getById(id);
        if(certificate == null) {
            return null;
        } else {


            if(newObj.getDuration()!=null) {
                certificate.setDuration(newObj.getDuration());
                certificate.setLastUpdateDate(new Date());
            }
            if(newObj.getPrice()!=null) {
                certificate.setPrice(newObj.getPrice());
                certificate.setLastUpdateDate(new Date());
            }
            return (GiftCertificate) sessionFactory.getCurrentSession().merge(certificate);
        }
    }

}
