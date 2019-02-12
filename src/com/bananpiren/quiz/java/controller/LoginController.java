package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.User;
import com.bananpiren.quiz.Services.UserService;
import com.bananpiren.quiz.java.view.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Controller for taking user input and check if username and password is in the
 * database and is matching.
 */

public class LoginController {
    @FXML
    private TextField userEmailTextField;

    @FXML
    private PasswordField userPasswordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label informationOutputTextField;

    @FXML
    private BorderPane loginBordePane;

    private MainController mainController = new MainController();
    private Stage primaryStage = new Stage();
    private UserService userService = new UserService();
    private Main main = new Main();

    private static User currentUser;

    @FXML
    private void initialize() {
        userEmailTextField.setText("user@user.se");
        userPasswordTextField.setText("user");

        loginButton.setOnAction(event -> checkPassword());
    }

    public void onEnter(ActionEvent ae){
        checkPassword();
    }

    private void checkPassword() {
        String storedUserEmail = userEmailTextField.getText();
        String storeUserPassword = userPasswordTextField.getText();

        try {
            currentUser = userService.findUserByEmail(storedUserEmail);
            if (storeUserPassword.equals(currentUser.getPassword())) {
                main.showMainView(primaryStage);
                mainController.showHome();
            } else {
                informationOutputTextField.setText("Felaktigt mail och/eller lösenord");
            }
        } catch (Exception e) {
            informationOutputTextField.setText("Felaktigt mail och/eller lösenord");
            System.out.println("Incorrect");
        }
    }

    //Method to get the current logged in user.
    public static User getCurrentUser() {
        return currentUser;
    }

    //Method to reload current user from database if changes to it were made.
    void reloadCurrentUser() {
        LoginController.currentUser = userService.findUserById(currentUser.getUserId());
    }
}