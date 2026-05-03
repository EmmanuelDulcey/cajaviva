package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.repository.JPA.CategoryRepository;
import com.cajaviva.cajaviva.service.CategoryService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(UUID id, Category category) {
        Category existing = findById(id);
        existing.setName(category.getName());
        existing.setType(category.getType());
        existing.setDescription(category.getDescription());
        existing.setUpdatedAt(category.getUpdatedAt());

        return categoryRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }
}
