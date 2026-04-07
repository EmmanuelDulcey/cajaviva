package com.cajaviva.cajaviva.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Component
public class Conexion {

    @Autowired
    private DataSource dataSource;

    public Connection obtenerConexion() throws SQLException {
        return dataSource.getConnection();
    }
}
