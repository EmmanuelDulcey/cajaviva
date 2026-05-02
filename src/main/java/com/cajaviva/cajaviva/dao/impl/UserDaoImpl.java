package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.exception.ConflictException;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository ("UserDaoJDBC")
public class UserDaoImpl implements UserDao {

    private final Conexion conexion;

    public UserDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM Users";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar los usuarios.", e);
        }

        return list;
    }

    @Override
    public Optional<User> findById(UUID id) {

        String sql = "SELECT * FROM Users WHERE id = ?";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el usuario.", e);
        }

        return Optional.empty();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        } else {
            return update(user);
        }
    }

    @Override
    public boolean existsById(UUID id) {

        String sql = "SELECT COUNT(*) FROM Users WHERE id = ?";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar si el usuario existe.", e);
        }

        return false;
    }

    @Override
    public void deleteById(UUID id) {

        String sql = "DELETE FROM Users WHERE id = ?";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el usuario.", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {

        String sql = "SELECT * FROM Users WHERE email = ?";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el usuario por email.", e);
        }

        return Optional.empty();
    }

    private User insert(User user) {

        user.setId(UUID.randomUUID());

        String sql = "INSERT INTO Users (id, name, last_name, email, active, password_digest, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.getActive());
            stmt.setString(6, user.getPasswordDigest());
            stmt.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.valueOf(user.getUpdatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            if (isDuplicateEmail(e)) {
                throw new ConflictException("Este email ya se encuentra registrado.");
            }
            throw new RuntimeException("Error al crear el usuario.", e);
        }

        return user;
    }

    private User update(User user) {

        String sql = "UPDATE Users SET name=?, last_name=?, email=?, active=?, password_digest=?, updated_at=? WHERE id=?";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setBoolean(4, user.getActive());
            stmt.setString(5, user.getPasswordDigest());
            stmt.setTimestamp(6, Timestamp.valueOf(user.getUpdatedAt()));
            stmt.setObject(7, user.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            if (isDuplicateEmail(e)) {
                throw new ConflictException("Este email ya se encuentra registrado.");
            }
            throw new RuntimeException("Error al actualizar el usuario.", e);
        }

        return user;
    }

    private User map(ResultSet rs) throws SQLException {

        User u = new User(null);

        u.setId(rs.getObject("id", UUID.class));
        u.setName(rs.getString("name"));
        u.setLastName(rs.getString("last_name"));
        u.setEmail(rs.getString("email"));
        u.setActive(rs.getBoolean("active"));
        u.setPasswordDigest(rs.getString("password_digest"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            u.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            u.setUpdatedAt(updated.toLocalDateTime());
        }

        return u;
    }

    private boolean isDuplicateEmail(SQLException exception) {
        return exception.getErrorCode() == 2627 || exception.getErrorCode() == 2601;
    }

    @Override
    public List<User> findByActive(boolean active) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByActive'");
    }
}
