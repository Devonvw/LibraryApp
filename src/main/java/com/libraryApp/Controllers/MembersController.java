package com.libraryApp.Controllers;

import com.libraryApp.DAL.UserDAO;
import com.libraryApp.Model.DialogMode;
import com.libraryApp.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private TextField searchInput;
    @FXML
    private Button addBtn;

    private List<User> users;
    public final ObservableList<User> usersObservableArray = FXCollections.observableArrayList();

    public MembersController() {
        try {
            users = UserDAO.loadUsers();
            usersObservableArray.setAll(FXCollections.observableArrayList(users));
        } catch (Exception ex) {

        }
    }

    public ObservableList<User> getUsersObservableArray() {
        return usersObservableArray;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBtn.setOnAction(e -> onAddUpdateClick(e, new User()));
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
                        try {
                            User userClicked = getTableView().getItems().get(getIndex());
                            UserDAO.deleteUser(users, userClicked.getId());

                            users = UserDAO.loadUsers();
                            usersObservableArray.setAll(FXCollections.observableArrayList(users));
                        } catch (Exception ex) {
                        }
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
                        User userClicked = getTableView().getItems().get(getIndex());
                        onAddUpdateClick(e, userClicked);
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

        searchInput.textProperty().addListener((obs, oldText, newText) -> {
            usersObservableArray.setAll(users.stream().filter(i -> i.getFirstname().toLowerCase().contains(newText.toLowerCase()) || i.getLastname().toLowerCase().contains(newText.toLowerCase())).collect(Collectors.toList()));
        });
    }

    public void onAddUpdateClick(ActionEvent e, User user) {
        DialogMode mode;
        String dialogTitle = "";

        if (user.getId() == 0) {
            mode = DialogMode.ADD;
            dialogTitle = "Add user";
        } else {
            mode = DialogMode.UPDATE;
            dialogTitle = "Update user";
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/libraryApp/MembersModal.fxml"));
            DialogPane modal = loader.load();

            MembersModalController modalController = loader.getController();
            modalController.setUser(user, mode);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(modal);
            dialog.setTitle(dialogTitle);

            Optional<ButtonType> clickedBtn = dialog.showAndWait();

            if (clickedBtn.get() == ButtonType.OK) {
                if (mode == DialogMode.ADD) UserDAO.insertUserWithoutId(users, modalController.user);
                else UserDAO.updateUser(users, modalController.user);

                users = UserDAO.loadUsers();
                usersObservableArray.setAll(FXCollections.observableArrayList(users));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
