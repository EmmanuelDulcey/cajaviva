package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository;
import com.cajaviva.cajaviva.service.impl.FinancialTransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinancialTransactionServiceTest {

    private FinancialTransactionRepository transactionRepository;
    private FinancialTransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(FinancialTransactionRepository.class);
        transactionService = new FinancialTransactionServiceImpl(transactionRepository);
    }

    @Test
    void testCreateTransaction() {
        FinancialTransaction tx = new FinancialTransaction();
        tx.setValue(BigDecimal.valueOf(1000));
        tx.setDescription("Pago prueba");

        when(transactionRepository.save(tx)).thenReturn(tx);

        FinancialTransaction created = transactionService.create(tx);

        assertNotNull(created);
        assertEquals("Pago prueba", created.getDescription());
        assertEquals(BigDecimal.valueOf(1000), created.getValue());
        verify(transactionRepository, times(1)).save(tx);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(id);

        when(transactionRepository.findById(id)).thenReturn(Optional.of(tx));

        FinancialTransaction found = transactionService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(transactionRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll() {
        FinancialTransaction tx = new FinancialTransaction();
        tx.setValue(BigDecimal.valueOf(500));

        when(transactionRepository.findAll()).thenReturn(List.of(tx));

        List<FinancialTransaction> transactions = transactionService.findAll();

        assertEquals(1, transactions.size());
        assertEquals(BigDecimal.valueOf(500), transactions.get(0).getValue());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testUpdateTransaction() {
        UUID id = UUID.randomUUID();
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(id);
        tx.setDescription("Original");

        when(transactionRepository.findById(id)).thenReturn(Optional.of(tx));
        when(transactionRepository.save(any(FinancialTransaction.class))).thenReturn(tx);

        FinancialTransaction updated = transactionService.update(id, tx);

        assertNotNull(updated);
        assertEquals("Original", updated.getDescription());
        verify(transactionRepository, times(1)).save(any(FinancialTransaction.class));
    }

    @Test
    void testDeleteTransaction() {
        UUID id = UUID.randomUUID();

        doNothing().when(transactionRepository).deleteById(id);

        transactionService.delete(id);

        verify(transactionRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(UUID.randomUUID());
        tx.setValue(BigDecimal.valueOf(300));

        when(transactionRepository.findByAccount_Id(accountId)).thenReturn(List.of(tx));

        List<FinancialTransaction> transactions = transactionService.findByAccountId(accountId);

        assertEquals(1, transactions.size());
        assertEquals(BigDecimal.valueOf(300), transactions.get(0).getValue());
        verify(transactionRepository, times(1)).findByAccount_Id(accountId);
    }
}
