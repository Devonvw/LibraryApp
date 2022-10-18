package com.example.eindopdracht;

import com.example.eindopdracht.DAL.Database;
import com.example.eindopdracht.Model.Item;
import com.example.eindopdracht.Model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class MembersController implements Initializable {
    @FXML
    private TableView<User> membersTbl;
    @FXML
    private TableColumn<User, LocalDate> birthDateCol;
    @FXML
    private TableColumn<User, String> firstNameCol;
    @FXML
    private TableColumn<User, Integer> identifierCol;
    @FXML
    private TableColumn<User, String> lastNameCol;
    @FXML
    private TableColumn<User, String> deleteCol;
    @FXML
    private TableColumn<User, String> editCol;
    @FXML
    private Label welcomeLbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<User> users = Database.loadUsers();

            deleteCol.setCellFactory(tc -> new TableCell<User, String>() {
                final Button btn = new Button("");
                final FontIcon icon = new FontIcon();
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        icon.setIconLiteral("fa-trash-o");
                        icon.setIconColor(Paint.valueOf("red"));
                        btn.setGraphic(icon);
                        btn.getStyleClass().add("iconBtn");
                        btn.setCursor(Cursor.HAND);
                        btn.setOnAction(e -> {
                            User itemClicked = getTableView().getItems().get(getIndex());
                            System.out.println(itemClicked.getUsername());
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            });

            editCol.setCellFactory(tc -> new TableCell<User, String>() {
                final Button btn = new Button("");
                final FontIcon icon = new FontIcon();
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        icon.setIconLiteral("fa-pencil");
                        btn.setGraphic(icon);
                        btn.getStyleClass().add("iconBtn");
                        btn.setCursor(Cursor.HAND);
                        btn.setOnAction(e -> {
                            User itemClicked = getTableView().getItems().get(getIndex());
                            System.out.println(itemClicked.getUsername());
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            });

            identifierCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
            firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));
            birthDateCol.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("birthdate"));
            deleteCol.setCellValueFactory(new PropertyValueFactory<>(""));
            editCol.setCellValueFactory(new PropertyValueFactory<>(""));

            membersTbl.setItems(FXCollections.observableArrayList(users));
        } catch (Exception ex) {

        }
    }
}
