package com.cajaviva.cajaviva.repository.JPA;

import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, UUID> {
    List<UserAccess> findByUser(User user);
    List<UserAccess> findByUser_Id(UUID userId);
    List<UserAccess> findByAccount_Id(UUID accountId);
    List<UserAccess> findByRole(Integer role);
}
