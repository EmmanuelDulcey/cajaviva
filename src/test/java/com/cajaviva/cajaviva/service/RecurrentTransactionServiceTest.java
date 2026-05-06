package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.repository.JPA.RecurrentTransactionRepository;
import com.cajaviva.cajaviva.service.impl.RecurrentTransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecurrentTransactionServiceTest {

    private RecurrentTransactionRepository recurrentTransactionRepository;
    private RecurrentTransactionService recurrentTransactionService;

    @BeforeEach
    void setUp() {
        recurrentTransactionRepository = Mockito.mock(RecurrentTransactionRepository.class);
        recurrentTransactionService = new RecurrentTransactionServiceImpl(recurrentTransactionRepository);
    }

    @Test
    void testCreateRecurrentTransaction() {
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setAmount(BigDecimal.valueOf(1000));

        when(recurrentTransactionRepository.save(tx)).thenReturn(tx);

        RecurrentTransaction created = recurrentTransactionService.create(tx);

        assertNotNull(created);
        assertEquals(BigDecimal.valueOf(1000), created.getAmount());
        verify(recurrentTransactionRepository, times(1)).save(tx);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setId(id);

        when(recurrentTransactionRepository.findById(id)).thenReturn(Optional.of(tx));

        RecurrentTransaction found = recurrentTransactionService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(recurrentTransactionRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll() {
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setAmount(BigDecimal.valueOf(500));

        when(recurrentTransactionRepository.findAll()).thenReturn(List.of(tx));

        List<RecurrentTransaction> transactions = recurrentTransactionService.findAll();

        assertEquals(1, transactions.size());
        assertEquals(BigDecimal.valueOf(500), transactions.get(0).getAmount());
        verify(recurrentTransactionRepository, times(1)).findAll();
    }

    @Test
    void testUpdateRecurrentTransaction() {
        UUID id = UUID.randomUUID();
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setId(id);
        tx.setAmount(BigDecimal.valueOf(200));

        when(recurrentTransactionRepository.findById(id)).thenReturn(Optional.of(tx));
        when(recurrentTransactionRepository.save(any(RecurrentTransaction.class))).thenReturn(tx);

        RecurrentTransaction updated = recurrentTransactionService.update(id, tx);

        assertNotNull(updated);
        assertEquals(BigDecimal.valueOf(200), updated.getAmount());
        verify(recurrentTransactionRepository, times(1)).save(any(RecurrentTransaction.class));
    }

    @Test
    void testDeleteRecurrentTransaction() {
        UUID id = UUID.randomUUID();

        doNothing().when(recurrentTransactionRepository).deleteById(id);

        recurrentTransactionService.delete(id);

        verify(recurrentTransactionRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByAccount() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setAccount(account);

        when(recurrentTransactionRepository.findByAccount(account)).thenReturn(List.of(tx));

        List<RecurrentTransaction> transactions = recurrentTransactionService.findByAccount(account);

        assertEquals(1, transactions.size());
        assertEquals(account, transactions.get(0).getAccount());
        verify(recurrentTransactionRepository, times(1)).findByAccount(account);
    }

    @Test
    void testFindByCategory() {
        Category category = new Category();
        category.setId(UUID.randomUUID());
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setCategory(category);

        when(recurrentTransactionRepository.findByCategory(category)).thenReturn(List.of(tx));

        List<RecurrentTransaction> transactions = recurrentTransactionService.findByCategory(category);

        assertEquals(1, transactions.size());
        assertEquals(category, transactions.get(0).getCategory());
        verify(recurrentTransactionRepository, times(1)).findByCategory(category);
    }

    @Test
    void testFindByStatus() {
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setStatus(1);

        when(recurrentTransactionRepository.findByStatus(1)).thenReturn(List.of(tx));

        List<RecurrentTransaction> transactions = recurrentTransactionService.findByStatus(1);

        assertEquals(1, transactions.size());
        assertEquals(1, transactions.get(0).getStatus());
        verify(recurrentTransactionRepository, times(1)).findByStatus(1);
    }

    @Test
    void testFindByFrequency() {
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setFrequency(30);

        when(recurrentTransactionRepository.findByFrequency(30)).thenReturn(List.of(tx));

        List<RecurrentTransaction> transactions = recurrentTransactionService.findByFrequency(30);

        assertEquals(1, transactions.size());
        assertEquals(30, transactions.get(0).getFrequency());
        verify(recurrentTransactionRepository, times(1)).findByFrequency(30);
    }

    @Test
    void testFindByCustomFrequency() {
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setCustomFrequency(7);

        when(recurrentTransactionRepository.findByCustomFrequency(7)).thenReturn(List.of(tx));

        List<RecurrentTransaction> transactions = recurrentTransactionService.findByCustomFrequency(7);

        assertEquals(1, transactions.size());
        assertEquals(7, transactions.get(0).getCustomFrequency());
        verify(recurrentTransactionRepository, times(1)).findByCustomFrequency(7);
    }

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setId(UUID.randomUUID());

        when(recurrentTransactionRepository.findByAccount_Id(accountId)).thenReturn(List.of(tx));

        List<RecurrentTransaction> transactions = recurrentTransactionService.findByAccountId(accountId);

        assertEquals(1, transactions.size());
        verify(recurrentTransactionRepository, times(1)).findByAccount_Id(accountId);
    }
}
