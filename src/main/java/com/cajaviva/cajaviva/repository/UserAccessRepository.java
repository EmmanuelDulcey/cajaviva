package com.cajaviva.cajaviva.repository;

import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserAccessRepository extends JpaRepository<UserAccess, UUID> {
    List<UserAccess> findByUser(User user);
    List<UserAccess> findByRole(String role);
}
