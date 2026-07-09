package com.proyecto.citas_proyecto.DAO;

import com.proyecto.citas_proyecto.BaseDatos.Conexion;
import com.proyecto.citas_proyecto.Model.Cita;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAOImpl implements CitaDAO {

    @Override
    public boolean agendar(Cita cita) {
        String sql = "INSERT INTO citas (especialidad, medico, fecha, hora, estado, descripcion, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cita.getEspecialidad());
            ps.setString(2, cita.getMedico());
            ps.setDate(3, cita.getFecha());
            ps.setTime(4, cita.getHora());
            ps.setString(5, cita.getEstado());
            ps.setString(6, cita.getDescripcion());
            ps.setInt(7, cita.getUsuarioId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Cita> listarPorUsuario(int usuarioId) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE usuario_id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Cita(
                            rs.getInt("id"),
                            rs.getString("especialidad"),
                            rs.getString("medico"),
                            rs.getDate("fecha"),
                            rs.getTime("hora"),
                            rs.getString("estado"),
                            rs.getString("descripcion"),
                            rs.getInt("usuario_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Cita> listarTodas() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cita(
                        rs.getInt("id"),
                        rs.getString("especialidad"),
                        rs.getString("medico"),
                        rs.getDate("fecha"),
                        rs.getTime("hora"),
                        rs.getString("estado"),
                        rs.getString("descripcion"),
                        rs.getInt("usuario_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean actualizarEstado(int idCita, String nuevoEstado) {
        String sql = "UPDATE citas SET estado = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idCita) {
        String sql = "DELETE FROM citas WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}