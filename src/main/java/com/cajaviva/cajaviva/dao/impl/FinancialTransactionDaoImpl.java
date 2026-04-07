package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.FinancialTransactionDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FinancialTransactionDaoImpl implements FinancialTransactionDao {

    private final Conexion conexion;

    public FinancialTransactionDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<FinancialTransaction> findAll() {
        List<FinancialTransaction> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM FinancialTransactions");
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
    public Optional<FinancialTransaction> findById(UUID id) {
        FinancialTransaction ft = null;
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM FinancialTransactions WHERE id = ?")) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ft = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(ft);
    }

    @Override
    public FinancialTransaction save(FinancialTransaction entity) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO FinancialTransactions (id, value, description, date, status, created_at, updated_at, account_id, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setObject(1, entity.getId());
            ps.setBigDecimal(2, entity.getValue());
            ps.setString(3, entity.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            ps.setInt(5, entity.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));
            ps.setTimestamp(7, Timestamp.valueOf(entity.getUpdatedAt()));
            ps.setObject(8, entity.getAccount().getId());

            if (entity.getCategory() != null) {
                ps.setObject(9, entity.getCategory().getId());
            } else {
                ps.setNull(9, Types.NULL);
            }

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
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM FinancialTransactions WHERE id = ?")) {

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
             PreparedStatement ps = conn.prepareStatement("DELETE FROM FinancialTransactions WHERE id = ?")) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FinancialTransaction> findByAccountId(UUID accountId) {
        List<FinancialTransaction> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM FinancialTransactions WHERE account_id = ?")) {

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

    @Override
    public List<FinancialTransaction> findByCategoryId(UUID categoryId) {
        List<FinancialTransaction> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM FinancialTransactions WHERE category_id = ?")) {

            ps.setObject(1, categoryId);
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
    private FinancialTransaction mapRow(ResultSet rs) throws SQLException {
        FinancialTransaction ft = new FinancialTransaction();
        ft.setId(rs.getObject("id", UUID.class));
        ft.setValue(rs.getBigDecimal("value"));
        ft.setDescription(rs.getString("description"));
        ft.setDate(rs.getTimestamp("date").toLocalDateTime());
        ft.setStatus(rs.getInt("status"));
        ft.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        ft.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        Account account = new Account();
        account.setId(rs.getObject("account_id", UUID.class));
        ft.setAccount(account);

        UUID categoryId = rs.getObject("category_id", UUID.class);
        if (categoryId != null) {
            Category category = new Category();
            category.setId(categoryId);
            ft.setCategory(category);
        }

        return ft;
    }
}
