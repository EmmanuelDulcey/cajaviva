package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {

    private final Conexion conexion;

    public UserDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT id, name, email FROM users";

        try (
                Connection connection = conexion.obtenerConexion();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User findById(UUID id) {

        String sql = "SELECT id, name, email FROM users WHERE id = ?";

        try (
                Connection connection = conexion.obtenerConexion();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    user.setId(UUID.fromString(rs.getString("id")));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User create(User user) {

        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";

        try (
                Connection connection = conexion.obtenerConexion();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {

            stmt.setString(1, user.getId().toString());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User update(UUID id, User user) {

        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";

        try (
                Connection connection = conexion.obtenerConexion();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, id.toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void delete(UUID id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (
                Connection connection = conexion.obtenerConexion();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}