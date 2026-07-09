package com.proyecto.citas_proyecto.DAO;

import com.proyecto.citas_proyecto.Model.Paciente;
import java.util.List;

public interface PacienteDAO {

    boolean guardar(Paciente paciente);

    boolean actualizar(Paciente paciente);

    boolean eliminar(int id);

    Paciente buscarPorId(int id);

    List<Paciente> listar();
}