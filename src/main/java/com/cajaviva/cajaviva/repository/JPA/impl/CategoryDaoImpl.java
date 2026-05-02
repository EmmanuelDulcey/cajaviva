package com.cajaviva.cajaviva.repository.JPA.impl;

import com.cajaviva.cajaviva.dao.CategoryDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.repository.JPA.CategoryRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository ("CategoryJPAImpl")
public class CategoryDaoImpl implements CategoryDao {

    private final CategoryRepository categoryRepository;

    public CategoryDaoImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> findByType(Integer type) {
        return categoryRepository.findByType(type);
    }

    @Override
    public List<Category> findByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<FinancialTransaction> findByAccount(Account account) {
        throw new UnsupportedOperationException("Unimplemented method 'findByAccount'");
    }

    @Override
    public List<FinancialTransaction> findByStatus(Integer status) {
        throw new UnsupportedOperationException("Unimplemented method 'findByStatus'");
    }

    @Override
    public List<User> findByActive(boolean active) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByActive'");
    }
}
