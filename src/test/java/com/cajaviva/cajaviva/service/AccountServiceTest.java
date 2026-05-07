package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.repository.JPA.AccountRepository;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.service.impl.AccountServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository repository;
    private AccountServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(AccountRepository.class);
        service = new AccountServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        List<Account> accounts = Arrays.asList(new Account(), new Account());
        when(repository.findAll()).thenReturn(accounts);

        List<Account> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(account));

        Account result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Account result = service.findById(id);

        assertNull(result);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testCreateGeneratesIdAndTimestamps() {
        Account account = new Account();
        when(repository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = service.create(account);

        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(repository, times(1)).save(result);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = service.update(id, account);

        assertEquals(id, result.getId());
        assertNotNull(result.getUpdatedAt());
        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).save(result);
    }

    @Test
    void testUpdateNotExisting() {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        when(repository.existsById(id)).thenReturn(false);

        Account result = service.update(id, account);

        assertNull(result);
        verify(repository, times(1)).existsById(id);
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void testDeleteExisting() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteNotExistingThrowsException() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
        verify(repository, times(1)).existsById(id);
        verify(repository, never()).deleteById(id);
    }

    @Test
    void testFindByUserId() {
        UUID userId = UUID.randomUUID();
        List<Account> accounts = Arrays.asList(new Account(), new Account());
        when(repository.findByUserId(userId)).thenReturn(accounts);

        List<Account> result = service.findByUserId(userId);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByUserId(userId);
    }
}
