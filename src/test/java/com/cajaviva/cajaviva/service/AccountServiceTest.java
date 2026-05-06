package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.repository.JPA.AccountRepository;
import com.cajaviva.cajaviva.service.impl.AccountServiceImpl; // implementación concreta
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    void testCreateAccount() {
        Account account = new Account();
        account.setName("Cuenta prueba");

        when(accountRepository.save(account)).thenReturn(account);

        Account created = accountService.create(account);

        assertNotNull(created);
        assertEquals("Cuenta prueba", created.getName());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testFindByIdSuccess() {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        Account found = accountService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(accountRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Account result = accountService.findById(id);

        assertNull(result); // 👈 tu servicio devuelve null
        verify(accountRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll() {
        Account account = new Account();
        account.setName("Cuenta prueba");

        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<Account> accounts = accountService.findAll();

        assertEquals(1, accounts.size());
        assertEquals("Cuenta prueba", accounts.get(0).getName());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testUpdateAccountSuccess() {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        account.setName("Cuenta prueba");

        when(accountRepository.existsById(id)).thenReturn(true);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updated = accountService.update(id, account);

        assertNotNull(updated);
        assertEquals("Cuenta prueba", updated.getName());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateAccountNotFound() {
        UUID id = UUID.randomUUID();
        Account account = new Account();

        when(accountRepository.existsById(id)).thenReturn(false);

        Account result = accountService.update(id, account);

        assertNull(result); // 👈 tu servicio devuelve null
        verify(accountRepository, times(1)).existsById(id);
    }

    @Test
    void testDeleteAccountSuccess() {
        UUID id = UUID.randomUUID();

        when(accountRepository.existsById(id)).thenReturn(true);
        doNothing().when(accountRepository).deleteById(id);

        accountService.delete(id);

        verify(accountRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteAccountNotFound() {
        UUID id = UUID.randomUUID();

        when(accountRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> accountService.delete(id));
        verify(accountRepository, times(1)).existsById(id);
    }

    @Test
    void testFindByUserId() {
        UUID userId = UUID.randomUUID();
        Account account = new Account();
        account.setId(UUID.randomUUID());

        when(accountRepository.findByUserId(userId)).thenReturn(List.of(account));

        List<Account> accounts = accountService.findByUserId(userId);

        assertEquals(1, accounts.size());
        verify(accountRepository, times(1)).findByUserId(userId);
    }
}
