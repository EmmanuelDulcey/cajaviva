package com.cajaviva.cajaviva.dao;

import java.util.List;
import java.util.Optional;

import com.cajaviva.cajaviva.entity.User;

public interface BaseDao<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    boolean existsById(ID id);

    void deleteById(ID id);

    List<User> findByActive(boolean active);
}
