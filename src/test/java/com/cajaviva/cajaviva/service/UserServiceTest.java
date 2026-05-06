package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.service.impl.UserServiceImpl;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("Esteban");

        when(userDao.create(any(User.class))).thenReturn(user);

        User created = userService.create(user);

        assertNotNull(created);
        assertEquals("Esteban", created.getName());
        verify(userDao, times(1)).create(any(User.class));
    }

    @Test
    void testFindByIdSuccess() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        when(userDao.findById(id)).thenReturn(user);

        User found = userService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(userDao, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(userDao.findById(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(id));
        verify(userDao, times(1)).findById(id);
    }

    @Test
    void testFindAll() {
        User user = new User();
        user.setName("Usuario prueba");

        when(userDao.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals("Usuario prueba", users.get(0).getName());
        verify(userDao, times(1)).findAll();
    }

    @Test
    void testUpdateUserSuccess() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setName("Original");

        when(userDao.update(id, user)).thenReturn(user);

        User updated = userService.update(id, user);

        assertNotNull(updated);
        assertEquals("Original", updated.getName());
        verify(userDao, times(1)).update(id, user);
    }

    @Test
    void testUpdateUserNotFound() {
        UUID id = UUID.randomUUID();
        User user = new User();

        when(userDao.update(id, user)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> userService.update(id, user));
        verify(userDao, times(1)).update(id, user);
    }

    @Test
    void testDeleteUser() {
        UUID id = UUID.randomUUID();

        doNothing().when(userDao).delete(id);

        userService.delete(id);

        verify(userDao, times(1)).delete(id);
    }
}
