package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.UserAccessService;
import com.cajaviva.cajaviva.entity.UserAccess;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-accesses")
public class UserAccessController {

    private final UserAccessService userAccessService;

    public UserAccessController(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    @GetMapping
    public List<UserAccess> getAllUserAccesses() {
        return userAccessService.findAll();
    }

    @GetMapping("/user/{user_id}")
    public List<UserAccess> getByUser(@PathVariable("user_id") UUID user_id) {
        return userAccessService.findByUserId(user_id);
    }

    @GetMapping("/account/{account_id}")
    public List<UserAccess> getByAccount(@PathVariable("account_id") UUID account_id) {
        return userAccessService.findByAccountId(account_id);
    }

    @PostMapping
    public UserAccess createUserAccess(@RequestBody UserAccess userAccess) {
        return userAccessService.create(userAccess);
    }
}
