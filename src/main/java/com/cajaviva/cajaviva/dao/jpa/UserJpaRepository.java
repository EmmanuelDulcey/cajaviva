package com.cajaviva.cajaviva.dao.jpa;

import com.cajaviva.cajaviva.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
