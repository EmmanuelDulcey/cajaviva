package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.service.UserAccessService;
import com.cajaviva.cajaviva.support.WithAuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAccessController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class UserAccessControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean UserAccessService userAccessService;
    @MockBean JwtService jwtService;

    private final UUID currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID otherUserId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void unauthenticatedEndpointsReturn401() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/api/user-accesses")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/user-accesses/user/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/user-accesses/account/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/user-accesses/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/user-accesses").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/user-accesses/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/user-accesses/" + id).with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    void getAllUserAccesses_ReturnsOnlyCurrentUserAccesses() throws Exception {
        when(userAccessService.findByUserId(currentUserId)).thenReturn(List.of());
        mockMvc.perform(get("/api/user-accesses")).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getById_OwnAccess_Returns200() throws Exception {
        UUID accessId = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(accessId);
        ua.setUserId(currentUserId);
        when(userAccessService.findById(accessId)).thenReturn(ua);

        mockMvc.perform(get("/api/user-accesses/" + accessId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getById_OtherUsersAccess_Returns403() throws Exception {
        UUID accessId = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(accessId);
        ua.setUserId(otherUserId);
        when(userAccessService.findById(accessId)).thenReturn(ua);

        mockMvc.perform(get("/api/user-accesses/" + accessId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void getByUser_OwnUser_Returns200() throws Exception {
        when(userAccessService.findByUserId(currentUserId)).thenReturn(List.of());
        mockMvc.perform(get("/api/user-accesses/user/" + currentUserId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getByUser_OtherUser_Returns403() throws Exception {
        mockMvc.perform(get("/api/user-accesses/user/" + otherUserId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(id);
        ua.setUserId(currentUserId);
        when(userAccessService.findByUserId(currentUserId)).thenReturn(List.of(ua));
        when(userAccessService.findByAccountId(id)).thenReturn(List.of(ua));
        when(userAccessService.findById(id)).thenReturn(ua);
        when(userAccessService.create(any(UserAccess.class))).thenReturn(ua);
        when(userAccessService.update(any(UUID.class), any(UserAccess.class))).thenReturn(ua);

        mockMvc.perform(get("/api/user-accesses")).andExpect(status().isOk());
        mockMvc.perform(get("/api/user-accesses/user/" + currentUserId)).andExpect(status().isOk());
        mockMvc.perform(get("/api/user-accesses/account/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/api/user-accesses/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/api/user-accesses").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(put("/api/user-accesses/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/api/user-accesses/" + id).with(csrf())).andExpect(status().isOk());
    }
}
