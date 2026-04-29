package com.cajaviva.cajaviva.repository;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findByUser(User user);

    List<Account> findByAccountType(Integer accountType);
}
