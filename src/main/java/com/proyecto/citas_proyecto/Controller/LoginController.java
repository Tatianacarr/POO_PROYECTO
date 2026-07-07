package com.proyecto.citas_proyecto.Controller;

import com.proyecto.citas_proyecto.BaseDatos.Conexion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtPasswordReg;
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPasswordLogin;

    @FXML
    public void registrar() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtPasswordReg.getText();
        if(nombre.isEmpty() ||
                apellido.isEmpty() ||
                correo.isEmpty() ||
                contrasena.isEmpty()){
            mensaje(
                    "Campos vacíos",
                    "Complete todos los datos",
                    Alert.AlertType.WARNING
            );
            return;
        }
        String sql = "INSERT INTO usuario(nombre, apellido, correo, contrasena) VALUES (?, ?, ?, ?)";
        try(Connection cn = Conexion.getConexion();
            PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, correo);
            ps.setString(4, contrasena);
            ps.executeUpdate();
            mensaje(
                    "Registro exitoso",
                    "Usuario creado correctamente",
                    Alert.AlertType.INFORMATION
            );
            txtNombre.clear();
            txtApellido.clear();
            txtCorreo.clear();
            txtPasswordReg.clear();
        }catch(Exception e){
            mensaje(
                    "Error",
                    e.getMessage(),
                    Alert.AlertType.ERROR
            );

        }

    }

    @FXML
    public void login(){
        String correo = txtUsuario.getText();
        String contrasena = txtPasswordLogin.getText();
        if(correo.isEmpty() || contrasena.isEmpty()){
            mensaje(
                    "Campos vacíos",
                    "Ingrese correo y contraseña",
                    Alert.AlertType.WARNING
            );
            return;
        }
        String sql = "SELECT * FROM usuario WHERE correo=? AND contrasena=?";
        try(Connection cn = Conexion.getConexion();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){

                String nombre = rs.getString("nombre");
                mensaje(
                        "Bienvenido",
                        "Inicio correcto\nUsuario: " + nombre,
                        Alert.AlertType.INFORMATION
                );
            }else{
                mensaje(
                        "Error",
                        "Correo o contraseña incorrectos",
                        Alert.AlertType.ERROR
                );

            }

        }catch(Exception e){
            mensaje(
                    "Error BD",
                    e.getMessage(),
                    Alert.AlertType.ERROR
            );

        }

    }

    private void mensaje(String titulo, String texto, Alert.AlertType tipo){


        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(texto);
        alerta.showAndWait();


    }

}