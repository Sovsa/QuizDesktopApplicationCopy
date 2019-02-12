package com.bananpiren.quiz.java.controller;

/**
 * This is a controller class that handles edits of quizes
 */

import com.bananpiren.quiz.Services.CorrectQuizService;
import com.bananpiren.quiz.java.model.Alerts;
import com.bananpiren.quiz.java.view.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;


public class CorrectQuizEditController {

    @FXML
    private Label quizNameLabel;

    @FXML
    private Button sendPoints;

    @FXML
    private TextField points;

    @FXML
    private void initialize() {

        quizNameLabel.setText("Rätta quiz");

        sendPoints.setOnAction(event -> {
            quizNameLabel.setText("Quizet är rättat");
            int id = CorrectQuizController.getQuizId();
            CorrectQuizService.updatePoints("" + id, Integer.parseInt(points.getText()));
            points.setDisable(true);

            Alerts.informationAlert("Klart", "Provet är rättat och sparat!", "");

            try {
                Main.showStatistics();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
