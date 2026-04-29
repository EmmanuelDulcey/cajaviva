package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.entity.FinancialTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao extends BaseDao<Category, UUID> {

    List<Category> findByName(String name);
    List<Category> findByType(Integer type);
    List<FinancialTransaction> findByAccount(Account account);
    List<FinancialTransaction> findByStatus(Integer status);
}
