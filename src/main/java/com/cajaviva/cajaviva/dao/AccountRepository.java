package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByPersonId(UUID personId);
}
