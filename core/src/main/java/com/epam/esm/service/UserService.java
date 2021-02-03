package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class UserService {
    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public List<User> getAll(int pageNumber,int pageSize) {
            return userDao.findAll(pageNumber,pageSize);
    }


    public User getById(Integer id) {
            User User =  userDao.getById(id);
            return User;
    }

    @Transactional
    public User save(User entity) {
        return userDao.save(entity);
    }

    @Transactional
    public void saveAll(List<User> users) {users.forEach(el->userDao.save(el));}

    public boolean delete(Integer id) {
        return userDao.delete(id);
    }

    public User getByName(String name) {
        return userDao.getByTagName(name);
    }

    public Long getUserCount() {
        return userDao.getUserCount();
    }
}
