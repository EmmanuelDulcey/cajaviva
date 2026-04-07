package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.UserAccessDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserAccessDaoImpl implements UserAccessDao {

    private final Conexion conexion;

    public UserAccessDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<UserAccess> findAll() {
        List<UserAccess> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM UserAccesses");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<UserAccess> findById(UUID id) {
        UserAccess ua = null;
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM UserAccesses WHERE id = ?")) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ua = mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(ua);
    }

    @Override
    public UserAccess save(UserAccess entity) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO UserAccesses (id, role, created_at, account_id, user_id) VALUES (?, ?, ?, ?, ?)")) {

            ps.setObject(1, entity.getId());
            ps.setInt(2, entity.getRole());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getCreatedAt()));
            ps.setObject(4, entity.getAccount().getId()); // usamos el id del objeto Account
            ps.setObject(5, entity.getUser().getId());    // usamos el id del objeto User

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            entity = null;
        }
        return entity;
    }

    @Override
    public boolean existsById(UUID id) {
        boolean exists = false;
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM UserAccesses WHERE id = ?")) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    @Override
    public void deleteById(UUID id) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM UserAccesses WHERE id = ?")) {

            ps.setObject(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<UserAccess> findByUserId(UUID userId) {
        List<UserAccess> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM UserAccesses WHERE user_id = ?")) {

            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<UserAccess> findByAccountId(UUID accountId) {
        List<UserAccess> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM UserAccesses WHERE account_id = ?")) {

            ps.setObject(1, accountId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Método auxiliar para mapear ResultSet a entidad
    private UserAccess mapRow(ResultSet rs) throws SQLException {
        UserAccess ua = new UserAccess();
        ua.setId(rs.getObject("id", UUID.class));
        ua.setRole(rs.getInt("role"));
        ua.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Creamos objetos "ligeros" de Account y User solo con el id
        Account account = new Account();
        account.setId(rs.getObject("account_id", UUID.class));
        ua.setAccount(account);

        User user = new User();
        user.setId(rs.getObject("user_id", UUID.class));
        ua.setUser(user);

        return ua;
    }
}
