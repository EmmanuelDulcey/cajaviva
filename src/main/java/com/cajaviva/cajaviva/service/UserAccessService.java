package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.UserAccess;
import java.util.List;
import java.util.UUID;

public interface UserAccessService {
    List<UserAccess> findAll();
    UserAccess findById(UUID id);
    UserAccess create(UserAccess userAccess);
    UserAccess update(UUID id, UserAccess userAccess);
    void delete(UUID id);

    // Métodos adicionales según tu DAO/Repository
    List<UserAccess> findByPersonId(UUID personId);
    List<UserAccess> findByAccountId(UUID accountId);
    List<UserAccess> findByRole(String role);
    List<UserAccess> findByUserId(UUID user_id);
}
