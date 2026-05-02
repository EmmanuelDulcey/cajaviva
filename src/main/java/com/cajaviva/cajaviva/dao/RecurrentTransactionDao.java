package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import java.util.List;
import java.util.UUID;

public interface RecurrentTransactionDao extends BaseDao<RecurrentTransaction, UUID> {
    List<RecurrentTransaction> findByAccount(UUID account_id);
    List<RecurrentTransaction> findByCategory(Category category);
    List<RecurrentTransaction> findByFrequency(Integer frequency);
    List<RecurrentTransaction> findByCustomFrequency(Integer customFrequency);
    List<RecurrentTransaction> findByStatus(Integer status);
    List<RecurrentTransaction> findByAccountId(UUID account_id);
    List<RecurrentTransaction> findByAccount(Account account);
    List<RecurrentTransaction> findByCategoryId(UUID categoryId);
}
