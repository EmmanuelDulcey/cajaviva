package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.Account;

import java.util.List;
import java.util.UUID;

public interface AccountDao extends BaseDao<Account, UUID> {

    List<Account> findByUserId(UUID user_id);

    List<Account> findByAccountType(Integer accountType);
}
