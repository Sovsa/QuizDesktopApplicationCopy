package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.Quiz;
import com.bananpiren.quiz.Entity.QuizQuestions;
import com.bananpiren.quiz.Services.QuestionService;
import com.bananpiren.quiz.Services.QuizService;
import com.bananpiren.quiz.java.model.Alerts;
import com.bananpiren.quiz.java.view.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * This is a controller class that handles edits of quizes and questions
 */

public class EditQuizDialogController {

    private ObservableList<QuizQuestions> questions = FXCollections.observableArrayList();

    private static int currentQuiz;
    private static int selectedQuestion;

    private String quizName;
    private int timeLimit;
    private String quizStartDate;
    private String quizEndDate;
    private String selfcorrecting;
    private String showSelfcorrecting;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker endDateDatePicker;

    @FXML
    private TextField timeLimitTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button editQuestionButton;

    @FXML
    private Button deleteQuestionButton;

    @FXML
    private TextField quizNameTextField;

    @FXML
    private DatePicker startDateDatePicker;

    @FXML
    private CheckBox selfCorrectingCheckBox;

    @FXML
    private CheckBox showSelfCorrectingCheckBox;

    @FXML
    private TableColumn<QuizQuestions, String> questionColumn;

    @FXML
    public TableView<QuizQuestions> questionsTable;

    public EditQuizDialogController() {
    }

    @FXML
    private void initialize() {
        // Set table columns
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));

        // Get selected quizId
        currentQuiz = EditQuizController.getStoredQuizId();

        // Load current quiz from database
        loadQuiz();

        // Set quiz properties to corresponding fields
        setQuizPropertiesToFields();

        // Load selected quiz questions and set table
        loadTableData();

        questionsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(questionsTable.getSelectionModel().getSelectedItem() == null) {
                questionsTable.setPlaceholder(new Label("Det finns inga sparade frågor till detta quiz"));
                editQuestionButton.setDisable(true);
                deleteQuestionButton.setDisable(true);
            } else if (questionsTable.getSelectionModel().getSelectedItem() != null){
                editQuestionButton.setDisable(false);
                deleteQuestionButton.setDisable(false);
                selectedQuestion = questionsTable.getSelectionModel().getSelectedItem().getQuestionId();
                System.out.println(selectedQuestion);
            }
        });

        // Check if self correcting is checked initial
        if (selfCorrectingCheckBox.isSelected()) {
            showSelfCorrectingCheckBox.setDisable(false);
        } else {
            showSelfCorrectingCheckBox.setDisable(true);
            showSelfCorrectingCheckBox.setSelected(false);
        }

        // Check if self correcting is checked listener
        selfCorrectingCheckBox.setOnAction(e -> {
            if (selfCorrectingCheckBox.isSelected()) {
                showSelfCorrectingCheckBox.setDisable(false);
            } else {
                showSelfCorrectingCheckBox.setDisable(true);
                showSelfCorrectingCheckBox.setSelected(false);
            }
        });

        // Edit button
        editQuestionButton.setOnAction(e -> showEditQuizDialog());

        // Delete button
        deleteQuestionButton.setOnAction(e -> {
            QuestionService.deleteQuestion(selectedQuestion);
            int selectedIndex = questionsTable.getSelectionModel().getSelectedIndex();
            questionsTable.getItems().remove(selectedIndex);
        });

        // Save button
        saveButton.setOnAction(e -> {
            String selfCorrectingCheckBoxValue;
            if (selfCorrectingCheckBox.isSelected()) {
                selfCorrectingCheckBoxValue = "yes";
            } else {
                selfCorrectingCheckBoxValue = "no";
            }

            String showSelfCorrectingCheckBoxValue;
            if (showSelfCorrectingCheckBox.isSelected()) {
                showSelfCorrectingCheckBoxValue = "no";
            } else {
                showSelfCorrectingCheckBoxValue = "yes";
            }

            int timeLimit = Integer.parseInt(timeLimitTextField.getText());

            try {
                QuizService.updateQuiz(currentQuiz, quizNameTextField.getText(), timeLimit, startDateDatePicker.getValue().toString(), endDateDatePicker.getValue().toString(), selfCorrectingCheckBoxValue, showSelfCorrectingCheckBoxValue);
                Alerts.informationAlert("Succe!", "Quizet uppdaterades", "");

                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            } catch (Exception ex) {
                Alerts.errorAlert("Fel", "Det gick inte att spara uppdateringar", "Testa igen");
            }
        });

        // Cancel button
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    private void setQuizPropertiesToFields() {
        quizNameTextField.setText(quizName);

        startDateDatePicker.setValue(LocalDate.parse(quizStartDate));
        endDateDatePicker.setValue(LocalDate.parse(quizEndDate));

        timeLimitTextField.setText(String.valueOf(timeLimit));

        switch(selfcorrecting) {
            case "yes" : selfCorrectingCheckBox.setSelected(true);
                break;
            case "no" : selfCorrectingCheckBox.setSelected(false);
                break;
            default: selfCorrectingCheckBox.setSelected(false);
        }

        switch(showSelfcorrecting) {
            case "yes" : showSelfCorrectingCheckBox.setSelected(false);
                break;
            case "no" : showSelfCorrectingCheckBox.setSelected(true);
                break;
            default: showSelfCorrectingCheckBox.setSelected(false);
        }
    }

    // Load selected quiz and set variables
    private void loadQuiz() {
        Quiz quiz = QuizService.findQuiz(currentQuiz);

        quizName = quiz.getQuizName();
        timeLimit = quiz.getTimeLimit();
        quizStartDate = quiz.getQuizStartDate();
        quizEndDate = quiz.getQuizEndDate();
        selfcorrecting = quiz.getSelfcorrecting();
        showSelfcorrecting = quiz.getShowSelfCorrecting();
    }

    private void loadTableData() {
        // Get questions from selected quiz
        List<QuizQuestions> tempQuestions = QuestionService.read(currentQuiz);

        // Add to observablelist
        questions.addAll(tempQuestions);

        // Show in listview
        questionsTable.setItems(questions);
        questionsTable.refresh();
    }

    private void showEditQuizDialog() {
        try {
            // Load FXML file to dialog stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("EditQuestionDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Redigera Fråga");
            dialogStage.initOwner(cancelButton.getScene().getWindow());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadTableData();
    }

    private void reloadTableData() {
        questions.clear();
        questions.addAll(QuestionService.findAllQuestions(currentQuiz));
    }

    // Getters and setters
    static int getSelectedQuestion() {
        return selectedQuestion;
    }
}