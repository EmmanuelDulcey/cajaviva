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

@Repository ("RecurrentTransactionDaoJDBC")
public class RecurrentTransactionDaoImpl implements RecurrentTransactionDao {

    private final Conexion conexion;

    public RecurrentTransactionDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<RecurrentTransaction> findAll() {
        List<RecurrentTransaction> list = new ArrayList<>();

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RecurrentTransactions");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Optional<RecurrentTransaction> findById(UUID id) {

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RecurrentTransactions WHERE id=?")) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public RecurrentTransaction save(RecurrentTransaction rt) {

        if (rt.getId() == null) {
            return insert(rt);
        } else {
            return update(rt);
        }
    }

    @Override
    public boolean existsById(UUID id) {

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM RecurrentTransactions WHERE id=?")) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void deleteById(UUID id) {

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM RecurrentTransactions WHERE id=?")) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔥 MÉTODOS PERSONALIZADOS

    @Override
    public List<RecurrentTransaction> findByAccountId(UUID accountId) {

        List<RecurrentTransaction> list = new ArrayList<>();

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM RecurrentTransactions WHERE account_id=?")) {

            stmt.setObject(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<RecurrentTransaction> findByCategoryId(UUID categoryId) {

        List<RecurrentTransaction> list = new ArrayList<>();

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM RecurrentTransactions WHERE category_id=?")) {

            stmt.setObject(1, categoryId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔥 INSERT

    private RecurrentTransaction insert(RecurrentTransaction rt) {

        rt.setId(UUID.randomUUID());

        String sql = "INSERT INTO RecurrentTransactions " +
                "(id, value, initial_date, end_date, frequency, custom_frequency, created_at, updated_at, account_id, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, rt.getId());
            stmt.setBigDecimal(2, rt.getValue());
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

            stmt.setTimestamp(7, Timestamp.valueOf(rt.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.valueOf(rt.getUpdatedAt()));
            stmt.setObject(9, rt.getAccountId());
            stmt.setObject(10, rt.getCategoryId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return rt;
    }

    // 🔥 UPDATE

    private RecurrentTransaction update(RecurrentTransaction rt) {

        String sql = "UPDATE RecurrentTransactions SET " +
                "value=?, initial_date=?, end_date=?, frequency=?, custom_frequency=?, updated_at=?, account_id=?, category_id=? " +
                "WHERE id=?";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, rt.getValue());
            stmt.setDate(2, Date.valueOf(rt.getInitialDate()));

            if (rt.getEndDate() != null) {
                stmt.setDate(3, Date.valueOf(rt.getEndDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setInt(4, rt.getFrequency());

            if (rt.getCustomFrequency() != null) {
                stmt.setInt(5, rt.getCustomFrequency());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setTimestamp(6, Timestamp.valueOf(rt.getUpdatedAt()));
            stmt.setObject(7, rt.getAccountId());
            stmt.setObject(8, rt.getCategoryId());
            stmt.setObject(9, rt.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return rt;
    }

    // 🔥 MAP

    private RecurrentTransaction map(ResultSet rs) throws SQLException {

        RecurrentTransaction rt = new RecurrentTransaction();

        rt.setId(rs.getObject("id", UUID.class));
        rt.setValue(rs.getBigDecimal("value"));
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

    @Override
    public List<RecurrentTransaction> findByAccount(UUID account_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByAccount'");
    }

    @Override
    public List<RecurrentTransaction> findByCategory(Category category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByCategory'");
    }

    @Override
    public List<RecurrentTransaction> findByFrequency(Integer frequency) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByFrequency'");
    }

    @Override
    public List<RecurrentTransaction> findByCustomFrequency(Integer customFrequency) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByCustomFrequency'");
    }

    @Override
    public List<RecurrentTransaction> findByStatus(Integer status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByStatus'");
    }

    @Override
    public List<RecurrentTransaction> findByAccount(Account account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByAccount'");
    }

    @Override
    public List<User> findByActive(boolean active) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByActive'");
    }
}