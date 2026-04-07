package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecurrentTransactionRepository extends JpaRepository<RecurrentTransaction, UUID> {
    List<RecurrentTransaction> findByAccountId(UUID accountId);
}
