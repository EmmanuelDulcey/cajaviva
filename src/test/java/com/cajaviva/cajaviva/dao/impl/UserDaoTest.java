package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDaoTest {

    private Connection connection;
    private PreparedStatement stmt;
    private ResultSet rs;
    private UserDaoImpl userDao;

    // StubConexion que devuelve el Connection mock
    static class StubConexion extends Conexion {
        private final Connection connection;

        public StubConexion(Connection connection) {
            super(mock(DataSource.class));
            this.connection = connection;
        }

        @Override
        public Connection obtenerConexion() {
            return connection;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        // Ahora sí podemos usar mocks normales gracias a mock-maker-inline
        connection = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        StubConexion stubConexion = new StubConexion(connection);
        userDao = new UserDaoImpl(stubConexion);
    }

    @Test
    void testFindAllReturnsUsers() throws Exception {
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString("id")).thenReturn(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        when(rs.getString("name")).thenReturn("Alice", "Bob");
        when(rs.getString("last_name")).thenReturn("A", "B");
        when(rs.getString("email")).thenReturn("alice@test.com", "bob@test.com");

        List<User> result = userDao.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
        verify(stmt, times(1)).executeQuery();
    }

    @Test
    void testFindByIdFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(rs.next()).thenReturn(true);
        when(rs.getString("id")).thenReturn(id.toString());
        when(rs.getString("name")).thenReturn("Charlie");
        when(rs.getString("last_name")).thenReturn("C");
        when(rs.getString("email")).thenReturn("charlie@test.com");

        User result = userDao.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Charlie", result.getName());
    }

    @Test
    void testFindByIdNotFoundReturnsNull() throws Exception {
        UUID id = UUID.randomUUID();
        when(rs.next()).thenReturn(false);

        User result = userDao.findById(id);

        assertNull(result);
    }

    @Test
    void testCreateGeneratesIdIfNull() throws Exception {
        User user = new User();
        user.setName("David");
        user.setLastName("Miller");
        user.setEmail("david@test.com");
        user.setPasswordDigest("hash");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User result = userDao.create(user);

        assertNotNull(result.getId());
        assertEquals("David", result.getName());
        verify(stmt, times(1)).executeUpdate();
    }

    @Test
    void testUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setName("Eva");
        user.setLastName("Stone");
        user.setEmail("eva@test.com");
        user.setPasswordDigest("hash2");
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());

        User result = userDao.update(id, user);

        assertEquals(id, result.getId());
        assertEquals("Eva", result.getName());
        verify(stmt, times(1)).executeUpdate();
    }

    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        userDao.delete(id);

        verify(stmt, times(1)).executeUpdate();
    }
}
