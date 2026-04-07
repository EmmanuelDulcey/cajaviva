package com.cajaviva.cajaviva.dao.jpa;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecurrentTransactionJpaRepository extends JpaRepository<RecurrentTransaction, UUID> {

    List<RecurrentTransaction> findByAccountId(UUID accountId);
}
