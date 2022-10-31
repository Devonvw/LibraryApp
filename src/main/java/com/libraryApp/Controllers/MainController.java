package com.libraryApp.Controllers;

import com.libraryApp.DAL.ItemDAO;
import com.libraryApp.Model.Item;
import com.libraryApp.Model.UserFeedback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button collectionBtn, lendingReceivingBtn, importItemsBtn, membersBtn;

    @FXML
    private BorderPane mainPane;
    @FXML
    private Label msgLblImport;

    private FileChooser fileChooser;
    private String currentPnl;

    public void onClickNavBtn(ActionEvent e) {
        resetButtons();
        Button btn = (Button)e.getSource();
        btn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        btn.getGraphic().getStyleClass().add("navBtnIconPrimary");

        if (btn.getText().equals("Import items")) {
            importItems();
            if (currentPnl.equals("Collection")) LoadScene(getClass().getResource("/com/libraryApp/Collection.fxml"));
            return;
        }

        currentPnl = btn.getText();

        switch (currentPnl) {
            case "Lending/Receiving":
                LoadScene(getClass().getResource("/com/libraryApp/LendingReceiving.fxml"));
                break;
            case "Collection":
                LoadScene(getClass().getResource("/com/libraryApp/Collection.fxml"));
                break;
            case "Members":
                LoadScene(getClass().getResource("/com/libraryApp/Members.fxml"));
                break;
        }
    }

    private void importItems() {
        resetButtons();
        importItemsBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        importItemsBtn.getGraphic().getStyleClass().add("navBtnIconPrimary");

        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());

        if (selectedFile == null) return;

        try {
            List<Item> items = new ArrayList<>();
            Files.readAllLines(selectedFile.toPath()).stream().skip(1).forEach(line -> {
                items.add(new Item(Integer.parseInt(line.split(";")[0]), line.split(";")[1], line.split(";")[2]));
            });
            ItemDAO.insertItem(items);


        } catch (Exception ex) {
            UserFeedback.setErrorMsg(msgLblImport, ex.getMessage(), false);
        }

    }
    private void resetButtons() {
        membersBtn.getGraphic().getStyleClass().removeAll("navBtnIconPrimary");
        collectionBtn.getGraphic().getStyleClass().removeAll("navBtnIconPrimary");
        lendingReceivingBtn.getGraphic().getStyleClass().removeAll("navBtnIconPrimary");
        importItemsBtn.getGraphic().getStyleClass().removeAll("navBtnIconPrimary");

        membersBtn.setStyle("");
        collectionBtn.setStyle("");
        lendingReceivingBtn.setStyle("");
        importItemsBtn.setStyle("");
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
        fileChooser = new FileChooser();
        lendingReceivingBtn.setStyle("-fx-background-color: white; -fx-text-fill: #0d9488;");
        lendingReceivingBtn.getGraphic().getStyleClass().add("navBtnIconPrimary");
    }
}
