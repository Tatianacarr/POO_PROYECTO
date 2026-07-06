package com.proyecto.citas_proyecto.BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String url= "jdbc:postgresql://localhost:5432/Citas_medicas";
    private static final String USER= "postgres";
    private static final String PASSWORD="123456";

    private static Connection conexion;
    public static Connection getConexion(){
        try{
            if (conexion == null || conexion.isClosed()){
                conexion = DriverManager.getConnection(url,USER,PASSWORD);
                System.out.println("Conexion exitosa");

            }
        } catch (SQLException e) {
            System.out.println("Error de conexion " + e.getMessage());
        }
        return conexion;
    }
    public static void cerrarConexion(){
        try{
            if (conexion !=null && !conexion.isClosed()){
                conexion.close();
                System.out.println("Conexion cerrada");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
