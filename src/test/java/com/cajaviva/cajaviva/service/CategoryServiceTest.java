package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.repository.JPA.CategoryRepository;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.service.impl.CategoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository repository;
    private CategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(CategoryRepository.class);
        service = new CategoryServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(repository.findAll()).thenReturn(categories);

        List<Category> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        category.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(category));

        Category result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testCreate() {
        Category category = new Category();
        when(repository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = service.create(category);

        assertNotNull(result);
        verify(repository, times(1)).save(category);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        Category existing = new Category();
        existing.setId(id);

        Category updateData = new Category();
        updateData.setName("Updated name");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = service.update(id, updateData);

        assertEquals("Updated name", result.getName());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        Category updateData = new Category();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(id, updateData));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Category.class));
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}
