package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Services.UserService;
import com.bananpiren.quiz.java.model.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Input dialog for add a new user to the database
 */

public class PersonAddDialogController {
    private UserService userService = new UserService();
    private Alerts alerts = new Alerts();

    private ObservableList<String> userLevel = FXCollections.observableArrayList("User", "Admin");

    @FXML
    private TextField mailTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private ChoiceBox<String> userLevelChoiceBox;

    @FXML
    private Button saveButton;

    @FXML
    private TextField firstNameTextfield;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private void initialize() {
        userLevelChoiceBox.setItems(userLevel);
        userLevelChoiceBox.getSelectionModel().select(0);

        saveButton.setOnAction(e -> {
            String regexMail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            String firstName = firstNameTextfield.getText();
            String lastName = lastNameTextField.getText();
            String userMail = mailTextField.getText();
            String password = passwordTextField.getText();

            StringBuilder warnings = new StringBuilder();

            // Validate input
            if (firstName == null || firstName.trim().isEmpty()) {
                warnings.append("Förnamn är inte ifyllt!\n");
            } else {
                firstNameTextfield.getText();
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                warnings.append("Efternamn är inte ifyllt!\n");
            } else {
                lastNameTextField.getText();
            }
            if (userMail == null || userMail.trim().isEmpty()) {
                warnings.append("Mailadress är inte ifyllt!\n");
            }
            if(!userMail.matches(regexMail)) {
                warnings.append("Mailadress har fel format!\n" + "Rätt format är xxx@xxx.xx\n");
            } else {
                mailTextField.getText();
            }
            if(password == null || password.trim().isEmpty()) {
                warnings.append("Lösenord är inte ifyllt!\n");
            }else {
                passwordTextField.getText();
            }

            if(warnings.length() > 0) {
                alerts.errorAlert(warnings);
            } else {
                String accountLevel = userLevelChoiceBox.getValue();

                try {
                    userService.createUser(firstName, lastName, userMail, password, accountLevel);

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Succe!");
                    alert.setHeaderText("Användare tillagd");
                    alert.setContentText("Vill du lägga till fler?");

                    ButtonType noButton = new ButtonType("Nej", ButtonBar.ButtonData.CANCEL_CLOSE);
                    ButtonType yesButton = new ButtonType("Ja");

                    alert.getButtonTypes().setAll(yesButton, noButton);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == noButton){
                        Stage stage = (Stage) saveButton.getScene().getWindow();
                        stage.close();
                    } else {
                        firstNameTextfield.setText("");
                        lastNameTextField.setText("");
                        mailTextField.setText("");
                        passwordTextField.setText("");
                        userLevelChoiceBox.getSelectionModel().select(0);
                    }
                } catch (Exception ex) {
                    Alerts.errorAlert("Error Dialog","Något gick fel, din användare skapades inte!", "Försök igen!");
                }
            }
        });

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }
}