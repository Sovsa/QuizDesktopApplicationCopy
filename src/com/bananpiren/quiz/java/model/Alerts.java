package com.bananpiren.quiz.java.model;

import javafx.scene.control.Alert;

/**
 * Class for controlling and creating alert events.
 */

public class Alerts {

    // Information alert
    public static void informationAlert(String alertTitle, String alertHeader, String alertContent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertContent);

        alert.showAndWait();
    }

    // Error message alert, that takes a warnings strings as parameter.
    public void errorAlert(StringBuilder warnings) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("" + warnings);
        alert.setContentText("Försök igen!");

        alert.showAndWait();
    }

    // Error message alert, that takes all information parameters.
    public static void errorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    // Warning alert that takes all information parameters.
    public static void warningAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}