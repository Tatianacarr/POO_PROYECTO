module com.proyecto.citas_proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.proyecto.citas_proyecto.Controller to javafx.fxml;
    exports com.proyecto.citas_proyecto.Controller;

    opens com.proyecto.citas_proyecto.App to javafx.fxml;
    exports com.proyecto.citas_proyecto.App;
}