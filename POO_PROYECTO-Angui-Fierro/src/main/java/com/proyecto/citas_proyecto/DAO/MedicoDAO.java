package com.proyecto.citas_proyecto.DAO;

import com.proyecto.citas_proyecto.Model.Medicos;
import java.util.List;

public interface MedicoDAO {
    boolean crear(Medicos medico);
    List<Medicos> leerTodos();
    boolean actualizar(Medicos medico);
    boolean eliminar(int id); // Cambiado a int id
    Medicos login(String correo, String contrasena);
}