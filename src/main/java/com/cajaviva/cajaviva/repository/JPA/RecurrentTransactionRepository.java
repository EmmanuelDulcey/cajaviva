package com.cajaviva.cajaviva.repository.JPA;

import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecurrentTransactionRepository extends JpaRepository<RecurrentTransaction, UUID> {

    List<RecurrentTransaction> findByAccount(Account account);
    List<RecurrentTransaction> findByAccount_Id(UUID accountId);
    List<RecurrentTransaction> findByCategory(Category category);
    List<RecurrentTransaction> findByStatus(Integer status);
    List<RecurrentTransaction> findByFrequency(Integer frequency);
    List<RecurrentTransaction> findByCustomFrequency(Integer customFrequency);
}
