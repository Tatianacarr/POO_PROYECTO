package com.proyecto.citas_proyecto.Controller;

import com.proyecto.citas_proyecto.DAO.*;
import com.proyecto.citas_proyecto.Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CrudController {

    // === ELEMENTOS GENERALES Y NAVEGACIÓN ===
    @FXML private Label lblBienvenida;
    @FXML private Button btnMenuInicio, btnMenuPaciente, btnMenuMedico, btnMenuAdmin;
    @FXML private VBox panelInicio, panelPaciente, panelMedico, panelAdmin;

    // === COMPONENTES PANEL PACIENTE (AGENDAMIENTO) ===
    @FXML private ComboBox<String> cmbEspecialidad, cmbMedico;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private TextArea txtDescripcion;
    @FXML private TableView<Cita> tablaMisCitas;

    // === COMPONENTES PANEL MÉDICO ===
    @FXML private TableView<Cita> tablaCitasMedicas;
    @FXML private Button btnCompletarCita;

    // === COMPONENTES PANEL ADMINISTRADOR (BUSCADOR) ===
    @FXML private TextField txtBuscarId;
    @FXML private TableView<Cita> tablaTodasLasCitas;
    @FXML private Button btnEliminarCitaAdmin;

    // === VARIABLES DE CONTROL Y ACCESO A DATOS ===
    private Persona usuarioLogueado;
    private String rolUsuario;
    private CitaDAO citaDAO = new CitaDAOImpl();

    @FXML
    public void initialize() {
        // Enlazar las acciones de los botones del menú lateral
        btnMenuInicio.setOnAction(e -> mostrarPanel(panelInicio));
        btnMenuPaciente.setOnAction(e -> mostrarPanel(panelPaciente));
        btnMenuMedico.setOnAction(e -> mostrarPanel(panelMedico));
        btnMenuAdmin.setOnAction(e -> mostrarPanel(panelAdmin));

        // Llenar datos iniciales de los ComboBox del Paciente
        cmbEspecialidad.getItems().addAll("Medicina General", "Cardiología", "Pediatría", "Odontología");
        cmbMedico.getItems().addAll("Dr. Byron Loarte", "Dra. Ana Martínez", "Dr. Carlos Cueva");

        // === CONFIGURACIÓN DE COLUMNAS DE LAS TABLAS (VITAL PARA QUE SE VEAN LOS DATOS) ===
        configurarTablas();
    }

    /**
     * Define el usuario de la sesión actual y restringe los accesos por rol (POO)
     */
    public void definirUsuarioYRol(Persona usuario, String rol) {
        this.usuarioLogueado = usuario;
        this.rolUsuario = rol.toUpperCase();

        // Cambiar el texto de la esquina superior izquierda
        lblBienvenida.setText("¡Hola, " + usuario.getNombre() + "!\n(" + rolUsuario + ")");

        // Control Polimórfico de Accesos de Seguridad
        if (rolUsuario.equals("PACIENTE")) {
            btnMenuMedico.setDisable(true);
            btnMenuAdmin.setDisable(true);
            actualizarTablaPaciente();
        }
        else if (rolUsuario.equals("MÉDICO") || rolUsuario.equals("MEDICO")) {
            btnMenuPaciente.setDisable(true);
            btnMenuAdmin.setDisable(true);
            actualizarTablaMedico();
        }
        else if (rolUsuario.equals("ADMINISTRADOR")) {
            btnMenuPaciente.setDisable(false);
            btnMenuMedico.setDisable(false);
            btnMenuAdmin.setDisable(false);
            actualizarTablaAdmin();
        }

        // Forzar a que la primera pantalla vista por todos sea la Introducción/Servicios
        mostrarPanel(panelInicio);
    }

    /**
     * Intercambia visualmente las capas del StackPane central
     */
    private void mostrarPanel(VBox panelAActivar) {
        panelInicio.setVisible(panelAActivar == panelInicio);
        panelPaciente.setVisible(panelAActivar == panelPaciente);
        panelMedico.setVisible(panelAActivar == panelMedico);
        panelAdmin.setVisible(panelAActivar == panelAdmin);
        panelAActivar.toFront();
    }

    // ================= SECCIÓN DE ACCIONES DEL PACIENTE =================
    @FXML
    private void guardarCita() {
        if (cmbEspecialidad.getValue() == null || cmbMedico.getValue() == null || dpFecha.getValue() == null || txtHora.getText().isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor llene todos los campos para agendar el turno.");
            return;
        }

        try {
            Cita nuevaCita = new Cita(
                    0,
                    cmbEspecialidad.getValue(),
                    cmbMedico.getValue(),
                    Date.valueOf(dpFecha.getValue()),
                    Time.valueOf(LocalTime.parse(txtHora.getText())),
                    "Pendiente",
                    txtDescripcion.getText(),
                    usuarioLogueado.getId() // FK del usuario logueado en la BD
            );

            if (citaDAO.agendar(nuevaCita)) {
                mostrarAlerta("Éxito", "Su cita médica ha sido registrada correctamente.");
                actualizarTablaPaciente();
                limpiarCamposPaciente();
            } else {
                mostrarAlerta("Error", "No se pudo conectar o almacenar la cita en la Base de Datos.");
            }
        } catch (Exception e) {
            mostrarAlerta("Formato Incorrecto", "Asegúrese de que la hora esté en formato HH:MM:SS (ej: 09:30:00).");
        }
    }

    private void limpiarCamposPaciente() {
        cmbEspecialidad.setValue(null);
        cmbMedico.setValue(null);
        dpFecha.setValue(null);
        txtHora.clear();
        txtDescripcion.clear();
    }

    // ================= SECCIÓN DE ACCIONES DEL MÉDICO =================
    @FXML
    private void marcarCitaCompletada() {
        Cita seleccionada = tablaCitasMedicas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            if (citaDAO.actualizarEstado(seleccionada.getId(), "Completada")) {
                mostrarAlerta("Éxito", "Consulta médica finalizada con éxito.");
                actualizarTablaMedico();
            }
        } else {
            mostrarAlerta("Aviso", "Por favor, seleccione un paciente de la lista primero.");
        }
    }

    // ================= SECCIÓN DE ACCIONES DEL ADMINISTRADOR =================
    @FXML
    private void buscarCitaPorId() {
        String idStr = txtBuscarId.getText().trim();
        if (idStr.isEmpty()) {
            mostrarAlerta("Aviso", "Ingrese un número de ID para iniciar el escaneo.");
            return;
        }
        try {
            int idBuscar = Integer.parseInt(idStr);
            List<Cita> todas = citaDAO.listarTodas();
            List<Cita> filtrada = new ArrayList<>();

            for (Cita c : todas) {
                if (c.getId() == idBuscar) {
                    filtrada.add(c);
                    break;
                }
            }

            tablaTodasLasCitas.getItems().clear();
            tablaTodasLasCitas.getItems().addAll(filtrada);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de búsqueda debe contener solo números enteros.");
        }
    }

    @FXML
    private void restablecerTablaAdmin() {
        txtBuscarId.clear();
        actualizarTablaAdmin();
    }

    @FXML
    private void eliminarCitaAdministrador() {
        Cita seleccionada = tablaTodasLasCitas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            if (citaDAO.eliminar(seleccionada.getId())) {
                mostrarAlerta("Eliminado", "La cita con ID " + seleccionada.getId() + " fue borrada del sistema.");
                actualizarTablaAdmin();
            }
        } else {
            mostrarAlerta("Aviso", "Seleccione la cita que desea dar de baja.");
        }
    }

    // ================= CONFIGURACIÓN INTERNA DE COLUMNAS JAVAFX =================
    private void configurarTablas() {
        // 1. Columnas Tabla Paciente
        TableColumn<Cita, Integer> colIdPac = new TableColumn<>("ID Cita");
        colIdPac.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Cita, String> colMedPac = new TableColumn<>("Médico Asignado");
        colMedPac.setCellValueFactory(new PropertyValueFactory<>("medico"));
        TableColumn<Cita, Date> colFecPac = new TableColumn<>("Fecha");
        colFecPac.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Cita, String> colEstPac = new TableColumn<>("Estado");
        colEstPac.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaMisCitas.getColumns().addAll(colIdPac, colMedPac, colFecPac, colEstPac);

        // 2. Columnas Tabla Médico
        TableColumn<Cita, Integer> colIdMed = new TableColumn<>("ID Consulta");
        colIdMed.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Cita, Integer> colUserMed = new TableColumn<>("ID Paciente");
        colUserMed.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
        TableColumn<Cita, Date> colFecMed = new TableColumn<>("Fecha");
        colFecMed.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Cita, Time> colHorMed = new TableColumn<>("Hora Pactada");
        colHorMed.setCellValueFactory(new PropertyValueFactory<>("hora"));
        tablaCitasMedicas.getColumns().addAll(colIdMed, colUserMed, colFecMed, colHorMed);

        // 3. Columnas Tabla Administrador
        TableColumn<Cita, Integer> colIdAdm = new TableColumn<>("ID Global");
        colIdAdm.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Cita, String> colMedAdm = new TableColumn<>("Médico");
        colMedAdm.setCellValueFactory(new PropertyValueFactory<>("medico"));
        TableColumn<Cita, String> colEspAdm = new TableColumn<>("Especialidad");
        colEspAdm.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        TableColumn<Cita, String> colEstAdm = new TableColumn<>("Estado actual");
        colEstAdm.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaTodasLasCitas.getColumns().addAll(colIdAdm, colMedAdm, colEspAdm, colEstAdm);
    }

    // ================= MÉTODOS REFRESCADORES DE DATOS =================
    private void actualizarTablaPaciente() {
        tablaMisCitas.getItems().clear();
        tablaMisCitas.getItems().addAll(citaDAO.listarPorUsuario(usuarioLogueado.getId()));
    }

    private void actualizarTablaMedico() {
        // Filtra dinámicamente las citas donde el apellido del médico logueado coincida
        List<Cita> todas = citaDAO.listarTodas();
        List<Cita> misPacientes = new ArrayList<>();

        for (Cita c : todas) {
            if (c.getMedico().toLowerCase().contains(usuarioLogueado.getApellido().toLowerCase())) {
                misPacientes.add(c);
            }
        }
        tablaCitasMedicas.getItems().clear();
        tablaCitasMedicas.getItems().addAll(misPacientes);
    }

    private void actualizarTablaAdmin() {
        tablaTodasLasCitas.getItems().clear();
        tablaTodasLasCitas.getItems().addAll(citaDAO.listarTodas());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}