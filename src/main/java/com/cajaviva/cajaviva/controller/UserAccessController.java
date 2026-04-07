package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.UserAccessRepository;
import com.cajaviva.cajaviva.entity.UserAccess;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-accesses")
public class UserAccessController {

    private final UserAccessRepository userAccessRepository;

    public UserAccessController(UserAccessRepository userAccessRepository) {
        this.userAccessRepository = userAccessRepository;
    }

    @GetMapping
    public List<UserAccess> getAllUserAccesses() {
        return userAccessRepository.findAll();
    }

    @GetMapping("/person/{personId}")
    public List<UserAccess> getByPerson(@PathVariable UUID personId) {
        return userAccessRepository.findByPersonId(personId);
    }

    @GetMapping("/account/{accountId}")
    public List<UserAccess> getByAccount(@PathVariable UUID accountId) {
        return userAccessRepository.findByAccountId(accountId);
    }

    @PostMapping
    public UserAccess createUserAccess(@RequestBody UserAccess userAccess) {
        return userAccessRepository.save(userAccess);
    }
}
