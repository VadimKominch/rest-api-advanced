package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private TagDao tagDao;

    @Autowired
    public UserDao(DataSource source, TagDao tagDao) {
        this.jdbcTemplate = new JdbcTemplate(source);
        this.tagDao = tagDao;
    }

    private RowMapper<User> ROW_MAPPER = (ResultSet resultSet, int rowNum) ->
            new User(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("surname"));

    public List<User> getAll() {
        return jdbcTemplate.query("select * from Users",ROW_MAPPER);
    }


    public User getById(Integer id) {
        return jdbcTemplate.queryForObject("select * from Users where id = ?",new Object[]{id},(rs, rowNum) ->
                new User(rs.getInt("id"),rs.getString("name"),rs.getString("surname")));
    }

    public User getByTagName(String userName) {
        return jdbcTemplate.queryForObject("select * from Users where name = ?",new Object[]{userName},(rs, rowNum) ->
                new User(rs.getInt("id"),rs.getString("name"),rs.getString("surname")));
    }


    public User save(User entity) {
        String sql = "insert into Users (name,surname) values (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pst =
                        con.prepareStatement(sql, new String[]{"id"});
                pst.setString(1,entity.getName());
                pst.setString(2,entity.getSurname());
                return pst;
            }
        },keyHolder);
        entity.setId(keyHolder.getKey().intValue());
        return entity;
    }


    public boolean delete(Integer id) {
        jdbcTemplate.update("delete from certificate_tags where tag_id = ?",id);
        return jdbcTemplate.update("delete from Tags where id = ?", id)!=0;
    }

    /**
     *
     * Pageable method for pagination*/
    public List<User> findAll(Integer pageNumber, Integer pageSize) {
        String sql = "select * from Users limit ?,?";
        int start_index = (pageNumber - 1)*pageSize;
        jdbcTemplate.query(sql,ROW_MAPPER);

        return null;
    }
}
