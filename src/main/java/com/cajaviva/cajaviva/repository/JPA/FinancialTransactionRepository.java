package com.cajaviva.cajaviva.repository.JPA;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {
    List<FinancialTransaction> findByAccount(Account account);
    List<FinancialTransaction> findByAccount_Id(UUID accountId);
    List<FinancialTransaction> findByCategory(Category category);
    List<FinancialTransaction> findByCategory_Id(UUID categoryId);
    List<FinancialTransaction> findByStatus(Integer status);
    List<FinancialTransaction> findByAccountAndCategory(Account account, Category category);
}
