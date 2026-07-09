package com.proyecto.citas_proyecto.DAO;

import com.proyecto.citas_proyecto.BaseDatos.Conexion;
import com.proyecto.citas_proyecto.Model.Administrador;
import java.sql.*;

public class AdministradorDAOImpl implements AdministradorDAO {

    @Override
    public Administrador login(String correo, String contrasena) {
        String sql = "SELECT * FROM usuario WHERE correo=? AND contrasena=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Administrador(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getString("contrasena")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}