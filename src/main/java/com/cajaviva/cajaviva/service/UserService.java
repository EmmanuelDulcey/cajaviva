package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> findAll();

    User findById(UUID id);

    User create(User user);

    User update(UUID id, User user);

    void delete(UUID id);
}
