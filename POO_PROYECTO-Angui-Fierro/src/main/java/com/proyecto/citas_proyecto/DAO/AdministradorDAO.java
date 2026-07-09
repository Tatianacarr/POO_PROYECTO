package com.proyecto.citas_proyecto.DAO;

import com.proyecto.citas_proyecto.Model.Administrador;

public interface AdministradorDAO {
    Administrador login(String correo, String contrasena);
}