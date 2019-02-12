package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.User;
import com.bananpiren.quiz.Services.UserService;
import com.bananpiren.quiz.java.model.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Input dialog for updating a user selected from menu in previous view.
 */

public class PersonEditDialogController {
    private ObservableList<String> userLevel = FXCollections
            .observableArrayList("User", "Admin");

    private ObservableList<User> data = FXCollections.observableArrayList();

    private int storedUserId;
    private int storedUserTableIndex;
    private String storedFirstName;
    private String storedLastName;
    private String storedEmail;
    private String storedAccountLevel;

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
    private void initialize() {
        UserService userService = new UserService();
        UsersController usersController = new UsersController();

        this.storedUserId = usersController.getStoredUserId();
        this.storedUserTableIndex = usersController.getStoredSelectedTableIndex();
        this.data = usersController.getData();

        userLevelChoiceBox.setItems(userLevel);
        if (data.get(storedUserTableIndex).getAccountLevel().equals("User")) {
            userLevelChoiceBox.getSelectionModel().select(0);
        } else {
            userLevelChoiceBox.getSelectionModel().select(1);
        }

        firstNameTextfield.setText(data.get(storedUserTableIndex).getFirstName());
        lastNameTextField.setText(data.get(storedUserTableIndex).getLastName());
        mailTextField.setText(data.get(storedUserTableIndex).getEmail());

        firstNameTextfield.setPromptText(data.get(storedUserTableIndex).getFirstName());
        lastNameTextField.setPromptText(data.get(storedUserTableIndex).getLastName());
        mailTextField.setPromptText(data.get(storedUserTableIndex).getEmail());

        saveButton.setOnAction((ActionEvent e) -> {
            if (firstNameTextfield != null) {
                this.storedFirstName = firstNameTextfield.getText();
            } else {
                this.storedFirstName = data.get(storedUserTableIndex).getFirstName();
            }
            if (lastNameTextField != null) {
                this.storedLastName = lastNameTextField.getText();
            } else {
                this.storedLastName = data.get(storedUserTableIndex).getLastName();
            }
            if (mailTextField != null) {
                this.storedEmail = mailTextField.getText();
            } else {
                this.storedEmail = data.get(storedUserTableIndex).getEmail();
            }

            this.storedAccountLevel = userLevelChoiceBox.getValue();

            userService.updateUser(storedUserId, storedFirstName, storedLastName, storedEmail, data.get(storedUserTableIndex).getPassword(), storedAccountLevel);

            Alerts.informationAlert("Användare uppdaterad!", null, "Användaren har blivit korrekt uppdaterad");

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }
}
