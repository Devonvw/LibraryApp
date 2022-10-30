package com.libraryApp.Controllers;

import com.libraryApp.DAL.ItemDAO;
import com.libraryApp.DAL.UserDAO;
import com.libraryApp.Model.User;
import com.libraryApp.Model.UserFeedback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LendingReceivingController implements Initializable {
    @FXML
    private Label welcomeLbl, msgLblLend, msgLblReceive;

    @FXML
    private TextField itemCodeLendInput, memberIdLendInput, itemCodeReceiveInput;

    private User user;

    public LendingReceivingController() {
        try {
            user = UserDAO.getLoggedInUser();
        } catch (Exception ex) {

        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        welcomeLbl.setText("Welcome " + user.getUsername());
    }

    public void onLendClick(ActionEvent e) {
        try {
            if (itemCodeLendInput.getText() == null || itemCodeLendInput.getText().isEmpty())
                throw new Exception("Item code not filled in");
            if (memberIdLendInput.getText() == null || memberIdLendInput.getText().isEmpty())
                throw new Exception("Member identifier not filled in");

            int itemId = Integer.parseInt(itemCodeLendInput.getText());
            int memberId = Integer.parseInt(memberIdLendInput.getText());

            itemCodeLendInput.setText("");
            memberIdLendInput.setText("");

            ItemDAO.lendItem(itemId, memberId);

            UserFeedback.setSuccessMsg(msgLblLend, "Item is lent");
        } catch (NumberFormatException ex) {
            UserFeedback.setErrorMsg(msgLblLend, "Item code or member identifier doesn't contain a correct number");
        } catch (Exception ex) {
            UserFeedback.setErrorMsg(msgLblLend, ex.getMessage());
        }

    }

    public void onReceiveClick(ActionEvent e) {
        try {
            if (itemCodeReceiveInput.getText() == null || itemCodeReceiveInput.getText().isEmpty())
                throw new Exception("Item code not filled in");

            int itemId = Integer.parseInt(itemCodeReceiveInput.getText());

            itemCodeReceiveInput.setText("");

            ItemDAO.receiveItem(itemId);

            UserFeedback.setSuccessMsg(msgLblReceive, "Item received");
        } catch (NumberFormatException ex) {
            UserFeedback.setErrorMsg(msgLblReceive, "Item code doesn't contain a correct number");
        } catch (Exception ex) {
            UserFeedback.setErrorMsg(msgLblReceive, ex.getMessage());
        }
    }
}
