package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.UserAccessDao;
import com.cajaviva.cajaviva.entity.UserAccess;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-accesses")
public class UserAccessController {

    private final UserAccessDao userAccessDao;

    public UserAccessController(UserAccessDao userAccessDao) {
        this.userAccessDao = userAccessDao;
    }

    @GetMapping
    public List<UserAccess> getAllUserAccesses() {
        return userAccessDao.findAll();
    }

    @GetMapping("/user/{user_id}")
    public List<UserAccess> getByUser(@PathVariable("user_id") UUID user_id) {
        return userAccessDao.findByUserId(user_id);
    }

    @GetMapping("/account/{account_id}")
    public List<UserAccess> getByAccount(@PathVariable("account_id") UUID account_id) {
        return userAccessDao.findByAccountId(account_id);
    }

    @PostMapping
    public UserAccess createUserAccess(@RequestBody UserAccess userAccess) {
        return userAccessDao.save(userAccess);
    }
}
