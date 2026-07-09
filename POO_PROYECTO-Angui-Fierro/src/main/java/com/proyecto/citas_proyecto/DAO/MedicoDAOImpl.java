package com.proyecto.citas_proyecto.DAO;

import com.proyecto.citas_proyecto.BaseDatos.Conexion;
import com.proyecto.citas_proyecto.Model.Medicos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAOImpl implements MedicoDAO {

    @Override
    public boolean crear(Medicos medico) {
        String sql = "INSERT INTO usuario (nombre, apellido, correo, contrasena) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getApellido());
            ps.setString(3, medico.getCorreo());
            ps.setString(4, medico.getContrasena());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Medicos> leerTodos() {
        List<Medicos> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Medicos(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contrasena")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean actualizar(Medicos medico) {
        String sql = "UPDATE usuario SET nombre=?, apellido=?, contrasena=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getApellido());
            ps.setString(3, medico.getContrasena());
            ps.setInt(4, medico.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuario WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Medicos login(String correo, String contrasena) {
        String sql = "SELECT * FROM usuario WHERE correo=? AND contrasena=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicos(
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