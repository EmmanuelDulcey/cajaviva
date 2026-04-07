package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;

import java.util.List;
import java.util.UUID;

public interface RecurrentTransactionDao extends BaseDao<RecurrentTransaction, UUID> {

    List<RecurrentTransaction> findByAccountId(UUID account_id);

    List<RecurrentTransaction> findByCategoryId(UUID categoryId);
}
