package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(UUID id);

    User save(User user);

    User update(UUID id, User user);

    void delete(UUID id);
}