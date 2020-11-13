package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {
    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public List<User> getAll() {
        try {
            return userDao.getAll();
        }catch (DataAccessException e) {
            return null;
        }
    }


    public User getById(Integer id) {
        try {
            User User =  userDao.getById(id);
            return User;
        }catch (DataAccessException e) {
            return null;
        }
    }


    public User save(User entity) {
        return userDao.save(entity);
    }


    public boolean delete(Integer id) {
        return userDao.delete(id);
    }

    public User getByName(String name) {
        return userDao.getByTagName(name);
    }
}
