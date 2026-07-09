package com.proyecto.citas_proyecto.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/com/proyecto/citas_proyecto/Login.fxml")
        );

        Scene scene = new Scene(loader.load(), 900, 550);

        stage.setTitle("Sistema de Citas Médicas");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
