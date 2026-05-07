package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {

    private final Conexion conexion;

    public UserDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, last_name, email, active, password_digest, created_at, updated_at FROM users";

        try (
                Connection conn = conexion.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setActive(rs.getBoolean("active"));
                user.setPasswordDigest(rs.getString("password_digest"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    user.setCreatedAt(createdAt.toLocalDateTime());
                }
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    user.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                users.add(user);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public User findById(UUID id) {

        String sql = "SELECT id, name, last_name, email, active, password_digest, created_at, updated_at FROM users WHERE id = ?";

        try (
                Connection conn = conexion.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    user.setId(UUID.fromString(rs.getString("id")));
                    user.setName(rs.getString("name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setActive(rs.getBoolean("active"));
                    user.setPasswordDigest(rs.getString("password_digest"));
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    if (createdAt != null) {
                        user.setCreatedAt(createdAt.toLocalDateTime());
                    }
                    Timestamp updatedAt = rs.getTimestamp("updated_at");
                    if (updatedAt != null) {
                        user.setUpdatedAt(updatedAt.toLocalDateTime());
                    }
                    return user;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public User create(User user) {

        String sql = "INSERT INTO users (id, name, last_name, email, active, password_digest, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        try (
                Connection conn = conexion.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, user.getId().toString());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.isActive());
            stmt.setString(6, user.getPasswordDigest());
            stmt.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.valueOf(user.getUpdatedAt()));

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public User update(UUID id, User user) {

        String sql = "UPDATE users SET name = ?, last_name = ?, email = ?, active = ?, password_digest = ?, updated_at = ? WHERE id = ?";

        try (
                Connection conn = conexion.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setBoolean(4, user.isActive());
            stmt.setString(5, user.getPasswordDigest());
            stmt.setTimestamp(6, Timestamp.valueOf(user.getUpdatedAt()));
            stmt.setString(7, id.toString());

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        user.setId(id);
        return user;
    }

    @Override
    public void delete(UUID id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (
                Connection conn = conexion.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
