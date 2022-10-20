package com.example.eindopdracht;

import com.example.eindopdracht.Model.DialogMode;
import com.example.eindopdracht.Model.Item;
import com.example.eindopdracht.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class MembersModalController implements Initializable {
    @FXML
    public DialogPane dialogPane;

    @FXML
    private TextField firstNameInput, lastNameInput;
    @FXML
    private DatePicker birthDateInput;
    public User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogPane.setExpandableContent(null);
    }

    public void setUser(User user, DialogMode mode) {
        this.user = user;
        firstNameInput.setText(user.getFirstname());
        lastNameInput.setText(user.getLastname());

        Button okBtn = (Button)dialogPane.lookupButton(ButtonType.OK);
        okBtn.addEventFilter(ActionEvent.ACTION, e -> {
            if (!validateInput()){
                e.consume();
            }
            else {
                this.user.setFirstname(firstNameInput.getText());
                this.user.setLastname(lastNameInput.getText());
            }
        });
    }

    private boolean validateInput() {
        if ((firstNameInput.getText() == null || firstNameInput.getText().isEmpty())) return false;
        else if ((lastNameInput.getText() == null || lastNameInput.getText().isEmpty())) return false;
        return true;
    }
}
