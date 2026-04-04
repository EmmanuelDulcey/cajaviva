package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Guardar cuenta
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    // Listar todas las cuentas
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    // Buscar cuenta por id
    public Optional<Account> findById(UUID id) {
        return accountRepository.findById(id);
    }

    // Eliminar cuenta
    public void deleteById(UUID id) {
        accountRepository.deleteById(id);
    }
}