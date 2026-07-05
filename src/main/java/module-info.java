module com.proyecto.citas_proyecto {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.proyecto.citas_proyecto to javafx.fxml;
    exports com.proyecto.citas_proyecto;
}