package com.proyecto.citas_proyecto.Model;

public class Paciente extends Persona {
    private String historialClinico;

    // 1. Constructor Completo (6 parámetros)
    public Paciente(int id, String nombre, String apellido, String correo, String contrasena, String historialClinico) {
        super(id, nombre, apellido, correo, contrasena);
        this.historialClinico = historialClinico;
    }

    // 2. CONSTRUCTOR COMPODÍN (5 parámetros) - ¡ESTO SALVA EL PROYECTO!
    public Paciente(int id, String nombre, String apellido, String correo, String contrasena) {
        super(id, nombre, apellido, correo, contrasena);
        this.historialClinico = "Sin historial inicial"; // Le asigna un valor por defecto
    }

    // Getters y Setters
    public String getHistorialClinico() { return historialClinico; }
    public void setHistorialClinico(String historialClinico) { this.historialClinico = historialClinico; }
}