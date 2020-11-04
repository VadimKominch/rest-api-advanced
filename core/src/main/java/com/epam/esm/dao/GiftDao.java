package com.epam.esm.dao;

import com.epam.esm.converter.DateConverter;
import com.epam.esm.entity.GiftSertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GiftDao {
    private JdbcTemplate template;
    private TagDao tagDao;

    @Autowired
    public GiftDao(DataSource source,TagDao tagDao) {
        this.template = new JdbcTemplate(source);
        this.tagDao = tagDao;
    }

    private RowMapper<GiftSertificate> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new GiftSertificate(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                new DateConverter().formatDate(resultSet.getTimestamp("creation_date")),
                new DateConverter().formatDate(resultSet.getTimestamp("last_update_time")),
                resultSet.getShort("duration"));
    };

    private RowMapper<Tag> TAG_ROW_MAPPER = (ResultSet resultSet, int rowNum) ->
            new Tag(resultSet.getString("name"),resultSet.getInt("id"));

    public List<GiftSertificate> getAll() {
        List<GiftSertificate> certificates = template.query("select * from Certificates", ROW_MAPPER);
        for (int i = 0; i < certificates.size(); i++) {
            List <Tag> tags = template.query("select * from tags join certificates_tags on tags.id = certificates_tags.tag_id where certificates_tags.certificate_id = ?",new Object[]{certificates.get(i).getId()},TAG_ROW_MAPPER);
            certificates.get(i).setTags(tags);
        }
        return certificates;
    }


    public GiftSertificate getById(Integer id) {
        List <Tag> tags = template.query("select * from tags join certificates_tags on tags.id = certificates_tags.tag_id where certificates_tags.certificate_id = ?",new Object[]{id},TAG_ROW_MAPPER);

        GiftSertificate certificate =  template.queryForObject("select * from Certificates where id = ?", new Object[]{id}, (rs, rowNum) ->
                new GiftSertificate(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        new DateConverter().formatDate(rs.getTimestamp("creation_date")),
                        new DateConverter().formatDate(rs.getTimestamp("last_update_time")),
                        rs.getShort("duration"))
        );
        certificate.setTags(tags);
        return certificate;
    }


    public boolean save(GiftSertificate entity) {
        String sql = "insert into Certificates(title,description,price,creation_date,last_update_time,duration) values (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = template.update(new PreparedStatementCreator() {
             public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                 Date date = new Date();
                 PreparedStatement pst =
                         con.prepareStatement(sql, new String[]{"id"});
                 pst.setString(1, entity.getName());
                 pst.setString(2, entity.getDescription());
                 pst.setDouble(3, entity.getPrice());
                 pst.setTimestamp(4, new Timestamp(date.getTime()));
                 pst.setTimestamp(5, new Timestamp(date.getTime()));
                 pst.setShort(6, entity.getDuration());
                 return pst;
             }
         }, keyHolder);
        List<Tag> list = tagDao.getAll();
        List<Tag> insertList = new ArrayList<>();
        entity.getTags().forEach(el->{
            if(!list.contains(el)) {
                insertList.add(tagDao.save(el));
            } else {
                insertList.add(tagDao.getByTagName(el.getName()));
            }

            });
        if(!insertList.isEmpty()) {
                insertList.forEach(el-> {
                    template.update("insert into certificates_tags(certificate_id, tag_id) VALUES (?,?)", keyHolder.getKey().intValue(), el.getId());
                });
        }
        return result > 0;
    }


    public boolean delete(Integer id) {
        template.update("delete from certificates_tags where certificate_id = ?",id);
        return template.update("delete from certificates where id = ?", id) != 0;
    }


    public GiftSertificate update(Integer id, GiftSertificate newObj) {
        template.update("update certificates set title = ?,description = ?,price=?,last_update_time = ?,duration = ? where id= ?", newObj.getName(), newObj.getDescription(), newObj.getPrice(), new Timestamp(new Date().getTime()), newObj.getDuration(), id);

        List<Tag> list = tagDao.getAll();
        List<Tag> insertList = new ArrayList<>();
        newObj.getTags().forEach(el->{
            if(!list.contains(el)) {
                insertList.add(tagDao.save(el));
            } else {
                insertList.add(tagDao.getByTagName(el.getName()));
            }

        });
        template.update("delete from certificates_tags where certificate_id= ?",id);
        if(!insertList.isEmpty()) {
            insertList.forEach(el-> {
                template.update("insert into certificates_tags(certificate_id, tag_id) VALUES (?,?)", id, el.getId());
            });
        }
        return newObj;
    }

}
