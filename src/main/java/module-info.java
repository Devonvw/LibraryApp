module com.libraryApp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.libraryApp to javafx.fxml;
    opens com.libraryApp.Model to javafx.fxml;

    exports com.libraryApp;
    exports com.libraryApp.Model;
    exports com.libraryApp.Controllers;
    opens com.libraryApp.Controllers to javafx.fxml;

}