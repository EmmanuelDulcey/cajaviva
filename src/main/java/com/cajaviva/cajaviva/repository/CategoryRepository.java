package com.cajaviva.cajaviva.repository;

import com.cajaviva.cajaviva.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByType(Integer type);
    List<Category> findByNameContainingIgnoreCase(String name);
}
