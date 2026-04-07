package com.cajaviva.cajaviva.dao.jpa;

import com.cajaviva.cajaviva.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserAccessJpaRepository extends JpaRepository<UserAccess, UUID> {

    List<UserAccess> findByUserId(UUID userId);

    List<UserAccess> findByAccountId(UUID accountId);
}
