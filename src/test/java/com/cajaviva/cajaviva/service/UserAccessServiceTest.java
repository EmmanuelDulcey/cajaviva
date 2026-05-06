package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.repository.JPA.UserAccessRepository;
import com.cajaviva.cajaviva.service.impl.UserAccessServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAccessServiceTest {

    private UserAccessRepository userAccessRepository;
    private UserAccessService userAccessService;

    @BeforeEach
    void setUp() {
        userAccessRepository = Mockito.mock(UserAccessRepository.class);
        userAccessService = new UserAccessServiceImpl(userAccessRepository);
    }

    @Test
    void testCreateUserAccess() {
        UserAccess ua = new UserAccess();
        ua.setRole("ADMIN");

        when(userAccessRepository.save(ua)).thenReturn(ua);

        UserAccess created = userAccessService.create(ua);

        assertNotNull(created);
        assertEquals("ADMIN", created.getRole());
        verify(userAccessRepository, times(1)).save(ua);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(id);

        when(userAccessRepository.findById(id)).thenReturn(Optional.of(ua));

        UserAccess found = userAccessService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(userAccessRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll() {
        UserAccess ua = new UserAccess();
        ua.setRole("USER");

        when(userAccessRepository.findAll()).thenReturn(List.of(ua));

        List<UserAccess> accesses = userAccessService.findAll();

        assertEquals(1, accesses.size());
        assertEquals("USER", accesses.get(0).getRole());
        verify(userAccessRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUserAccess() {
        UUID id = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(id);
        ua.setRole("VIEWER");

        when(userAccessRepository.save(any(UserAccess.class))).thenReturn(ua);

        UserAccess updated = userAccessService.update(id, ua);

        assertNotNull(updated);
        assertEquals("VIEWER", updated.getRole());
        verify(userAccessRepository, times(1)).save(any(UserAccess.class));
    }

    @Test
    void testDeleteUserAccess() {
        UUID id = UUID.randomUUID();

        doNothing().when(userAccessRepository).deleteById(id);

        userAccessService.delete(id);

        verify(userAccessRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByPersonId() {
        UUID personId = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(UUID.randomUUID());

        when(userAccessRepository.findByPersonId(personId)).thenReturn(List.of(ua));

        List<UserAccess> accesses = userAccessService.findByPersonId(personId);

        assertEquals(1, accesses.size());
        verify(userAccessRepository, times(1)).findByPersonId(personId);
    }

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(UUID.randomUUID());

        when(userAccessRepository.findByAccountId(accountId)).thenReturn(List.of(ua));

        List<UserAccess> accesses = userAccessService.findByAccountId(accountId);

        assertEquals(1, accesses.size());
        verify(userAccessRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    void testFindByRole() {
        UserAccess ua = new UserAccess();
        ua.setRole("MANAGER");

        when(userAccessRepository.findByRole("MANAGER")).thenReturn(List.of(ua));

        List<UserAccess> accesses = userAccessService.findByRole("MANAGER");

        assertEquals(1, accesses.size());
        assertEquals("MANAGER", accesses.get(0).getRole());
        verify(userAccessRepository, times(1)).findByRole("MANAGER");
    }

    @Test
    void testFindByUserId() {
        UUID userId = UUID.randomUUID();
        UserAccess ua = new UserAccess();
        ua.setId(UUID.randomUUID());

        when(userAccessRepository.findByUserId(userId)).thenReturn(List.of(ua));

        List<UserAccess> accesses = userAccessService.findByUserId(userId);

        assertEquals(1, accesses.size());
        verify(userAccessRepository, times(1)).findByUserId(userId);
    }
}
