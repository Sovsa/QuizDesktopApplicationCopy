package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.StatisticCurrentUser;
import com.bananpiren.quiz.Entity.StatisticsUser;
import com.bananpiren.quiz.Entity.UserQuiz;
import com.bananpiren.quiz.Services.QuizService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller class for collecting and displaying data fro current users taken test
 */

public class ResultsController {

    @FXML
    private TableView<StatisticCurrentUser> resultsTable;

    @FXML
    private TableColumn<StatisticCurrentUser, Integer> quizNameColumn;

    @FXML
    private TableColumn<StatisticCurrentUser, Integer> correctAnswersColumn;

    @FXML
    private TableColumn<StatisticCurrentUser, Integer> numberOfQuestionsColumn;

    @FXML
    private TableColumn<StatisticCurrentUser, String> resultColumn;

    @FXML
    private TableColumn<StatisticCurrentUser, String> gradeColumn;

    @FXML
    private PieChart resultPieChart;

    private ObservableList<UserQuiz> data = FXCollections.observableArrayList();
    private ObservableList<StatisticsUser> userData = FXCollections.observableArrayList();
    private ObservableList<StatisticCurrentUser> statisticsUserData = FXCollections.observableArrayList();

    private static int storedSelectedTableIndex;
    private int storedScore;
    private int storedWrongAnswers;
    private int storedNumberOfQuestions;

    public ResultsController() {
        data.addAll(QuizService.getUserQuiz(LoginController.getCurrentUser().getUserId()));
    }

    @FXML
    private void initialize(){
        quizNameColumn.setCellValueFactory(new PropertyValueFactory<>("QuizName"));
        correctAnswersColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        numberOfQuestionsColumn.setCellValueFactory(new PropertyValueFactory<>("noOfQuestions"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("correctPercentage"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        //Taking data from UserQuiz entity and creating a list of StatisticsUser.
        data.forEach(y -> {
            double userPoints = y.getPoints();
            double maxPoints = y.getMaxPoints();
            double pointsPercentage = (userPoints/maxPoints) * 100;
            pointsPercentage = Math.round(pointsPercentage * 100.0) / 100.0;
            String pointsPercentageString = String.valueOf(pointsPercentage);
            userData.add(
                    new StatisticsUser(y.getUserName(), y.getUserLastName(), pointsPercentageString, y.getUserId()));
        });

        //Combining UserQuiz data and StatisticsUser data to a list of StatisticCurrentUser data.
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getPoints() != -1) {
                statisticsUserData.add(new StatisticCurrentUser(
                        data.get(i).getQuizName(),
                        data.get(i).getPoints(),
                        data.get(i).getNoOfQuestions(),
                        userData.get(i).getCorrectPercentage(),
                        userData.get(i).getGrade())
                );
            }
        }

        resultsTable.setItems(statisticsUserData);

        //Calculating correct and wrong answers in a selected quiz and shows it in a pie chart.
        resultsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            storedSelectedTableIndex = resultsTable.getSelectionModel().getSelectedIndex();

            storedScore = statisticsUserData.get(storedSelectedTableIndex).getPoints();
            storedNumberOfQuestions = statisticsUserData.get(storedSelectedTableIndex).getNoOfQuestions();
            storedWrongAnswers = storedNumberOfQuestions - storedScore;

            ObservableList<PieChart.Data> userData = FXCollections.observableArrayList(
                    new PieChart.Data("Rätt svar: " + storedScore, storedScore),
                    new PieChart.Data("Fel svar: " + storedWrongAnswers, storedWrongAnswers)
            );

            resultPieChart.setData(userData);
        });
    }
}