package com.cajaviva.cajaviva.repository.JPA.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.repository.JPA.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("UserJPAImpl")
public class UserJPAimpl implements UserDao {

    private final UserRepository userRepository;

    public UserJPAimpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByActive(boolean active) {
        return userRepository.findByActive(active);
    }
}
