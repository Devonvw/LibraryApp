package com.example.eindopdracht;

import com.example.eindopdracht.DAL.Database;
import com.example.eindopdracht.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LendingRecievingController implements Initializable {
    @FXML
    private Label welcomeLbl;

    @FXML
    private TextField itemCodeLendInput, memberIdLendInput, itemCodeRecieveInput;

    private User user;
    public LendingRecievingController() {
        try {
            user = Database.getLoggedInUser();
        } catch (Exception ex) {

        }
    }
    @FXML
    public void initialize(URL url, ResourceBundle rb){
        welcomeLbl.setText("Welcome " + user.getUsername());
    }

    public void onLendClick(ActionEvent e) {
        try{
            if (itemCodeLendInput.getText() == null || itemCodeLendInput.getText().isEmpty()) throw new Exception("Item code not filled in");
            if (memberIdLendInput.getText() == null || memberIdLendInput.getText().isEmpty()) throw new Exception("Member identifier not filled in");

            int itemId = Integer.parseInt(itemCodeLendInput.getText());
            int memberId = Integer.parseInt(memberIdLendInput.getText());

            Database.lendItem(itemId, memberId);

            itemCodeLendInput.setText("");
            memberIdLendInput.setText("");
        }
        catch (NumberFormatException ex){
            System.out.println("Item code or member identifier doesn't contain a correct number");
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
    public void onRecieveClick(ActionEvent e) {
        try{
            if (itemCodeRecieveInput.getText() == null || itemCodeRecieveInput.getText().isEmpty()) throw new Exception("Item code not filled in");

            int itemId = Integer.parseInt(itemCodeRecieveInput.getText());

            Database.recieveItem(itemId);

            itemCodeRecieveInput.setText("");
        }
        catch (NumberFormatException ex){
            System.out.println("Item code doesn't contain a correct number");
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
