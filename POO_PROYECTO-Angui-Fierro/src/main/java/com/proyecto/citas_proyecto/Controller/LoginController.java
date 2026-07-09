package com.proyecto.citas_proyecto.Controller;

import com.proyecto.citas_proyecto.DAO.*;
import com.proyecto.citas_proyecto.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPasswordReg;
    @FXML private Button btnRegistrar;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPasswordLogin;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private Button btnLogin;

    private MedicoDAO medicoDAO = new MedicoDAOImpl();
    private PacienteDAO pacienteDAO = new PacienteDAOImpl();
    private AdministradorDAO adminDAO = new AdministradorDAOImpl();

    @FXML
    public void initialize() {
        cmbCategoria.getItems().addAll("Administrador", "Médico", "Paciente");
    }

    @FXML
    void login(ActionEvent event) {
        String correo = txtUsuario.getText().trim();
        String password = txtPasswordLogin.getText().trim();
        String categoria = cmbCategoria.getValue();
        if (correo.isEmpty() || password.isEmpty() || categoria == null) {
            mostrarAlerta("Campos Incompletos", "Por favor, llene todos los campos y seleccione una categoría.");
            return;
        }
        switch (categoria) {
            case "Administrador":
                Administrador admin = adminDAO.login(correo, password);
                if (admin != null) {
                    abrirDashboard("ADMINISTRADOR", admin);
                } else {
                    mostrarAlerta("Error", "Credenciales de Administrador incorrectas.");
                }
                break;

            case "Médico":
                Medicos medico = medicoDAO.login(correo, password);
                if (medico != null) {
                    abrirDashboard("MEDICO", medico);
                } else {
                    mostrarAlerta("Error", "Credenciales de Médico incorrectas.");
                }
                break;

            case "Paciente":
                Paciente paciente = pacienteDAO.login(correo, password);
                if (paciente != null) {
                    abrirDashboard("PACIENTE", paciente);
                } else {
                    mostrarAlerta("Error", "Credenciales de Paciente incorrectas.");
                }
                break;
        }
    }

    @FXML
    void registrar(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();
        String password = txtPasswordReg.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, llene todos los campos para crear su cuenta.");
            return;
        }

        // === ¡CORREGIDO AQUÍ! ===
        // Se añade el '0' al principio como ID temporal para cumplir los requisitos de la herencia de Persona
        Paciente nuevoPaciente = new Paciente(0, nombre, apellido, correo, password, "Sin historial inicial");

        if (pacienteDAO.crear(nuevoPaciente)) {
            mostrarAlerta("¡Cuenta Creada!", "Registro exitoso. Ya puedes iniciar sesión del lado derecho.");
            limpiarCamposRegistro();
        } else {
            mostrarAlerta("Error de Registro", "No se pudo registrar la cuenta. El correo podría estar duplicado.");
        }
    }

    private void abrirDashboard(String rol, Persona usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/citas_proyecto/Crud.fxml"));
            Parent root = loader.load();
            CrudController controller = loader.getController();
            controller.definirUsuarioYRol(usuario, rol);

            // Cambiamos la escena actual para mostrar el CRUD
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Citas Médicas - Panel de Gestión");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de carga", "No se pudo encontrar o abrir el archivo Crud.fxml.");
        }
    }

    private void limpiarCamposRegistro() {
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtPasswordReg.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}