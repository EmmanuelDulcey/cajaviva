package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dto.RegisterUserRequest;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.service.RegistryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegistryController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistryService registryService;

    @MockBean
    private JwtService jwtService;

    @Test
    void registerUserReturns201() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Ana");
        user.setLastName("Lopez");
        user.setEmail("ana@test.com");
        when(registryService.registerUser(any(RegisterUserRequest.class))).thenReturn(user);

        mockMvc.perform(post("/registries/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Ana",
                                  "lastName": "Lopez",
                                  "email": "ana@test.com",
                                  "password": "Secret123*"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.email").value("ana@test.com"));
    }
}
