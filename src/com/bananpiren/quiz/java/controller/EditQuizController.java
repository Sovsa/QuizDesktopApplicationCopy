package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.Quiz;
import com.bananpiren.quiz.Entity.User;
import com.bananpiren.quiz.Services.QuizService;
import com.bananpiren.quiz.Services.UserService;
import com.bananpiren.quiz.java.model.Alerts;
import com.bananpiren.quiz.java.model.SendMail;
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

/**
 * This is a controller class that handles edits of quizes
 */

public class EditQuizController {

    private final ObservableList<Quiz> data = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();

    private static int storedSelectedTableIndex;
    private static int storedQuizId;

    private QuizService quizService = new QuizService();
    private UserService userService = new UserService();

    private SendMail sendMail = new SendMail();

    @FXML
    private Label numberOfQuestionsLabel;

    @FXML
    private Label timeLimitLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private Label selfCorrectedLabel;

    @FXML
    private Button editButton;

    @FXML
    private Label quizNameLabel;

    @FXML
    private Label startDateLimitLabel;

    @FXML
    private Label endDateLimitLabel;

    @FXML
    private Label showSelfCorrectedLabel;

    @FXML
    private TableColumn<Quiz, String> quizNameColumn;

    @FXML
    private TableView<Quiz> quizTableView;

    @FXML
    private Button sendMailButton;

    public EditQuizController() {
        loadTableData();
    }

    @FXML
    private void initialize() {

        // Setting data to right column "cellvalue"
        quizNameColumn.setCellValueFactory(new PropertyValueFactory<>("quizName"));
        quizTableView.setItems(data);

        editButton.setOnAction(e -> showEditQuizDialog());

        deleteButton.setOnAction(e -> handleDeleteQuiz());

        sendMailButton.setOnAction(event -> sendMailToUsers());

        quizTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(quizTableView.getSelectionModel().getSelectedItem() == null) {
                quizTableView.setPlaceholder(new Label("Det finns inga sparade quizer"));
            } else if(quizTableView.getSelectionModel().selectedItemProperty() != null) {
                storedSelectedTableIndex = quizTableView.getSelectionModel().getSelectedIndex();
                storedQuizId = data.get(storedSelectedTableIndex).getQuizId();

                quizNameLabel.setText(data.get(storedSelectedTableIndex).getQuizName());
                numberOfQuestionsLabel.setText("" + QuizService.numberOfQuestions(storedQuizId).size());
                timeLimitLabel.setText(Integer.toString(data.get(storedSelectedTableIndex).getTimeLimit()) + " minuter");
                startDateLimitLabel.setText(data.get(storedSelectedTableIndex).getQuizStartDate());
                endDateLimitLabel.setText(data.get(storedSelectedTableIndex).getQuizEndDate());

                switch(data.get(storedSelectedTableIndex).getShowSelfCorrecting()) {
                    case "yes" : showSelfCorrectedLabel.setText("Ja");
                        break;
                    case "no" : showSelfCorrectedLabel.setText("Nej");
                        break;
                }

                switch(data.get(storedSelectedTableIndex).getSelfcorrecting()) {
                    case "yes" : selfCorrectedLabel.setText("Ja");
                        break;
                    case "no" : selfCorrectedLabel.setText("Nej");
                        break;
                }

                editButton.setDisable(false);
                deleteButton.setDisable(false);
                sendMailButton.setDisable(false);
            } else {
                editButton.setDisable(true);
                deleteButton.setDisable(true);
                sendMailButton.setDisable(true);
            }
        });
    }

    private void handleDeleteQuiz() {
        if(quizTableView.getSelectionModel().getSelectedItem() != null) {
            quizService.deleteQuiz(storedQuizId);
            int selectedQuiz = quizTableView.getSelectionModel().getSelectedIndex();
            quizTableView.getItems().remove(selectedQuiz);
        } else {
            Alerts.warningAlert("Inget valt", "Ingen quiz är valt", "För att ta bort, välj ett quiz");
        }
    }

    private void showEditQuizDialog() {
        try {
            // Load FXML file to dialog stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("EditQuizDialog.fxml"));

            BorderPane page = loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Redigera Quiz");
            dialogStage.initOwner(deleteButton.getScene().getWindow());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadTableData();
        showSelfCorrectedLabel.setText(data.get(storedSelectedTableIndex).getShowSelfCorrecting());
    }

    private void loadTableData() {
        data.clear();
        data.addAll(QuizService.findAllQuiz());
    }

    private void sendMailToUsers() {
        users.addAll(userService.findAllStudents());

        for (User user : users) {
            sendMail.sendMailToUser(user.getEmail(),
                    data.get(storedSelectedTableIndex).getQuizName(),
                    data.get(storedSelectedTableIndex).getQuizStartDate(),
                    data.get(storedSelectedTableIndex).getQuizEndDate());
        }
    }

    static int getStoredQuizId() {
        return storedQuizId;
    }
}