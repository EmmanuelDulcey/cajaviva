package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.repository.JPA.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.service.impl.LiquidityProjectionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LiquidityProjectionServiceTest {

    private LiquidityProjectionRepository projectionRepository;
    private LiquidityProjectionService projectionService;

    @BeforeEach
    void setUp() {
        projectionRepository = Mockito.mock(LiquidityProjectionRepository.class);
        projectionService = new LiquidityProjectionServiceImpl(projectionRepository);
    }

    @Test
    void testCreateProjection() {
        LiquidityProjection projection = new LiquidityProjection();
        projection.setProjectedBalance(BigDecimal.valueOf(1500));
        projection.setProjectionDate(LocalDate.now());

        when(projectionRepository.save(projection)).thenReturn(projection);

        LiquidityProjection created = projectionService.create(projection);

        assertNotNull(created);
        assertEquals(BigDecimal.valueOf(1500), created.getProjectedBalance());
        verify(projectionRepository, times(1)).save(projection);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        LiquidityProjection projection = new LiquidityProjection();
        projection.setId(id);

        when(projectionRepository.findById(id)).thenReturn(Optional.of(projection));

        LiquidityProjection found = projectionService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(projectionRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll() {
        LiquidityProjection projection = new LiquidityProjection();
        projection.setProjectedBalance(BigDecimal.valueOf(2000));

        when(projectionRepository.findAll()).thenReturn(List.of(projection));

        List<LiquidityProjection> projections = projectionService.findAll();

        assertEquals(1, projections.size());
        assertEquals(BigDecimal.valueOf(2000), projections.get(0).getProjectedBalance());
        verify(projectionRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProjection() {
        UUID id = UUID.randomUUID();
        LiquidityProjection projection = new LiquidityProjection();
        projection.setId(id);
        projection.setProjectedBalance(BigDecimal.valueOf(500));

        when(projectionRepository.findById(id)).thenReturn(Optional.of(projection));
        when(projectionRepository.save(any(LiquidityProjection.class))).thenReturn(projection);

        LiquidityProjection updated = projectionService.update(id, projection);

        assertNotNull(updated);
        assertEquals(BigDecimal.valueOf(500), updated.getProjectedBalance());
        verify(projectionRepository, times(1)).save(any(LiquidityProjection.class));
    }

    @Test
    void testDeleteProjection() {
        UUID id = UUID.randomUUID();

        doNothing().when(projectionRepository).deleteById(id);

        projectionService.delete(id);

        verify(projectionRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByAccount() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);

        when(projectionRepository.findByAccount(account)).thenReturn(List.of(projection));

        List<LiquidityProjection> projections = projectionService.findByAccount(account);

        assertEquals(1, projections.size());
        assertEquals(account, projections.get(0).getAccount());
        verify(projectionRepository, times(1)).findByAccount(account);
    }

    @Test
    void testFindByProjectionDate() {
        LocalDate date = LocalDate.now();
        LiquidityProjection projection = new LiquidityProjection();
        projection.setProjectionDate(date);

        when(projectionRepository.findByProjectionDate(date)).thenReturn(List.of(projection));

        List<LiquidityProjection> projections = projectionService.findByProjectionDate(date);

        assertEquals(1, projections.size());
        assertEquals(date, projections.get(0).getProjectionDate());
        verify(projectionRepository, times(1)).findByProjectionDate(date);
    }

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        LiquidityProjection projection = new LiquidityProjection();
        projection.setId(UUID.randomUUID());

        when(projectionRepository.findByAccount_Id(accountId)).thenReturn(List.of(projection));

        List<LiquidityProjection> projections = projectionService.findByAccountId(accountId);

        assertEquals(1, projections.size());
        verify(projectionRepository, times(1)).findByAccount_Id(accountId);
    }
}
