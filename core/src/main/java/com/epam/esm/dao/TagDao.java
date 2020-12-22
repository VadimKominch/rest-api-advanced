package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
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
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Transactional
@Component
public class TagDao {
    /*private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDao(DataSource source) {
        jdbcTemplate = new JdbcTemplate(source);
    }

    private RowMapper<Tag> ROW_MAPPER = (ResultSet resultSet, int rowNum) ->
            new Tag(resultSet.getString("name"),resultSet.getInt("id"));

    public List<Tag> getAll() {
        return jdbcTemplate.query("select * from Tags",ROW_MAPPER);
    }


    public Tag getById(Integer id) {
        return jdbcTemplate.queryForObject("select * from Tags where id = ?",new Object[]{id},(rs, rowNum) ->
                new Tag(rs.getString("name"),rs.getInt("id")));
    }

    public Tag getByTagName(String tagName) {
        return jdbcTemplate.queryForObject("select * from tags where name = ?",new Object[]{tagName},(rs, rowNum) ->
                new Tag(rs.getString("name"),rs.getInt("id")));
    }


    public Tag save(Tag entity) {
        String sql = "insert into Tags(name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pst =
                        con.prepareStatement(sql, new String[]{"id"});
                pst.setString(1,entity.getName());
                return pst;
            }
        },keyHolder);
        entity.setId(keyHolder.getKey().intValue());
        return entity;
    }


    public boolean delete(Integer id) {
        jdbcTemplate.update("delete from certificate_tags where tag_id = ?",id);
        return jdbcTemplate.update("delete from Tags where id = ?", id)!=0;
    }*/

    @Autowired
    private SessionFactory sessionFactory;

    public List<Tag> getAll() {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
        Root<Tag> rootEntry = cq.from(Tag.class);
        CriteriaQuery<Tag> all = cq.select(rootEntry);
        TypedQuery<Tag> allQuery = sessionFactory.getCurrentSession().createQuery(all);
        return allQuery.getResultList();
    }

    public Tag getByTagName(String name) {
           return sessionFactory.getCurrentSession().get(Tag.class,name);
    }

    public boolean delete(Integer id) {
        Tag tag = getById(id);
        if(tag == null) {
            return false;
        } else  {
            CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
            CriteriaDelete<Tag> cq = cb.createCriteriaDelete(Tag.class);
            Root<Tag> rootEntry = cq.from(Tag.class);
            cq.where(cb.equal(rootEntry.get("id"),id));
            Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
            sessionFactory.getCurrentSession().createQuery(cq).executeUpdate();
            transaction.commit();
            return true;
        }
    }

    @Transactional
    public Tag save(Tag tag) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(tag);
        return tag;
    }

    public Tag getById(Integer id) {
        return sessionFactory.getCurrentSession().get(Tag.class,id);
    }

}
