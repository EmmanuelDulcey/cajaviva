package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    public Optional<User> findById(UUID id) {
        return userDao.findById(id);
    }

    @Override
    public User save(User user) {

        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userDao.save(user);
    }

    @Override
    public User update(UUID id, User user) {

        if (!userDao.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        user.setId(id);
        user.setUpdatedAt(LocalDateTime.now());

        return userDao.save(user);
    }

    @Override
    public void delete(UUID id) {

        if (!userDao.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userDao.deleteById(id);
    }
}