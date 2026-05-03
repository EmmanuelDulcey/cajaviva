package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.FinancialTransactionDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("FinancialTransactionDaoJDBC")
public class FinancialTransactionDaoImpl implements FinancialTransactionDao {

    private final Conexion conexion;

    public FinancialTransactionDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<FinancialTransaction> findAll() {
        return query("SELECT * FROM financial_transactions", ps -> {});
    }

    @Override
    public Optional<FinancialTransaction> findById(UUID id) {
        List<FinancialTransaction> result = query("SELECT * FROM financial_transactions WHERE id = ?", ps -> ps.setObject(1, id));
        return result.stream().findFirst();
    }

    @Override
    public FinancialTransaction save(FinancialTransaction entity) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO financial_transactions (id, value, description, date, status, created_at, updated_at, account_id, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            if (entity.getId() == null) {
                entity.setId(UUID.randomUUID());
            }
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
                ps.setNull(9, Types.OTHER);
            }

            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM financial_transactions WHERE id = ?")) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM financial_transactions WHERE id = ?")) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FinancialTransaction> findByAccount(Account account) {
        return findByAccountId(account.getId());
    }

    @Override
    public List<FinancialTransaction> findByCategory(Category category) {
        return findByCategoryId(category.getId());
    }

    @Override
    public List<FinancialTransaction> findByStatus(Integer status) {
        return query("SELECT * FROM financial_transactions WHERE status = ?", ps -> ps.setInt(1, status));
    }

    @Override
    public List<FinancialTransaction> findByAccountAndCategory(Account account, Category category) {
        return query("SELECT * FROM financial_transactions WHERE account_id = ? AND category_id = ?", ps -> {
            ps.setObject(1, account.getId());
            ps.setObject(2, category.getId());
        });
    }

    @Override
    public List<FinancialTransaction> findByAccountId(UUID accountId) {
        return query("SELECT * FROM financial_transactions WHERE account_id = ?", ps -> ps.setObject(1, accountId));
    }

    @Override
    public List<FinancialTransaction> findByCategoryId(UUID categoryId) {
        return query("SELECT * FROM financial_transactions WHERE category_id = ?", ps -> ps.setObject(1, categoryId));
    }

    @Override
    public List<User> findByActive(boolean active) {
        return List.of();
    }

    private List<FinancialTransaction> query(String sql, SqlBinder binder) {
        List<FinancialTransaction> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

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

    @FunctionalInterface
    private interface SqlBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }
}
