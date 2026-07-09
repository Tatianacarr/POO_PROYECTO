package com.proyecto.citas_proyecto.DAO;

import com.proyecto.citas_proyecto.Model.Cita;
import java.util.List;

public interface CitaDAO {
    boolean agendar(Cita cita);
    List<Cita> listarPorUsuario(int usuarioId); // Para que el paciente solo vea sus citas
    List<Cita> listarTodas(); // Para el Admin y los Médicos
    boolean actualizarEstado(int idCita, String nuevoEstado);
    boolean eliminar(int idCita);
}
