package com.example.eindopdracht;

import com.example.eindopdracht.DAL.Database;
import com.example.eindopdracht.Model.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LendingRecievingController implements Initializable {
    @FXML
    private Label welcomeLbl, msgLblLend, msgLblRecieve;

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
            setSuccessMsg(msgLblLend, "Item is lent");


        }
        catch (NumberFormatException ex){
            setErrorMsg(msgLblLend, "Item code or member identifier doesn't contain a correct number");
        }
        catch (Exception ex) {
            setErrorMsg(msgLblLend, ex.getMessage());
        }

    }
    public void onRecieveClick(ActionEvent e) {
        try{
            if (itemCodeRecieveInput.getText() == null || itemCodeRecieveInput.getText().isEmpty()) throw new Exception("Item code not filled in");

            int itemId = Integer.parseInt(itemCodeRecieveInput.getText());

            Database.recieveItem(itemId);

            itemCodeRecieveInput.setText("");
            setSuccessMsg(msgLblRecieve, "Item recieved");

            Timer tm = new Timer();
            tm.schedule(new TimerTask(){
                public void run(){
                    resetMsg(msgLblRecieve);
                }
            }, 2000L);
        }
        catch (NumberFormatException ex){
            setErrorMsg(msgLblRecieve, "Item code doesn't contain a correct number");
        }
        catch (Exception ex) {
            setErrorMsg(msgLblRecieve, ex.getMessage());
        }
    }

    private void setErrorMsg(Label label, String msg) {
        label.setText(msg);
        label.setStyle("-fx-background-color: rgba(220, 38, 38, 0.2); -fx-text-fill: red;");
        resetMsg(label);
    }
    private void setSuccessMsg(Label label, String msg) {
        label.setText(msg);
        label.setStyle("-fx-background-color: rgba(110, 136, 13, 0.2); -fx-text-fill: #15803d;");
        resetMsg(label);
    }
    private void resetMsg(Label label) {
        Timeline tm = new Timeline(new KeyFrame(Duration.seconds(3), t -> {
            label.setText("");
            label.setStyle("");
        }));
        tm.setCycleCount(1);
        tm.play();
    }
}
