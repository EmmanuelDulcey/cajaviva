package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.AccountDao;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountDaoImpl implements AccountDao {

    private final Conexion conexion;

    public AccountDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Account> findAll() {
        List<Account> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Accounts");
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
    public Optional<Account> findById(UUID id) {
        Account account = null;
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Accounts WHERE id = ?")) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                account = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(account);
    }

    @Override
    public Account save(Account entity) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Accounts (id, name, account_type, balance, created_at, updated_at, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            ps.setObject(1, entity.getId());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getAccountType());
            ps.setBigDecimal(4, entity.getBalance());
            ps.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));
            ps.setObject(7, entity.getUserId());

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
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Accounts WHERE id = ?")) {

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
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Accounts WHERE id = ?")) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        List<Account> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Accounts WHERE user_id = ?")) {

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
    public List<Account> findByAccountType(Integer accountType) {
        List<Account> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Accounts WHERE account_type = ?")) {

            ps.setInt(1, accountType);
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
    private Account mapRow(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getObject("id", UUID.class));
        account.setName(rs.getString("name"));
        account.setAccountType(rs.getInt("account_type"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        account.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        account.setUserId(rs.getObject("user_id", UUID.class));

        return account;
    }
}
