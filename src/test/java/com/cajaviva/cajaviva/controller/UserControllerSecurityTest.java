package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.service.UserService;
import com.cajaviva.cajaviva.support.WithAuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    private final UUID currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID otherUserId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void unauthenticatedEndpointsReturn401() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/users")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/users/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/users/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/users/" + id).with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    void getAll_ReturnsOwnProfile() throws Exception {
        User user = new User();
        user.setId(currentUserId);
        when(userService.findById(currentUserId)).thenReturn(user);

        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getById_OwnUser_Returns200() throws Exception {
        User user = new User();
        user.setId(currentUserId);
        when(userService.findById(currentUserId)).thenReturn(user);

        mockMvc.perform(get("/users/" + currentUserId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getById_OtherUser_Returns403() throws Exception {
        mockMvc.perform(get("/users/" + otherUserId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = currentUserId;
        User user = new User();
        user.setId(id);
        when(userService.findById(id)).thenReturn(user);
        when(userService.create(any(User.class))).thenReturn(user);
        when(userService.update(any(UUID.class), any(User.class))).thenReturn(user);

        mockMvc.perform(get("/users")).andExpect(status().isOk());
        mockMvc.perform(get("/users/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"A\",\"email\":\"a@a.com\"}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(put("/users/" + id).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"A\",\"email\":\"a@a.com\"}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/users/" + id).with(csrf())).andExpect(status().isOk());
    }
}
