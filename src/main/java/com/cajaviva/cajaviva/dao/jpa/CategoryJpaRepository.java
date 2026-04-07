package com.cajaviva.cajaviva.dao.jpa;

import com.cajaviva.cajaviva.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByName(String name);
}
