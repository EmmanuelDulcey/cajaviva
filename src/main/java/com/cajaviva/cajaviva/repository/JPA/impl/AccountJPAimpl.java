package com.cajaviva.cajaviva.repository.JPA.impl;

import com.cajaviva.cajaviva.dao.AccountDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.repository.JPA.AccountRepository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository ("AccountJPAImpl")
public class AccountJPAimpl implements AccountDao {

    private final AccountRepository accountRepository;


    public AccountJPAimpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account save(Account entity) {
        return accountRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return accountRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        accountRepository.deleteById(id);
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        // Aquí usamos el método del repositorio que recibe un User completo.
        // Si quieres mantener la búsqueda por UUID, puedes crear un User "dummy" con ese id.
        return accountRepository.findByUser(
                new com.cajaviva.cajaviva.entity.User(userId)
        );
    }

    @Override
    public List<Account> findByAccountType(Integer accountType) {
        return accountRepository.findByAccountType(accountType);
    }

    @Override
    public List<User> findByActive(boolean active) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByActive'");
    }


}
