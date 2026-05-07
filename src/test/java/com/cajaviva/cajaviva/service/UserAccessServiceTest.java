package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.repository.JPA.UserAccessRepository;
import com.cajaviva.cajaviva.service.impl.UserAccessServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAccessServiceTest {

    private UserAccessRepository repository;
    private UserAccessServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(UserAccessRepository.class);
        service = new UserAccessServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        List<UserAccess> accesses = Arrays.asList(new UserAccess(), new UserAccess());
        when(repository.findAll()).thenReturn(accesses);

        List<UserAccess> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        UserAccess access = new UserAccess();
        access.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(access));

        UserAccess result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFoundReturnsNull() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        UserAccess result = service.findById(id);

        assertNull(result);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testCreate() {
        UserAccess access = new UserAccess();
        when(repository.save(any(UserAccess.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAccess result = service.create(access);

        assertNotNull(result);
        verify(repository, times(1)).save(access);
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        UserAccess existing = new UserAccess();
        existing.setId(id);
        UserAccess access = new UserAccess();
        access.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(UserAccess.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAccess result = service.update(id, access);

        assertEquals(id, result.getId());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(access);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        List<UserAccess> accesses = Arrays.asList(new UserAccess(), new UserAccess());
        when(repository.findByAccountId(accountId)).thenReturn(accesses);

        List<UserAccess> result = service.findByAccountId(accountId);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByAccountId(accountId);
    }

    @Test
    void testFindByRole() {
        Integer role = 1;
        List<UserAccess> accesses = Arrays.asList(new UserAccess(), new UserAccess());
        when(repository.findByRole(role)).thenReturn(accesses);

        List<UserAccess> result = service.findByRole(role);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByRole(role);
    }

    @Test
    void testFindByUserId() {
        UUID userId = UUID.randomUUID();
        List<UserAccess> accesses = Arrays.asList(new UserAccess(), new UserAccess());
        when(repository.findByUserId(userId)).thenReturn(accesses);

        List<UserAccess> result = service.findByUserId(userId);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByUserId(userId);
    }
}
