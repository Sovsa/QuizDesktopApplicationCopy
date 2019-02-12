package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.QuestionAnswers;
import com.bananpiren.quiz.Entity.QuizQuestions;
import com.bananpiren.quiz.Services.AnswerService;
import com.bananpiren.quiz.Services.QuestionService;

import com.bananpiren.quiz.java.model.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

import static com.bananpiren.quiz.Services.QuestionService.findQuestion;

/**
 * This is a controller class that handles edits of questions
 */

public class EditQuestionDialogController {

    private final ObservableList<QuestionAnswers> answers = FXCollections.observableArrayList();

    private int selectedQuestion;

    private TextField[] answerTextField;
    private CheckBox[] answerCheckbox;
    private RadioButton[] radioButtonAnswer;

    private QuizQuestions question;

    @FXML
    private Button exitButton;

    @FXML
    private VBox questionsVBox;

    @FXML
    private TextField questionTextArea;

    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {

        // Get current selected question to handle
        selectedQuestion = EditQuizDialogController.getSelectedQuestion();

        // Get question information from database and print to textField
        loadQuestion();

        // Get answers for the corresponding question from database and print to textField
        loadAnswers();

        // Save button
        saveButton.setOnAction(e -> {
            updateQuestion();
            updateAnswers();
            Alerts.informationAlert("Succe", "Fråga uppdaterad", "");
            closeWindow();
        });

        // Exit button
        exitButton.setOnAction(e -> closeWindow());
    }

    private void updateQuestion() {
        QuestionService.update(selectedQuestion, questionTextArea.getText());
    }

    private void updateAnswers() {
        int correctAnswer;
        for(int i = 0; i < answers.size(); i++) {
            switch(answers.get(i).getQuestion().getQuestionType()) {
                case "single":
                    if(radioButtonAnswer[i].isSelected()) {
                        correctAnswer = 1;
                    } else {
                        correctAnswer = 0;
                    }
                    break;
                case "multiple":
                    if(answerCheckbox[i].isSelected()) {
                        correctAnswer = 1;
                    } else {
                        correctAnswer = 0;
                    }
                    break;
                default:
                    correctAnswer = 0;
                    break;
            }
            AnswerService.update(answers.get(i).getAnswerId(), answerTextField[i].getText(), correctAnswer);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    // Load selected quiz and set variables
    private void loadQuestion() {
        question = findQuestion(selectedQuestion);

        questionTextArea.setText(question.getQuestion());
    }

    private void loadAnswers() {
        // Get answers from selected question
        List<QuestionAnswers> tempAnswers = AnswerService.read(selectedQuestion);

        // Add to observablelist
        answers.addAll(tempAnswers);

        ToggleGroup correctAnswerToggleGroup = new ToggleGroup();

        int answerNumber = 1;

        switch(question.getQuestionType()) {
            case "single":
                answerTextField = new TextField[answers.size()];
                radioButtonAnswer = new RadioButton[answers.size()];

                for (int i = 0; i < answers.size(); i++) {
                    HBox answerHBox = new HBox();
                    Label answerLabel = new Label("Svar: " + answerNumber++);

                    answerTextField[i] = new TextField(answers.get(i).getAnswer());
                    answerTextField[i].setPrefWidth(400);

                    radioButtonAnswer[i] = new RadioButton("Rätt svar");
                    radioButtonAnswer[i].setToggleGroup(correctAnswerToggleGroup);

                    if(answers.get(i).getCorrectAnswer() == 1) {
                        radioButtonAnswer[i].setSelected(true);
                    } else {
                        radioButtonAnswer[i].setSelected(false);
                    }
                    answerHBox.getChildren().addAll(answerLabel, answerTextField[i], radioButtonAnswer[i]);
                    questionsVBox.getChildren().addAll(answerHBox);
                }
                break;
            case "multiple":
                answerTextField = new TextField[answers.size()];
                answerCheckbox = new CheckBox[answers.size()];

                for (int i = 0; i < answers.size(); i++) {
                    HBox answerHBox = new HBox();
                    Label answerLabel = new Label("Svar: " + answerNumber++);

                    answerTextField[i] = new TextField(answers.get(i).getAnswer());
                    answerTextField[i].setPrefWidth(400);

                    answerCheckbox[i] = new CheckBox("Rätt svar");

                    if(answers.get(i).getCorrectAnswer() == 1) {
                        answerCheckbox[i].setSelected(true);
                    } else {
                        answerCheckbox[i].setSelected(false);
                    }
                    answerHBox.getChildren().addAll(answerLabel, answerTextField[i], answerCheckbox[i]);
                    questionsVBox.getChildren().addAll(answerHBox);
                }
                break;
            case "open":
                answerTextField = new TextField[answers.size()];
                for (int i = 0; i < answers.size(); i++) {
                    HBox answerHBox = new HBox();
                    Label answerLabel = new Label("Svar: " + answerNumber++);

                    answerTextField[i] = new TextField(answers.get(i).getAnswer());
                    answerTextField[i].setPrefWidth(400);

                    answerHBox.getChildren().addAll(answerLabel, answerTextField[i]);
                    questionsVBox.getChildren().addAll(answerHBox);
                }
                break;
        }
    }
}