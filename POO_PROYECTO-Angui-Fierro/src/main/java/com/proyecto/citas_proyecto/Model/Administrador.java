package com.proyecto.citas_proyecto.Model;

public class Administrador extends Persona {
    public Administrador(int id, String nombre, String apellido, String correo, String contrasena) {
        super(id, nombre, apellido, correo, contrasena); // Pasa los 5 parámetros a Persona
    }
}