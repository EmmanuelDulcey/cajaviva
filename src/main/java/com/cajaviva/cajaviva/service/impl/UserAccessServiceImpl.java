package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.repository.JPA.UserAccessRepository;
import com.cajaviva.cajaviva.service.UserAccessService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserAccessServiceImpl implements UserAccessService {

    private final UserAccessRepository repository;

    public UserAccessServiceImpl(UserAccessRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserAccess> findAll() {
        return repository.findAll();
    }

    @Override
    public UserAccess findById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public UserAccess create(UserAccess userAccess) {
        userAccess.setCreatedAt(LocalDateTime.now());
        return repository.save(userAccess);
    }

    @Override
    public UserAccess update(UUID id, UserAccess userAccess) {
        UserAccess existing = findById(id);
        if (existing != null && existing.getCreatedAt() != null) {
            userAccess.setCreatedAt(existing.getCreatedAt());
        } else if (userAccess.getCreatedAt() == null) {
            userAccess.setCreatedAt(LocalDateTime.now());
        }
        userAccess.setId(id);
        return repository.save(userAccess);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserAccess> findByAccountId(UUID accountId) {
        return repository.findByAccountId(accountId);
    }

    @Override
    public List<UserAccess> findByRole(Integer role) {
        return repository.findByRole(role);
    }

    @Override
    public List<UserAccess> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }
}
