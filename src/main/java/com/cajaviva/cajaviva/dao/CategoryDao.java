package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao extends BaseDao<Category, UUID> {

    Optional<Category> findByName(String name);

    List<Category> findByType(Integer type);
}
