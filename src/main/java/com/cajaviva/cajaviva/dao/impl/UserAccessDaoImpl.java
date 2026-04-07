package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.UserAccessDao;
import com.cajaviva.cajaviva.dao.jpa.UserAccessJpaRepository;
import com.cajaviva.cajaviva.entity.UserAccess;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserAccessDaoImpl implements UserAccessDao {

    private final UserAccessJpaRepository userAccessJpaRepository;

    public UserAccessDaoImpl(UserAccessJpaRepository userAccessJpaRepository) {
        this.userAccessJpaRepository = userAccessJpaRepository;
    }

    @Override
    public List<UserAccess> findAll() {
        return userAccessJpaRepository.findAll();
    }

    @Override
    public Optional<UserAccess> findById(UUID id) {
        return userAccessJpaRepository.findById(id);
    }

    @Override
    public UserAccess save(UserAccess entity) {
        return userAccessJpaRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return userAccessJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        userAccessJpaRepository.deleteById(id);
    }

    @Override
    public List<UserAccess> findByUserId(UUID user_id) {
        return userAccessJpaRepository.findByUserId(user_id);
    }

    @Override
    public List<UserAccess> findByAccountId(UUID account_id) {
        return userAccessJpaRepository.findByAccountId(account_id);
    }
}
