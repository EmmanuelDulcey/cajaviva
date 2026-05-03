package com.cajaviva.cajaviva.repository.JPA.impl;

import com.cajaviva.cajaviva.dao.UserAccessDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.repository.JPA.UserAccessRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("UserAccessJPAIpml")
public class UserAccessJPAImpl implements UserAccessDao {

    private final UserAccessRepository userAccessRepository;

    public UserAccessJPAImpl(UserAccessRepository userAccessRepository) {
        this.userAccessRepository = userAccessRepository;
    }

    @Override
    public List<UserAccess> findAll() {
        return userAccessRepository.findAll();
    }

    @Override
    public Optional<UserAccess> findById(UUID id) {
        return userAccessRepository.findById(id);
    }

    @Override
    public UserAccess save(UserAccess entity) {
        return userAccessRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return userAccessRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        userAccessRepository.deleteById(id);
    }

    @Override
    public List<UserAccess> findByPersonId(UUID personId) {
        return userAccessRepository.findByUser_Id(personId);
    }

    @Override
    public List<UserAccess> findByAccountId(UUID accountId) {
        return userAccessRepository.findByAccount_Id(accountId);
    }

    @Override
    public List<UserAccess> findByRole(String role) {
        return userAccessRepository.findByRole(Integer.valueOf(role));
    }

    @Override
    public List<UserAccess> findByUserId(UUID user_id) {
        return userAccessRepository.findByUser_Id(user_id);
    }

    @Override
    public List<User> findByActive(boolean active) {
        return List.of();
    }
}
