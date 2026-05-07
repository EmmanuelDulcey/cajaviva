package com.cajaviva.cajaviva.utilities;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class Conexion {

    private DataSource dataSource = null;

    public Conexion() {
    }

    public Connection obtenerConexion() throws Exception {
        return dataSource.getConnection();
    }
}