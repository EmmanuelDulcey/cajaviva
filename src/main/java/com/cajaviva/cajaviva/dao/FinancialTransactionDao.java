package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.FinancialTransaction;

import java.util.List;
import java.util.UUID;

public interface FinancialTransactionDao extends BaseDao<FinancialTransaction, UUID> {

    List<FinancialTransaction> findByAccountId(UUID account_id);
}
