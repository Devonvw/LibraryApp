package com.libraryApp.Controllers;

import com.libraryApp.Model.Item;
import com.libraryApp.DAL.Database;
import com.libraryApp.Model.DialogMode;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class CollectionController implements Initializable {
    @FXML
    private TableView<Item> collectionTbl;
    @FXML
    private TableColumn<Item, String> authorCol;

    @FXML
    private TableColumn<Item, Boolean> availableCol;

    @FXML
    private TableColumn<Item, Integer> itemCodeCol;

    @FXML
    private TableColumn<Item, String> titleCol;

    @FXML
    private TableColumn<Item, String> deleteCol;
    @FXML
    private TableColumn<Item, String> editCol;
    @FXML
    private Label welcomeLbl;
    @FXML
    private TextField searchInput;
    @FXML
    private Button addBtn;

    private List<Item> items;
    public final ObservableList<Item> itemsObservableArray = FXCollections.observableArrayList();

    public CollectionController() {
        try {
            items = Database.loadItems();
            itemsObservableArray.setAll(FXCollections.observableArrayList(items));
        } catch (Exception ex) {

        }
    }

    public ObservableList<Item> getItemsObservableArray() {
        return itemsObservableArray;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addBtn.setOnAction(e -> onAddUpdateClick(e, new Item()));

            availableCol.setCellFactory(tc -> new TableCell<Item, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null :
                            item.booleanValue() ? "Yes" : "No");
                }
            });

            deleteCol.setCellFactory(tc -> new TableCell<Item, String>() {
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
                                Item itemClicked = getTableView().getItems().get(getIndex());
                                Database.deleteItem(items, itemClicked.getId());

                                items = Database.loadItems();
                                itemsObservableArray.setAll(FXCollections.observableArrayList(items));
                            }
                            catch (Exception ex) {}
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            });

            editCol.setCellFactory(tc -> new TableCell<Item, String>() {
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
                            Item itemClicked = getTableView().getItems().get(getIndex());
                            System.out.println(itemClicked.getTitle());
                            onAddUpdateClick(e, itemClicked);
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            });

            itemCodeCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));
            availableCol.setCellValueFactory(new PropertyValueFactory<Item, Boolean>("available"));
            titleCol.setCellValueFactory(new PropertyValueFactory<Item, String>("title"));
            authorCol.setCellValueFactory(new PropertyValueFactory<Item, String>("author"));
            deleteCol.setCellValueFactory(new PropertyValueFactory<>(""));
            editCol.setCellValueFactory(new PropertyValueFactory<>(""));

            searchInput.textProperty().addListener((obs, oldText, newText) -> {
                itemsObservableArray.setAll(items.stream().filter(i -> i.getTitle().toLowerCase().contains(newText.toLowerCase()) || i.getAuthor().toLowerCase().contains(newText.toLowerCase())).collect(Collectors.toList()));
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void onAddUpdateClick(ActionEvent e, Item item) {
        DialogMode mode;
        String dialogTitle = "";

        if (item.getId() == 0) {
            mode = DialogMode.ADD;
            dialogTitle = "Add item";
        }
        else {
            mode = DialogMode.UPDATE;
            dialogTitle = "Update item";
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/libraryApp/CollectionModal.fxml"));
            DialogPane modal = loader.load();

            CollectionModalController modalController = loader.getController();
            modalController.setItem(item, mode);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(modal);
            dialog.setTitle(dialogTitle);

            Optional<ButtonType> clickedBtn = dialog.showAndWait();

            if (clickedBtn.get() == ButtonType.OK) {
                if (mode == DialogMode.ADD) Database.insertItemWithoutId(items, modalController.item);
                else Database.updateItem(items, modalController.item);

                items = Database.loadItems();
                itemsObservableArray.setAll(FXCollections.observableArrayList(items));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}