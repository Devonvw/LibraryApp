package com.libraryApp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public Button lendingReceivingBtn;
    @FXML
    private Button collectionBtn;
    @FXML
    private Button membersBtn;

    @FXML
    private BorderPane mainPane;

    public void onClickLendingReceiving(ActionEvent e) {
        resetButtons();
        lendingReceivingBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        lendingReceivingBtn.getGraphic().getStyleClass().add("navBtnIconPrimary");
        LoadScene(getClass().getResource("/com/libraryApp/LendingReceiving.fxml"));
    }

    public void onClickCollection(ActionEvent e) {
        resetButtons();
        collectionBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        collectionBtn.getGraphic().getStyleClass().add("navBtnIconPrimary");
        LoadScene(getClass().getResource("/com/libraryApp/Collection.fxml"));
    }

    public void onClickMembers(ActionEvent e) {
        resetButtons();
        membersBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        membersBtn.getGraphic().getStyleClass().add("navBtnIconPrimary");
        LoadScene(getClass().getResource("/com/libraryApp/Members.fxml"));
    }

    private void resetButtons() {
        membersBtn.getGraphic().getStyleClass().removeAll("navBtnIconPrimary");
        collectionBtn.getGraphic().getStyleClass().removeAll("navBtnIconPrimary");
        lendingReceivingBtn.getGraphic().getStyleClass().removeAll("navBtnIconPrimary");

        membersBtn.setStyle("");
        collectionBtn.setStyle("");
        lendingReceivingBtn.setStyle("");
    }

    private void LoadScene(URL url) {
        try {
            mainPane.setCenter(FXMLLoader.load(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lendingReceivingBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        lendingReceivingBtn.getGraphic().getStyleClass().add("navBtnIconPrimary");
    }
}
