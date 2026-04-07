package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.CategoryDao;
import com.cajaviva.cajaviva.dao.jpa.CategoryJpaRepository;
import com.cajaviva.cajaviva.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryDaoImpl(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public Category save(Category entity) {
        return categoryJpaRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name);
    }
}
