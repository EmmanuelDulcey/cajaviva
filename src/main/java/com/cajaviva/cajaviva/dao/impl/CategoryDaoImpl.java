package com.cajaviva.cajaviva.dao.impl;

import com.cajaviva.cajaviva.dao.CategoryDao;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.utilities.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    private final Conexion conexion;

    public CategoryDaoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Category> findAll() {
        List<Category> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Categories");
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
    public Optional<Category> findById(UUID id) {
        Category category = null;
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Categories WHERE id = ?")) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                category = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(category);
    }

    @Override
    public Category save(Category entity) {
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Categories (id, name, type, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)")) {

            ps.setObject(1, entity.getId());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getType());
            ps.setString(4, entity.getDescription());
            ps.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));

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
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Categories WHERE id = ?")) {

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
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Categories WHERE id = ?")) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Category> findByName(String name) {
        Category category = null;
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Categories WHERE name = ?")) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                category = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> findByType(Integer type) {
        List<Category> result = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Categories WHERE type = ?")) {

            ps.setInt(1, type);
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
    private Category mapRow(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getObject("id", UUID.class));
        category.setName(rs.getString("name"));
        category.setType(rs.getInt("type"));
        category.setDescription(rs.getString("description"));
        category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        category.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return category;
    }
}
