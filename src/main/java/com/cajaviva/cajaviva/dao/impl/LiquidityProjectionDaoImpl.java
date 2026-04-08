package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.LiquidityProjectionDao;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LiquidityProjectionDaoImpl implements LiquidityProjectionDao {

    private final Conexion conexion;

    public LiquidityProjectionDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<LiquidityProjection> findAll() {
        List<LiquidityProjection> result = new ArrayList<>();

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM LiquidityProjections");
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
    public Optional<LiquidityProjection> findById(UUID id) {
        LiquidityProjection lp = null;

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM LiquidityProjections WHERE id = ?")) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lp = mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(lp);
    }

    @Override
    public LiquidityProjection save(LiquidityProjection entity) {

        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        boolean exists = false;

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM LiquidityProjections WHERE id = ?")) {

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
             PreparedStatement ps = conn.prepareStatement("DELETE FROM LiquidityProjections WHERE id = ?")) {

            ps.setObject(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LiquidityProjection> findByAccountId(UUID accountId) {
        List<LiquidityProjection> result = new ArrayList<>();

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM LiquidityProjections WHERE account_id = ?")) {

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

    // 🔥 INSERT
    private LiquidityProjection insert(LiquidityProjection entity) {

        entity.setId(UUID.randomUUID());

        String sql = "INSERT INTO LiquidityProjections (id, calculation_date, projected_balance, projection_date, created_at, updated_at, account_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

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

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return entity;
    }

    // 🔥 UPDATE
    private LiquidityProjection update(LiquidityProjection entity) {

        String sql = "UPDATE LiquidityProjections SET calculation_date=?, projected_balance=?, projection_date=?, updated_at=?, account_id=? WHERE id=?";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(entity.getCalculationDate()));
            ps.setBigDecimal(2, entity.getProjectedBalance());
            ps.setDate(3, Date.valueOf(entity.getProjectionDate()));
            ps.setTimestamp(4, Timestamp.valueOf(entity.getUpdatedAt()));
            ps.setObject(5, entity.getAccountId());
            ps.setObject(6, entity.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return entity;
    }

    // 🔥 MAPEO
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
}