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
    User user = userDao.findById(id);
    if (user == null) {
        throw new com.cajaviva.cajaviva.exception.ResourceNotFoundException("User not found: " + id);
    }
    return user;
}

    @Override
    public User create(User user) {
        // Si no tiene id, generar uno nuevo
        if (user.getId() == null) user.setId(UUID.randomUUID());
        return userDao.create(user);
    }

@Override
public User update(UUID id, User user) {
    User updated = userDao.update(id, user);
    if (updated == null) {
        throw new com.cajaviva.cajaviva.exception.ResourceNotFoundException("User not found: " + id);
    }
    return updated;
}

    @Override
    public void delete(UUID id) {
        userDao.delete(id);
    }
}
