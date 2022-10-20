package com.example.eindopdracht;

import com.example.eindopdracht.Model.DialogMode;
import com.example.eindopdracht.Model.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CollectionModalController implements Initializable {
    @FXML
    public DialogPane dialogPane;

    @FXML
    private TextField titleInput, authorInput;
    public Item item;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogPane.setExpandableContent(null);
    }

    public void setItem(Item item, DialogMode mode) {
        this.item = item;
        titleInput.setText(item.getTitle());
        authorInput.setText(item.getAuthor());

        Button okBtn = (Button)dialogPane.lookupButton(ButtonType.OK);
        okBtn.addEventFilter(ActionEvent.ACTION, e -> {
            if (!validateInput()){
                e.consume();
            }
            else {
                this.item.setTitle(titleInput.getText());
                this.item.setAuthor(authorInput.getText());
            }
        });
    }

    private boolean validateInput() {
        if ((titleInput.getText() == null || titleInput.getText().isEmpty())) return false;
        else if ((authorInput.getText() == null || authorInput.getText().isEmpty())) return false;
        return true;
    }
}
