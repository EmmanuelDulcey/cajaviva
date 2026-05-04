package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserDao {
    List<User> findAll();
    User findById(UUID id);
    User create(User user);
    User update(UUID id, User user);
    void delete(UUID id);
}
