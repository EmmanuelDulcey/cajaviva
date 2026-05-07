package com.cajaviva.cajaviva.utilities;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class Conexion {

    private final DataSource dataSource;

    public Conexion(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection obtenerConexion() throws Exception {
        return dataSource.getConnection();
    }
}
