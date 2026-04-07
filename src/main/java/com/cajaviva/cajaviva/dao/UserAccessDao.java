package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.UserAccess;

import java.util.List;
import java.util.UUID;

public interface UserAccessDao extends BaseDao<UserAccess, UUID> {

    List<UserAccess> findByUserId(UUID user_id);

    List<UserAccess> findByAccountId(UUID account_id);
}
