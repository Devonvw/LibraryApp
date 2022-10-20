package com.example.eindopdracht.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class UserFeedback {
    public static void setErrorMsg(Label label, String msg) {
        label.setText(msg);
        label.setStyle("-fx-background-color: rgba(220, 38, 38, 0.2); -fx-text-fill: red;");
        resetMsg(label);
    }
    public static void setSuccessMsg(Label label, String msg) {
        label.setText(msg);
        label.setStyle("-fx-background-color: rgba(110, 136, 13, 0.2); -fx-text-fill: #15803d;");
        resetMsg(label);
    }
    public static void resetMsg(Label label) {
        Timeline tm = new Timeline(new KeyFrame(Duration.seconds(3), t -> {
            label.setText("");
            label.setStyle("");
        }));
        tm.setCycleCount(1);
        tm.play();
    }
}
