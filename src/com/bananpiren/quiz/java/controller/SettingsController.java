package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Services.UserService;
import com.bananpiren.quiz.java.model.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * This is a controller class that handles the settings page
 */

public class SettingsController {
    private UserService userService = new UserService();
    private LoginController loginController = new LoginController();
    private Alerts alerts = new Alerts();

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private Button usernameBtn;

    public SettingsController() {
    }

    @FXML
    private void initialize(){
        emailTextField.setText(loginController.getCurrentUser().getEmail());

        usernameBtn.setOnAction(event ->{
            String regexMail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            StringBuilder warnings = new StringBuilder();

            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();

            if (email.isEmpty()) {
                email = loginController.getCurrentUser().getEmail();
            }else if(!email.matches(regexMail)) {
                warnings.append("Mailadress har fel format!\n" + "Rätt format är xxx@xxx.xx\n");
            } else {
               emailTextField.getText();
            }
            if(password.isEmpty()){
                password = loginController.getCurrentUser().getPassword();
            }else if(!password.matches(confirmPassword)){
                warnings.append("Lösenord matchar inte!");
            }else{
                passwordTextField.getText();
                confirmPasswordTextField.getText();
            }

            if(warnings.length() > 0) {
                alerts.errorAlert(warnings);
            } else {
                userService.updateUser(loginController.getCurrentUser().getUserId(), loginController.getCurrentUser().getFirstName(), loginController.getCurrentUser().getLastName(), email, password, loginController.getCurrentUser().getAccountLevel());
                loginController.reloadCurrentUser();

                emailTextField.setText(loginController.getCurrentUser().getEmail());
                passwordTextField.setText("");
                confirmPasswordTextField.setText("");

                alerts.informationAlert("Succe!", "Användare redigerad", null);
            }
        });
    }
}