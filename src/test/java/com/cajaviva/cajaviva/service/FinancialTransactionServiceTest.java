package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.service.impl.FinancialTransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinancialTransactionServiceTest {

    private FinancialTransactionRepository repository;
    private FinancialTransactionServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(FinancialTransactionRepository.class);
        service = new FinancialTransactionServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        List<FinancialTransaction> transactions = Arrays.asList(new FinancialTransaction(), new FinancialTransaction());
        when(repository.findAll()).thenReturn(transactions);

        List<FinancialTransaction> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(tx));

        FinancialTransaction result = service.findById(id);

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
        FinancialTransaction tx = new FinancialTransaction();
        when(repository.save(any(FinancialTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FinancialTransaction result = service.create(tx);

        assertNotNull(result);
        verify(repository, times(1)).save(tx);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        FinancialTransaction existing = new FinancialTransaction();
        existing.setId(id);

        FinancialTransaction updateData = new FinancialTransaction();
        updateData.setDescription("Updated description");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(FinancialTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FinancialTransaction result = service.update(id, updateData);

        assertEquals("Updated description", result.getDescription());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        FinancialTransaction updateData = new FinancialTransaction();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(id, updateData));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(FinancialTransaction.class));
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        List<FinancialTransaction> transactions = Arrays.asList(new FinancialTransaction(), new FinancialTransaction());
        when(repository.findByAccount_Id(accountId)).thenReturn(transactions);

        List<FinancialTransaction> result = service.findByAccountId(accountId);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByAccount_Id(accountId);
    }
}
