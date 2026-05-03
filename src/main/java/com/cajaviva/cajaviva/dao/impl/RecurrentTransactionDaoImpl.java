package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.RecurrentTransactionDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("RecurrentTransactionDaoJDBC")
public class RecurrentTransactionDaoImpl implements RecurrentTransactionDao {

    private final Conexion conexion;

    public RecurrentTransactionDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<RecurrentTransaction> findAll() {
        return query("SELECT * FROM recurrent_transactions", ps -> {});
    }

    @Override
    public Optional<RecurrentTransaction> findById(UUID id) {
        List<RecurrentTransaction> result = query("SELECT * FROM recurrent_transactions WHERE id = ?", ps -> ps.setObject(1, id));
        return result.stream().findFirst();
    }

    @Override
    public RecurrentTransaction save(RecurrentTransaction rt) {
        return rt.getId() == null ? insert(rt) : update(rt);
    }

    @Override
    public boolean existsById(UUID id) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM recurrent_transactions WHERE id=?")) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM recurrent_transactions WHERE id=?")) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RecurrentTransaction> findByAccountId(UUID accountId) {
        return query("SELECT * FROM recurrent_transactions WHERE account_id=?", ps -> ps.setObject(1, accountId));
    }

    @Override
    public List<RecurrentTransaction> findByCategoryId(UUID categoryId) {
        return query("SELECT * FROM recurrent_transactions WHERE category_id=?", ps -> ps.setObject(1, categoryId));
    }

    @Override
    public List<RecurrentTransaction> findByAccount(UUID accountId) {
        return findByAccountId(accountId);
    }

    @Override
    public List<RecurrentTransaction> findByCategory(Category category) {
        return findByCategoryId(category.getId());
    }

    @Override
    public List<RecurrentTransaction> findByFrequency(Integer frequency) {
        return query("SELECT * FROM recurrent_transactions WHERE frequency=?", ps -> ps.setInt(1, frequency));
    }

    @Override
    public List<RecurrentTransaction> findByCustomFrequency(Integer customFrequency) {
        return query("SELECT * FROM recurrent_transactions WHERE custom_frequency=?", ps -> ps.setInt(1, customFrequency));
    }

    @Override
    public List<RecurrentTransaction> findByStatus(Integer status) {
        return query("SELECT * FROM recurrent_transactions WHERE status=?", ps -> ps.setInt(1, status));
    }

    @Override
    public List<RecurrentTransaction> findByAccount(Account account) {
        return findByAccountId(account.getId());
    }

    @Override
    public List<User> findByActive(boolean active) {
        return List.of();
    }

    private RecurrentTransaction insert(RecurrentTransaction rt) {
        rt.setId(UUID.randomUUID());
        String sql = "INSERT INTO recurrent_transactions " +
                "(id, value, status, initial_date, end_date, frequency, custom_frequency, created_at, updated_at, account_id, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, rt.getId());
            stmt.setBigDecimal(2, rt.getValue());
            if (rt.getStatus() != null) {
                stmt.setInt(3, rt.getStatus());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setDate(4, Date.valueOf(rt.getInitialDate()));
            if (rt.getEndDate() != null) {
                stmt.setDate(5, Date.valueOf(rt.getEndDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setInt(6, rt.getFrequency());
            if (rt.getCustomFrequency() != null) {
                stmt.setInt(7, rt.getCustomFrequency());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setTimestamp(8, Timestamp.valueOf(rt.getCreatedAt()));
            stmt.setTimestamp(9, Timestamp.valueOf(rt.getUpdatedAt()));
            stmt.setObject(10, rt.getAccountId());
            stmt.setObject(11, rt.getCategoryId());
            stmt.executeUpdate();
            return rt;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private RecurrentTransaction update(RecurrentTransaction rt) {
        String sql = "UPDATE recurrent_transactions SET " +
                "value=?, status=?, initial_date=?, end_date=?, frequency=?, custom_frequency=?, updated_at=?, account_id=?, category_id=? " +
                "WHERE id=?";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, rt.getValue());
            if (rt.getStatus() != null) {
                stmt.setInt(2, rt.getStatus());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setDate(3, Date.valueOf(rt.getInitialDate()));
            if (rt.getEndDate() != null) {
                stmt.setDate(4, Date.valueOf(rt.getEndDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setInt(5, rt.getFrequency());
            if (rt.getCustomFrequency() != null) {
                stmt.setInt(6, rt.getCustomFrequency());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setTimestamp(7, Timestamp.valueOf(rt.getUpdatedAt()));
            stmt.setObject(8, rt.getAccountId());
            stmt.setObject(9, rt.getCategoryId());
            stmt.setObject(10, rt.getId());
            stmt.executeUpdate();
            return rt;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<RecurrentTransaction> query(String sql, SqlBinder binder) {
        List<RecurrentTransaction> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private RecurrentTransaction map(ResultSet rs) throws SQLException {
        RecurrentTransaction rt = new RecurrentTransaction();
        rt.setId(rs.getObject("id", UUID.class));
        rt.setValue(rs.getBigDecimal("value"));
        int status = rs.getInt("status");
        if (!rs.wasNull()) {
            rt.setStatus(status);
        }
        rt.setInitialDate(rs.getDate("initial_date").toLocalDate());
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            rt.setEndDate(endDate.toLocalDate());
        }
        rt.setFrequency(rs.getInt("frequency"));
        int custom = rs.getInt("custom_frequency");
        if (!rs.wasNull()) {
            rt.setCustomFrequency(custom);
        }
        rt.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        rt.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        rt.setAccountId(rs.getObject("account_id", UUID.class));
        rt.setCategoryId(rs.getObject("category_id", UUID.class));
        return rt;
    }

    @FunctionalInterface
    private interface SqlBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }
}
