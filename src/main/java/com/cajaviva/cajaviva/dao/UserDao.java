package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao extends BaseDao<User, UUID> {

    Optional<User> findByEmail(String email);
}