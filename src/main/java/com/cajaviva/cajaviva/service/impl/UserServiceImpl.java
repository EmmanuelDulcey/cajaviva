package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(UUID id) {
        return userDao.findById(id);
    }

    @Override
    public User create(User user) {
        return userDao.create(user);
    }

    @Override
    public User update(UUID id, User user) {
        return userDao.update(id, user);
    }

    @Override
    public void delete(UUID id) {
        userDao.delete(id);
    }
}