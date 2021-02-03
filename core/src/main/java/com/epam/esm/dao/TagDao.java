package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ObjectNotFoundException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
@EnableTransactionManagement
public class TagDao {

    private static final int PAGE_SIZE = 10;
    private SessionFactory sessionFactory;

    @Autowired
    public TagDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Tag> getAll() {
        return initCriteria().getResultList();
    }

    @Transactional
    public Tag getByTagName(String name) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
        Root<Tag> rootEntry = cq.from(Tag.class);
        cq
                .select(rootEntry)
                .where(cb.equal(rootEntry.get("name"),name));
        TypedQuery<Tag> nameQuery = sessionFactory.getCurrentSession().createQuery(cq);
        return nameQuery.getSingleResult();
    }

    @Transactional
    public boolean delete(Integer id) {
        Tag tag;
        try {
         tag = getById(id);

        } catch(ObjectNotFoundException e) {
            return false;
        }

        sessionFactory.getCurrentSession().delete(tag);
        sessionFactory.getCurrentSession().flush();
        return true;
}

    @Transactional
    public Tag save(Tag tag) {
        sessionFactory.getCurrentSession().save(tag);
        return tag;
    }

    @Transactional
    public Tag getById(Integer id) throws ObjectNotFoundException {
        Tag tag = sessionFactory.getCurrentSession().get(Tag.class,id);
        if(tag == null)
            throw new ObjectNotFoundException();
        else
            return tag;
    }

    @Transactional
    public List<Tag> getPageOfTags(int pageNumber,int pageSize) {
        TypedQuery<Tag> typedQuery = initCriteria();
        typedQuery.setFirstResult((pageNumber-1)*pageSize);
        typedQuery.setMaxResults(pageSize);
        List<Tag> tags = typedQuery.getResultList();
        return tags;
    }

    private TypedQuery<Tag> initCriteria() {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
        Root<Tag> from = criteria.from(Tag.class);
        CriteriaQuery<Tag> select = criteria.select(from);
        return sessionFactory.getCurrentSession().createQuery(select);
    }

    @Transactional
    public Long getTagCount() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Tag> from = cq.from(Tag.class);
        CriteriaQuery<Long> select = cq.select(cb.count(from));
        return sessionFactory.getCurrentSession().createQuery(select).getSingleResult();
    }

}
