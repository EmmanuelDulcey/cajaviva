package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.repository.JPA.RecurrentTransactionRepository;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.service.impl.RecurrentTransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecurrentTransactionServiceTest {

    private RecurrentTransactionRepository repository;
    private RecurrentTransactionServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(RecurrentTransactionRepository.class);
        service = new RecurrentTransactionServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        List<RecurrentTransaction> transactions = Arrays.asList(new RecurrentTransaction(), new RecurrentTransaction());
        when(repository.findAll()).thenReturn(transactions);

        List<RecurrentTransaction> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(tx));

        RecurrentTransaction result = service.findById(id);

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
        RecurrentTransaction tx = new RecurrentTransaction();
        when(repository.save(any(RecurrentTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecurrentTransaction result = service.create(tx);

        assertNotNull(result);
        verify(repository, times(1)).save(tx);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        RecurrentTransaction existing = new RecurrentTransaction();
        existing.setId(id);

        RecurrentTransaction updateData = new RecurrentTransaction();
        updateData.setAmount(new java.math.BigDecimal("500"));

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(RecurrentTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecurrentTransaction result = service.update(id, updateData);

        assertEquals(new java.math.BigDecimal("500"), result.getAmount());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        RecurrentTransaction updateData = new RecurrentTransaction();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(id, updateData));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(RecurrentTransaction.class));
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
        List<RecurrentTransaction> transactions = Arrays.asList(new RecurrentTransaction(), new RecurrentTransaction());
        when(repository.findByAccount(account)).thenReturn(transactions);

        List<RecurrentTransaction> result = service.findByAccount(account);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByAccount(account);
    }

    @Test
    void testFindByCategory() {
        Category category = new Category();
        List<RecurrentTransaction> transactions = Arrays.asList(new RecurrentTransaction(), new RecurrentTransaction());
        when(repository.findByCategory(category)).thenReturn(transactions);

        List<RecurrentTransaction> result = service.findByCategory(category);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByCategory(category);
    }

    @Test
    void testFindByStatus() {
        Integer status = 1;
        List<RecurrentTransaction> transactions = Arrays.asList(new RecurrentTransaction(), new RecurrentTransaction());
        when(repository.findByStatus(status)).thenReturn(transactions);

        List<RecurrentTransaction> result = service.findByStatus(status);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByStatus(status);
    }

    @Test
    void testFindByFrequency() {
        Integer frequency = 30;
        List<RecurrentTransaction> transactions = Arrays.asList(new RecurrentTransaction(), new RecurrentTransaction());
        when(repository.findByFrequency(frequency)).thenReturn(transactions);

        List<RecurrentTransaction> result = service.findByFrequency(frequency);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByFrequency(frequency);
    }

    @Test
    void testFindByCustomFrequency() {
        Integer customFrequency = 45;
        List<RecurrentTransaction> transactions = Arrays.asList(new RecurrentTransaction(), new RecurrentTransaction());
        when(repository.findByCustomFrequency(customFrequency)).thenReturn(transactions);

        List<RecurrentTransaction> result = service.findByCustomFrequency(customFrequency);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByCustomFrequency(customFrequency);
    }

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        List<RecurrentTransaction> transactions = Arrays.asList(new RecurrentTransaction(), new RecurrentTransaction());
        when(repository.findByAccount_Id(accountId)).thenReturn(transactions);

        List<RecurrentTransaction> result = service.findByAccountId(accountId);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByAccount_Id(accountId);
    }
}
