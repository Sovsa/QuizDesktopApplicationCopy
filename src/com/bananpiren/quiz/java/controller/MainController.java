package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.java.view.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is a controller class that handles the start of the program and loads in the GUI
 */

public class MainController {
    private Main main = new Main();

    @FXML
    private Button buttonSettings;

    @FXML
    private Button buttonCorrectQuiz;

    @FXML
    private Button buttonUsers;

    @FXML
    private Button buttonStatistics;

    @FXML
    private Button buttonCreateQuiz;

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonResults;

    @FXML
    private Button buttonEditQuiz;

    @FXML
    private Button buttonLogOut;

    @FXML
    private Button buttonSocketClient;

    @FXML
    private void initialize() {
        if (LoginController.getCurrentUser().getAccountLevel().equals("User")) {
            buttonCreateQuiz.setManaged(false);
            buttonEditQuiz.setManaged(false);
            buttonUsers.setManaged(false);
            buttonStatistics.setManaged(false);
            buttonCorrectQuiz.setManaged(false);
        }

        // Start button
        buttonStart.setOnAction(e -> {
            showHome();

        });

        // Create Quiz Button
        buttonCreateQuiz.setOnAction(e -> {
            showCreateQuiz();
        });

        // Edit Quiz button
        buttonEditQuiz.setOnAction(e -> {
            showEditQuiz();
        });

        // Results Button
        buttonResults.setOnAction(e -> {
            showResults();
        });

        // Users Button
        buttonUsers.setOnAction(e -> {
            showUsers();
        });

        // Statistics Button
        buttonStatistics.setOnAction(e -> {
            showStatistics();
        });

        // Settings Button
        buttonSettings.setOnAction(e -> {
            showSettings();
        });

        // Quiz Battle Button
        buttonSocketClient.setOnAction(e -> {
            showSocketClient();
        });

        //Correct quiz button
        buttonCorrectQuiz.setOnAction(e ->{
            showCorrectQuiz();
        });

        // Log out button
        buttonLogOut.setOnAction(event -> {
            showLogin();
        });

        // Exit button
        buttonExit.setOnAction(e -> {
            System.exit(1);
        });
    }

    @FXML
    public void showHome() {
        try {
            main.showStart();
        } catch (IOException e) {
            System.out.println("Couldn't show Home"+e);
        }
    }

    @FXML
    private void showCreateQuiz() {
        try {
            main.showCreateQuiz();
        } catch (IOException e) {
            System.out.println("Couldn't show Create Quiz");
        }
    }

    @FXML
    private void showEditQuiz() {
        try {
            main.showEditQuiz();
        } catch (IOException e) {
            System.out.println("Couldn't show Edit Quiz");
        }
    }

    @FXML
    private void showResults() {
        try {
            main.showResults();
        } catch (IOException e) {
            System.out.println("Couldn't show Results");
        }
    }

    @FXML
    private void showUsers() {
        try {
            main.showUsers();
        } catch (IOException e) {
            System.out.println("Couldn't show Users");
        }
    }

    @FXML
    private void showSocketClient() {
        try {
            main.showSocketClient();
        } catch (IOException e) {
            System.out.println("Couldn't show Quiz Battle");
        }
    }

    @FXML
    private void showStatistics() {
        try {
            main.showStatistics();
        } catch (IOException e) {
            System.out.println("Couldn't show Statistics"+e);
        }
    }

    @FXML
    private void showSettings() {
        try {
            main.showSettings();
        } catch (IOException e) {
            System.out.println("Couldn't show Settings");
        }
    }

    @FXML
    private void showLogin() {
        try {
            main.showLoginScreen();
            Stage stage = (Stage) buttonLogOut.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            System.out.println("Couldn't log out");
        }
    }

    @FXML
    private void showCorrectQuiz(){
        try {
            main.showCorrectQuiz();
        }catch (IOException e){
            System.out.println("Couldn't show Correct Quiz");
        }
    }
}