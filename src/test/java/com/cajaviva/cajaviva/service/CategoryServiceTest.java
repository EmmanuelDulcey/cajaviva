package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.repository.JPA.CategoryRepository;
import com.cajaviva.cajaviva.service.impl.CategoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setName("Categoría prueba");

        when(categoryRepository.save(category)).thenReturn(category);

        Category created = categoryService.create(category);

        assertNotNull(created);
        assertEquals("Categoría prueba", created.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        category.setId(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Category found = categoryService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll() {
        Category category = new Category();
        category.setName("Categoría prueba");

        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> categories = categoryService.findAll();

        assertEquals(1, categories.size());
        assertEquals("Categoría prueba", categories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCategory() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        category.setId(id);
        category.setName("Nombre original");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category updated = categoryService.update(id, category);

        assertNotNull(updated);
        assertEquals("Nombre original", updated.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDeleteCategory() {
        UUID id = UUID.randomUUID();

        doNothing().when(categoryRepository).deleteById(id);

        categoryService.delete(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }
}
