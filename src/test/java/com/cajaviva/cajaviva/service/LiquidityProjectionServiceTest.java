package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.repository.JPA.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.service.impl.LiquidityProjectionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LiquidityProjectionServiceTest {

    private LiquidityProjectionRepository repository;
    private LiquidityProjectionServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(LiquidityProjectionRepository.class);
        service = new LiquidityProjectionServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        List<LiquidityProjection> projections = Arrays.asList(new LiquidityProjection(), new LiquidityProjection());
        when(repository.findAll()).thenReturn(projections);

        List<LiquidityProjection> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        LiquidityProjection projection = new LiquidityProjection();
        projection.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(projection));

        LiquidityProjection result = service.findById(id);

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
        LiquidityProjection projection = new LiquidityProjection();
        when(repository.save(any(LiquidityProjection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LiquidityProjection result = service.create(projection);

        assertNotNull(result);
        verify(repository, times(1)).save(projection);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        LiquidityProjection existing = new LiquidityProjection();
        existing.setId(id);

        LiquidityProjection updateData = new LiquidityProjection();
        updateData.setAmount(new java.math.BigDecimal("1000"));

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(LiquidityProjection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LiquidityProjection result = service.update(id, updateData);

        assertEquals(new java.math.BigDecimal("1000"), result.getAmount());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        LiquidityProjection updateData = new LiquidityProjection();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(id, updateData));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(LiquidityProjection.class));
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testFindByAccount() {
        Account account = new Account();
        List<LiquidityProjection> projections = Arrays.asList(new LiquidityProjection(), new LiquidityProjection());
        when(repository.findByAccount(account)).thenReturn(projections);

        List<LiquidityProjection> result = service.findByAccount(account);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByAccount(account);
    }

    @Test
    void testFindByProjectionDate() {
        LocalDate date = LocalDate.now();
        List<LiquidityProjection> projections = Arrays.asList(new LiquidityProjection(), new LiquidityProjection());
        when(repository.findByProjectionDate(date)).thenReturn(projections);

        List<LiquidityProjection> result = service.findByProjectionDate(date);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByProjectionDate(date);
    }

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        List<LiquidityProjection> projections = Arrays.asList(new LiquidityProjection(), new LiquidityProjection());
        when(repository.findByAccount_Id(accountId)).thenReturn(projections);

        List<LiquidityProjection> result = service.findByAccountId(accountId);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByAccount_Id(accountId);
    }
}
