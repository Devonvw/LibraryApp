package com.example.eindopdracht;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

public class MainController {
    @FXML
    public Button lendingRecievingBtn;
    @FXML
    private Button collectionBtn;
    @FXML
    private Button membersBtn;

    @FXML
    private BorderPane mainPane;

    public void onClickLendingRecieving(ActionEvent e) {
        resetButtons();
        lendingRecievingBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        LoadScene(getClass().getResource("LendingRecieving.fxml"));
    }
    public void onClickCollection(ActionEvent e) {
        resetButtons();
        collectionBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        LoadScene(getClass().getResource("Collection.fxml"));
    }
    public void onClickMembers(ActionEvent e) {
        resetButtons();
        membersBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        LoadScene(getClass().getResource("Members.fxml"));
    }
    private void resetButtons(){
        membersBtn.setStyle("");
        collectionBtn.setStyle("");
        lendingRecievingBtn.setStyle("");
    }
    private void LoadScene(URL url) {
        try {
            mainPane.setCenter(FXMLLoader.load(url));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
