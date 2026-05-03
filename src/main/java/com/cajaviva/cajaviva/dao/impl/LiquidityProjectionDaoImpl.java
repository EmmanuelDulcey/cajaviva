package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.LiquidityProjectionDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("LiquidityProjectionDaoJDBC")
public class LiquidityProjectionDaoImpl implements LiquidityProjectionDao {

    private final Conexion conexion;

    public LiquidityProjectionDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<LiquidityProjection> findAll() {
        return query("SELECT * FROM liquidity_projections", ps -> {});
    }

    @Override
    public Optional<LiquidityProjection> findById(UUID id) {
        List<LiquidityProjection> result = query("SELECT * FROM liquidity_projections WHERE id = ?", ps -> ps.setObject(1, id));
        return result.stream().findFirst();
    }

    @Override
    public LiquidityProjection save(LiquidityProjection entity) {
        return entity.getId() == null ? insert(entity) : update(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM liquidity_projections WHERE id = ?")) {
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
             PreparedStatement ps = conn.prepareStatement("DELETE FROM liquidity_projections WHERE id = ?")) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LiquidityProjection> findByAccount(Account account) {
        return findByAccountId(account.getId());
    }

    @Override
    public List<LiquidityProjection> findByProjectionDate(LocalDate projectionDate) {
        return query("SELECT * FROM liquidity_projections WHERE projection_date = ?", ps -> ps.setDate(1, Date.valueOf(projectionDate)));
    }

    @Override
    public List<FinancialTransaction> findByStatus(Integer status) {
        return List.of();
    }

    @Override
    public List<LiquidityProjection> findByAccountId(UUID accountId) {
        return query("SELECT * FROM liquidity_projections WHERE account_id = ?", ps -> ps.setObject(1, accountId));
    }

    @Override
    public List<User> findByActive(boolean active) {
        return List.of();
    }

    private LiquidityProjection insert(LiquidityProjection entity) {
        entity.setId(UUID.randomUUID());
        String sql = "INSERT INTO liquidity_projections (id, calculation_date, projected_balance, projection_date, created_at, updated_at, account_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, entity.getId());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getCalculationDate()));
            ps.setBigDecimal(3, entity.getProjectedBalance());
            ps.setDate(4, Date.valueOf(entity.getProjectionDate()));
            ps.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));
            ps.setObject(7, entity.getAccountId());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private LiquidityProjection update(LiquidityProjection entity) {
        String sql = "UPDATE liquidity_projections SET calculation_date=?, projected_balance=?, projection_date=?, updated_at=?, account_id=? WHERE id=?";
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(entity.getCalculationDate()));
            ps.setBigDecimal(2, entity.getProjectedBalance());
            ps.setDate(3, Date.valueOf(entity.getProjectionDate()));
            ps.setTimestamp(4, Timestamp.valueOf(entity.getUpdatedAt()));
            ps.setObject(5, entity.getAccountId());
            ps.setObject(6, entity.getId());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<LiquidityProjection> query(String sql, SqlBinder binder) {
        List<LiquidityProjection> result = new ArrayList<>();
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

    private LiquidityProjection mapRow(ResultSet rs) throws SQLException {
        LiquidityProjection lp = new LiquidityProjection();
        lp.setId(rs.getObject("id", UUID.class));
        lp.setCalculationDate(rs.getTimestamp("calculation_date").toLocalDateTime());
        lp.setProjectedBalance(rs.getBigDecimal("projected_balance"));
        lp.setProjectionDate(rs.getDate("projection_date").toLocalDate());
        lp.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        lp.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        lp.setAccountId(rs.getObject("account_id", UUID.class));
        return lp;
    }

    @FunctionalInterface
    private interface SqlBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }
}
