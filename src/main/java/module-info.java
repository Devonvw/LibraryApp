module com.example.eindopdracht {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.eindopdracht to javafx.fxml;
    opens com.example.eindopdracht.Model to javafx.fxml;

    exports com.example.eindopdracht;
    exports com.example.eindopdracht.Model;

}