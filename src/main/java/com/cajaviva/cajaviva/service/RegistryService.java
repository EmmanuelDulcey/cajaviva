package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.dto.RegisterUserRequest;
import com.cajaviva.cajaviva.entity.User;

public interface RegistryService {
    User registerUser(RegisterUserRequest request);
}
