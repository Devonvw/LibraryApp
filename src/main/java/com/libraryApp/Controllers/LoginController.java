package com.libraryApp.Controllers;

import com.libraryApp.DAL.Database;
import com.libraryApp.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Label msgLabel;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    private Database database;

    public LoginController() {
        database = new Database();
    }

    public void onLoginButtonClick(ActionEvent e) {
        try {
            database.login(new User(usernameInput.getText(), passwordInput.getText()));

            Stage loginStage = (Stage)msgLabel.getScene().getWindow();
            loginStage.close();

            //Load main window
            Stage mainStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/libraryApp/Main.fxml"));
            mainStage.setScene(new Scene(root));
            mainStage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            msgLabel.setText(ex.getMessage());
        }
    }
}
