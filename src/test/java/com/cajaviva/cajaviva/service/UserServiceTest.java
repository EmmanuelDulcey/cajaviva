package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDao userDao;
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        service = new UserServiceImpl(userDao);
    }

    @Test
    void testFindAll() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userDao.findAll()).thenReturn(users);

        List<User> result = service.findAll();

        assertEquals(2, result.size());
        verify(userDao, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        when(userDao.findById(id)).thenReturn(user);

        User result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(userDao, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        when(userDao.findById(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
        verify(userDao, times(1)).findById(id);
    }

    @Test
    void testCreateGeneratesIdIfNull() {
        User user = new User();
        when(userDao.create(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.create(user);

        assertNotNull(result.getId());
        verify(userDao, times(1)).create(user);
    }

    @Test
    void testCreateWithExistingId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        when(userDao.create(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.create(user);

        assertEquals(user.getId(), result.getId());
        verify(userDao, times(1)).create(user);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        when(userDao.update(eq(id), any(User.class))).thenReturn(user);

        User result = service.update(id, user);

        assertEquals(id, result.getId());
        verify(userDao, times(1)).update(id, user);
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        User user = new User();

        when(userDao.update(eq(id), any(User.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.update(id, user));
        verify(userDao, times(1)).update(id, user);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(userDao, times(1)).delete(id);
    }
}
