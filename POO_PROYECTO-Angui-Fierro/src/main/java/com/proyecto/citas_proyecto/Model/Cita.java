package com.proyecto.citas_proyecto.Model;

import java.sql.Date;
import java.sql.Time;

public class Cita {
    private int id;
    private String specialty; // mapeado como 'especialidad'
    private String doctor;    // mapeado como 'medico'
    private Date date;        // mapeado como 'fecha'
    private Time time;        // mapeado como 'hora'
    private String status;    // mapeado como 'estado'
    private String description; // mapeado como 'descripcion'
    private int userId;       // mapeado como 'usuario_id' (FK)

    // Constructor vacío obligado para buenas prácticas
    public Cita() {
    }

    // Constructor completo para los DAOs y el Controller
    public Cita(int id, String specialty, String doctor, Date date, Time time, String status, String description, int userId) {
        this.id = id;
        this.specialty = specialty;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = status;
        this.description = description;
        this.userId = userId;
    }

    // === Getters y Setters con los nombres exactos que usa PropertyValueFactory ===
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEspecialidad() { return specialty; }
    public void setEspecialidad(String specialty) { this.specialty = specialty; }

    public String getMedico() { return doctor; }
    public void setMedico(String doctor) { this.doctor = doctor; }

    public Date getFecha() { return date; }
    public void setFecha(Date date) { this.date = date; }

    public Time getHora() { return time; }
    public void setHora(Time time) { this.time = time; }

    public String getEstado() { return status; }
    public void setEstado(String status) { this.status = status; }

    public String getDescripcion() { return description; }
    public void setDescripcion(String description) { this.description = description; }

    public int getUsuarioId() { return userId; }
    public void setUsuarioId(int userId) { this.userId = userId; }
}