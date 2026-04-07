package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userDao.findById(id).orElse(null);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userDao.save(user);
    }
}
