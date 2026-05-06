package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.repository.JPA.AlertRepository;
import com.cajaviva.cajaviva.service.impl.AlertServiceImpl;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertServiceTest {

    private AlertRepository alertRepository;
    private AlertService alertService;

    @BeforeEach
    void setUp() {
        alertRepository = Mockito.mock(AlertRepository.class);
        alertService = new AlertServiceImpl(alertRepository);
    }

    @Test
    void testCreateAlert() {
        Alert alert = new Alert();
        alert.setMessage("Alerta prueba");

        when(alertRepository.save(alert)).thenReturn(alert);

        Alert created = alertService.create(alert);

        assertNotNull(created);
        assertEquals("Alerta prueba", created.getMessage());
        verify(alertRepository, times(1)).save(alert);
    }

    @Test
    void testFindByIdSuccess() {
        UUID id = UUID.randomUUID();
        Alert alert = new Alert();
        alert.setId(id);

        when(alertRepository.findById(id)).thenReturn(Optional.of(alert));

        Alert found = alertService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(alertRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(alertRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alertService.findById(id));
        verify(alertRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll() {
        Alert alert = new Alert();
        alert.setMessage("Alerta prueba");

        when(alertRepository.findAll()).thenReturn(List.of(alert));

        List<Alert> alerts = alertService.findAll();

        assertEquals(1, alerts.size());
        assertEquals("Alerta prueba", alerts.get(0).getMessage());
        verify(alertRepository, times(1)).findAll();
    }

    @Test
    void testUpdateAlert() {
        UUID id = UUID.randomUUID();
        Alert existing = new Alert();
        existing.setId(id);
        existing.setMessage("Original");

        Alert updatedData = new Alert();
        updatedData.setMessage("Actualizado");

        when(alertRepository.findById(id)).thenReturn(Optional.of(existing));
        when(alertRepository.save(any(Alert.class))).thenReturn(existing);

        Alert updated = alertService.update(id, updatedData);

        assertNotNull(updated);
        assertEquals("Actualizado", updated.getMessage());
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void testDeleteAlert() {
        UUID id = UUID.randomUUID();

        doNothing().when(alertRepository).deleteById(id);

        alertService.delete(id);

        verify(alertRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByLiquidityProjectionId() {
        UUID projectionId = UUID.randomUUID();
        Alert alert = new Alert();
        alert.setId(UUID.randomUUID());

        when(alertRepository.findByLiquidityProjection_Id(projectionId)).thenReturn(List.of(alert));

        List<Alert> alerts = alertService.findByLiquidityProjectionId(projectionId);

        assertEquals(1, alerts.size());
        verify(alertRepository, times(1)).findByLiquidityProjection_Id(projectionId);
    }
}
