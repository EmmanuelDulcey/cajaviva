package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.RecurrentTransactionDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RecurrentTransactionDaoImpl implements RecurrentTransactionDao {

    private final Conexion conexion;

    public RecurrentTransactionDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<RecurrentTransaction> findAll() {
        List<RecurrentTransaction> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM RecurrentTransactions");
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
    public Optional<RecurrentTransaction> findById(UUID id) {
        RecurrentTransaction rt = null;
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM RecurrentTransactions WHERE id = ?")) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rt = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(rt);
    }

    @Override
    public RecurrentTransaction save(RecurrentTransaction entity) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO RecurrentTransactions (id, value, initial_date, end_date, frequency, custom_frequency, created_at, updated_at, account_id, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setObject(1, entity.getId());
            ps.setBigDecimal(2, entity.getValue());
            ps.setDate(3, Date.valueOf(entity.getInitialDate()));
            if (entity.getEndDate() != null) {
                ps.setDate(4, Date.valueOf(entity.getEndDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setInt(5, entity.getFrequency());
            if (entity.getCustomFrequency() != null) {
                ps.setInt(6, entity.getCustomFrequency());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setTimestamp(7, Timestamp.valueOf(entity.getCreatedAt()));
            ps.setTimestamp(8, Timestamp.valueOf(entity.getUpdatedAt()));
            ps.setObject(9, entity.getAccount().getId());
            ps.setObject(10, entity.getCategory().getId());

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
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM RecurrentTransactions WHERE id = ?")) {

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
             PreparedStatement ps = conn.prepareStatement("DELETE FROM RecurrentTransactions WHERE id = ?")) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RecurrentTransaction> findByAccountId(UUID accountId) {
        List<RecurrentTransaction> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM RecurrentTransactions WHERE account_id = ?")) {

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
    public List<RecurrentTransaction> findByCategoryId(UUID categoryId) {
        List<RecurrentTransaction> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM RecurrentTransactions WHERE category_id = ?")) {

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
    private RecurrentTransaction mapRow(ResultSet rs) throws SQLException {
        RecurrentTransaction rt = new RecurrentTransaction();
        rt.setId(rs.getObject("id", UUID.class));
        rt.setValue(rs.getBigDecimal("value"));
        rt.setInitialDate(rs.getDate("initial_date").toLocalDate());

        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            rt.setEndDate(endDate.toLocalDate());
        }

        rt.setFrequency(rs.getInt("frequency"));
        int customFreq = rs.getInt("custom_frequency");
        if (!rs.wasNull()) {
            rt.setCustomFrequency(customFreq);
        }

        rt.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        rt.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        Account account = new Account();
        account.setId(rs.getObject("account_id", UUID.class));
        rt.setAccount(account);

        Category category = new Category();
        category.setId(rs.getObject("category_id", UUID.class));
        rt.setCategory(category);

        return rt;
    }
}
