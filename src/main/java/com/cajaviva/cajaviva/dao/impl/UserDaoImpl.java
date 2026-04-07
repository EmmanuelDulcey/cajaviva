package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {

    private final Conexion conexion;

    public UserDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (Connection connection = conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getObject("id", UUID.class));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setActive(resultSet.getBoolean("active"));
                user.setPasswordDigest(resultSet.getString("password_digest"));
                user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
                result.add(user);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<User> findById(UUID id) {
        User user = null;
        try (Connection connection = conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE id = ?")) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getObject("id", UUID.class));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setActive(resultSet.getBoolean("active"));
                user.setPasswordDigest(resultSet.getString("password_digest"));
                user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User save(User entity) {
        try (Connection connection = conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Users (id, name, last_name, email, active, password_digest, created_at, updated_at) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setObject(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getLastName());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setBoolean(5, entity.getActive());
            preparedStatement.setString(6, entity.getPasswordDigest());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(entity.getCreatedAt()));
            preparedStatement.setTimestamp(8, Timestamp.valueOf(entity.getUpdatedAt()));

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
            entity = null;
        }
        return entity;
    }

    @Override
    public boolean existsById(UUID id) {
        boolean exists = false;
        try (Connection connection = conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE id = ?")) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return exists;
    }

    @Override
    public void deleteById(UUID id) {
        try (Connection connection = conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id = ?")) {

            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = null;
        try (Connection connection = conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE email = ?")) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getObject("id", UUID.class));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setActive(resultSet.getBoolean("active"));
                user.setPasswordDigest(resultSet.getString("password_digest"));
                user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(user);
    }
}
