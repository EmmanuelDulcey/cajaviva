package com.cajaviva.cajaviva.repository;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {
}