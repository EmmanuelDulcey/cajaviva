package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;

import java.util.List;
import java.util.UUID;

public interface FinancialTransactionDao extends BaseDao<FinancialTransaction, UUID> {

    List<FinancialTransaction> findByAccount(Account account);
    List<FinancialTransaction> findByStatus(Integer status);
    List<FinancialTransaction> findByCategory(Category category);
    List<FinancialTransaction> findByAccountAndCategory(Account account, Category category);
    List<FinancialTransaction> findByAccountId(UUID account_id);
    List<FinancialTransaction> findByCategoryId(UUID categoryId);

}
