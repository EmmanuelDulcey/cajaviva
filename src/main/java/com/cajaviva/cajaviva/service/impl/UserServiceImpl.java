package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            user.setLastName(user.getName());
        }
        if (user.getPasswordDigest() == null || user.getPasswordDigest().isBlank()) {
            throw new com.cajaviva.cajaviva.exception.BusinessValidationException("passwordDigest es requerido.");
        }
        user.setActive(true);
        LocalDateTime now = LocalDateTime.now();
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(now);
        }
        user.setUpdatedAt(now);
        return userDao.create(user);
    }

@Override
public User update(UUID id, User user) {
    User existing = userDao.findById(id);
    if (existing == null) {
        throw new com.cajaviva.cajaviva.exception.ResourceNotFoundException("User not found: " + id);
    }
    if (user.getLastName() == null || user.getLastName().isBlank()) {
        user.setLastName(user.getName());
    }
    if (user.getPasswordDigest() == null || user.getPasswordDigest().isBlank()) {
        user.setPasswordDigest(existing.getPasswordDigest());
    }
    user.setActive(existing.isActive());
    user.setCreatedAt(existing.getCreatedAt());
    user.setUpdatedAt(LocalDateTime.now());
    User updated = userDao.update(id, user);
    return updated;
}

    @Override
    public void delete(UUID id) {
        userDao.delete(id);
    }
}
