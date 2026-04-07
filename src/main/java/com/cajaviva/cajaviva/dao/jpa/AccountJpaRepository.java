package com.cajaviva.cajaviva.dao.jpa;

import com.cajaviva.cajaviva.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<Account, UUID> {

    List<Account> findByUserId(UUID userId);
}
