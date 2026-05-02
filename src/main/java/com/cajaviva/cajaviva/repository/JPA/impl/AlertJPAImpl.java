package com.cajaviva.cajaviva.repository.JPA.impl;

import com.cajaviva.cajaviva.dao.AlertDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository ("AlertJPAIpml")
public class AlertJPAImpl implements AlertDao {

    private final Conexion conexion;

    public AlertJPAImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Alert> findAll() {
        List<Alert> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Alerts");
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
    public Optional<Alert> findById(UUID id) {
        Alert alert = null;
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Alerts WHERE id = ?")) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                alert = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(alert);
    }

    @Override
    public Alert save(Alert entity) {
        try (Connection conn = conexion.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Alerts (id, type, message, date, status, created_at, updated_at, liquidity_projection_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setObject(1, entity.getId());
            ps.setInt(2, entity.getType());
            ps.setString(3, entity.getMessage());
            ps.setDate(4, Date.valueOf(entity.getDate()));
            ps.setInt(5, entity.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));
            ps.setTimestamp(7, Timestamp.valueOf(entity.getUpdatedAt()));
            ps.setObject(8, entity.getLiquidityProjection());

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
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Alerts WHERE id = ?")) {

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
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Alerts WHERE id = ?")) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Alert> findByStatus(Integer status) {
        List<Alert> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Alerts WHERE status = ?")) {

            ps.setInt(1, status);
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
    public List<Alert> findByType(Integer type) {
        List<Alert> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Alerts WHERE type = ?")) {

            ps.setInt(1, type);
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
    public List<Alert> findByLiquidityProjectionId(UUID liquidityProjectionId) {
        List<Alert> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Alerts WHERE liquidity_projection_id = ?")) {

            ps.setObject(1, liquidityProjectionId);
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
    private Alert mapRow(ResultSet rs) throws SQLException {
        Alert alert = new Alert();
        alert.setId(rs.getObject("id", UUID.class));
        alert.setType(rs.getInt("type"));
        alert.setMessage(rs.getString("message"));
        alert.setDate(rs.getDate("date").toLocalDate());
        alert.setStatus(rs.getInt("status"));
        alert.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        alert.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        alert.setId(rs.getObject("id", UUID.class));

        return alert;
    }

    @Override
    public List<FinancialTransaction> findByAccount(Account account) {
        throw new UnsupportedOperationException("Unimplemented method 'findByAccount'");
    }

    @Override
    public List<User> findByActive(boolean active) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByActive'");
    }
}
