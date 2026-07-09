package com.proyecto.citas_proyecto.BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/Citas_medicas";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    public static Connection getConexion(){
        try {
            Connection cn = DriverManager.getConnection(URL, USER, PASSWORD
            );
            System.out.println(" Conectado a PostgreSQL");
            return cn;
        } catch(SQLException e){
            System.out.println(" Error de conexión:");
            System.out.println(e.getMessage());
            return null;
        }

    }

}