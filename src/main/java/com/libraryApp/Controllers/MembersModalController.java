package com.libraryApp.Controllers;

import com.libraryApp.Model.DialogMode;
import com.libraryApp.Model.User;
import com.libraryApp.Model.UserFeedback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class MembersModalController implements Initializable {
    @FXML
    public DialogPane dialogPane;
    @FXML
    private Label msgLabel;
    @FXML
    private TextField firstNameInput, lastNameInput;
    @FXML
    private DatePicker birthDateInput;
    public User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogPane.setExpandableContent(null);
        dialogPane.lookupButton(ButtonType.OK).getStyleClass().add("outlineBtn");
        dialogPane.lookupButton(ButtonType.CANCEL).getStyleClass().add("whiteBtn");
    }

    public void setUser(User user, DialogMode mode) {
        this.user = user;
        firstNameInput.setText(user.getFirstname());
        lastNameInput.setText(user.getLastname());
        birthDateInput.setValue(user.getBirthdate());

        Button okBtn = (Button) dialogPane.lookupButton(ButtonType.OK);
        okBtn.addEventFilter(ActionEvent.ACTION, e -> {
            if (!validateInput()) {
                e.consume();
            } else {
                this.user.setFirstname(firstNameInput.getText());
                this.user.setLastname(lastNameInput.getText());
                if ((birthDateInput.getEditor().getText() == null || birthDateInput.getEditor().getText().isEmpty()))
                    this.user.setBirthdate(birthDateInput.getValue());
                else
                    this.user.setBirthdate(birthDateInput.getConverter().fromString(birthDateInput.getEditor().getText()));
            }
        });
    }

    private boolean validateInput() {
        System.out.println(birthDateInput.getValue());

        try {
            if ((firstNameInput.getText() == null || firstNameInput.getText().isEmpty())) {
                UserFeedback.setErrorMsg(msgLabel, "First name cannot be empty!");
                return false;
            } else if ((lastNameInput.getText() == null || lastNameInput.getText().isEmpty())) {
                UserFeedback.setErrorMsg(msgLabel, "Last name cannot be empty!");
                return false;
            } else if (birthDateInput.getValue() == null) {
                if ((birthDateInput.getEditor().getText() == null || birthDateInput.getEditor().getText().isEmpty())) {
                    UserFeedback.setErrorMsg(msgLabel, "Birth date cannot be empty!");
                    return false;
                } else if (birthDateInput.getConverter().fromString(birthDateInput.getEditor().getText()) == null) {
                    UserFeedback.setErrorMsg(msgLabel, "Birth date is not in correct format!");
                    return false;
                }
            } else if ((birthDateInput.getEditor().getText() == null || birthDateInput.getEditor().getText().isEmpty())) {
                UserFeedback.setErrorMsg(msgLabel, "Birth date cannot be empty!");
                return false;
            }

            UserFeedback.resetMsg(msgLabel);
            return true;
        } catch (DateTimeParseException ex) {
            UserFeedback.setErrorMsg(msgLabel, "Birth date is not in correct format!");
            return false;
        } catch (Exception ex) {
            UserFeedback.setErrorMsg(msgLabel, "Not able to validate input");
            return false;
        }
    }
}
