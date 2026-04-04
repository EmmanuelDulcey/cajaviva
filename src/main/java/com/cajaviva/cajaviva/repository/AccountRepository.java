package com.cajaviva.cajaviva.repository;

import com.cajaviva.cajaviva.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
}