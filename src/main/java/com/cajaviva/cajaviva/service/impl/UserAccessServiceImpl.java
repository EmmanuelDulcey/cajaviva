package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.dao.UserAccessDao;
import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.service.UserAccessService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserAccessServiceImpl implements UserAccessService {

    private final UserAccessDao userAccessDao;

    public UserAccessServiceImpl(@Qualifier("UserAccessJPAIpml") UserAccessDao userAccessDao) {
        this.userAccessDao = userAccessDao;
    }

    @Override
    public List<UserAccess> findAll() {
        return userAccessDao.findAll();
    }

    @Override
    public UserAccess findById(UUID id) {
        return userAccessDao.findById(id).orElse(null);
    }

    @Override
    public UserAccess create(UserAccess userAccess) {
        return userAccessDao.save(userAccess);
    }

    @Override
    public UserAccess update(UUID id, UserAccess userAccess) {
        userAccess.setId(id);
        return userAccessDao.save(userAccess);
    }

    @Override
    public void delete(UUID id) {
        userAccessDao.deleteById(id);
    }

    @Override
    public List<UserAccess> findByPersonId(UUID personId) {
        return userAccessDao.findByPersonId(personId);
    }

    @Override
    public List<UserAccess> findByAccountId(UUID accountId) {
        return userAccessDao.findByAccountId(accountId);
    }

    @Override
    public List<UserAccess> findByRole(String role) {
        return userAccessDao.findByRole(role);
    }

    @Override
    public List<UserAccess> findByUserId(UUID user_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUserId'");
    }
}
