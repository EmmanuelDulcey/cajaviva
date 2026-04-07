package com.cajaviva.cajaviva.dao.jpa;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FinancialTransactionJpaRepository extends JpaRepository<FinancialTransaction, UUID> {

    List<FinancialTransaction> findByAccountId(UUID accountId);
}
