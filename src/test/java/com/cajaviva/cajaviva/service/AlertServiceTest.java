package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.repository.JPA.AlertRepository;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.service.impl.AlertServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertServiceTest {

    private AlertRepository repository;
    private AlertServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(AlertRepository.class);
        service = new AlertServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        List<Alert> alerts = Arrays.asList(new Alert(), new Alert());
        when(repository.findAll()).thenReturn(alerts);

        List<Alert> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        Alert alert = new Alert();
        alert.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(alert));

        Alert result = service.findById(id);

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
        Alert alert = new Alert();
        when(repository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Alert result = service.create(alert);

        assertNotNull(result);
        verify(repository, times(1)).save(alert);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        Alert existing = new Alert();
        existing.setId(id);

        Alert updateData = new Alert();
        updateData.setMessage("Updated message");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Alert result = service.update(id, updateData);

        assertEquals("Updated message", result.getMessage());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        Alert updateData = new Alert();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(id, updateData));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Alert.class));
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testFindByLiquidityProjectionId() {
        UUID projectionId = UUID.randomUUID();
        List<Alert> alerts = Arrays.asList(new Alert(), new Alert());
        when(repository.findByLiquidityProjection_Id(projectionId)).thenReturn(alerts);

        List<Alert> result = service.findByLiquidityProjectionId(projectionId);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByLiquidityProjection_Id(projectionId);
    }
}
