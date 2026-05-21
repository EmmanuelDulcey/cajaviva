package com.cajaviva.cajaviva.repository.JPA;

import com.cajaviva.cajaviva.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByType(Integer type);
    List<Category> findByNameContainingIgnoreCase(String name);
    Optional<Category> findByNameIgnoreCase(String name);
}
