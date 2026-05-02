package com.cajaviva.cajaviva.repository.JPA;

import com.cajaviva.cajaviva.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, UUID> {
    List<UserAccess> findByPersonId(UUID personId);
    List<UserAccess> findByAccountId(UUID accountId);
    List<UserAccess> findByRole(String role);
}
