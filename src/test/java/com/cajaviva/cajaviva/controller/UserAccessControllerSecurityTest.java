package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.service.UserAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
    @WithMockUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(id);
        when(userAccessService.findAll()).thenReturn(List.of(ua));
        when(userAccessService.findByUserId(id)).thenReturn(List.of(ua));
        when(userAccessService.findByAccountId(id)).thenReturn(List.of(ua));
        when(userAccessService.findById(id)).thenReturn(ua);
        when(userAccessService.create(any(UserAccess.class))).thenReturn(ua);
        when(userAccessService.update(any(UUID.class), any(UserAccess.class))).thenReturn(ua);

        mockMvc.perform(get("/api/user-accesses")).andExpect(status().isOk());
        mockMvc.perform(get("/api/user-accesses/user/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/api/user-accesses/account/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/api/user-accesses/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/api/user-accesses").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(put("/api/user-accesses/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/api/user-accesses/" + id).with(csrf())).andExpect(status().isOk());
    }
}
