package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.CorrectQuiz;
import com.bananpiren.quiz.Entity.TakeQuiz;
import com.bananpiren.quiz.Entity.UserQuiz;
import com.bananpiren.quiz.Services.CorrectQuizService;
import com.bananpiren.quiz.Services.QuizService;
import com.bananpiren.quiz.Services.UserQuizService;
import com.bananpiren.quiz.java.model.QuizTimer;
import com.bananpiren.quiz.java.view.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This is a controller class that handles take quiz page
 */

public class TakeQuizController {
    @FXML
    private Label quizLabel;

    @FXML
    private Label totQuestions;

    @FXML
    private Label maxResult;

    @FXML
    private Button sendQuizButton;

    @FXML
    private ArrayList<CheckBox> multiAnswerList = new ArrayList<>();

    @FXML
    private
    ArrayList<RadioButton> singleAnswerList = new ArrayList<>();

    @FXML
    private
    ArrayList<TakeQuiz> takeQuizList = new ArrayList<>();

    @FXML
    private
    ArrayList<TextField> openAnswerList = new ArrayList<>();

    @FXML
    private CorrectQuiz correctQuiz;

    @FXML
    private Label quizTimeLabel;

    private int maxResultNo;

    private static boolean selfCorrect;
    private static boolean showCorrect;

    private QuizTimer quizTimer = new QuizTimer();


    @FXML
    private void initialize() {
        takeQuizList = StartController.getTakeQuizList();
        multiAnswerList = StartController.getMultiAnswerList();
        singleAnswerList = StartController.getSingleAnswerList();
        openAnswerList = StartController.getOpenAnswerList();

        maxResultNo = QuizService.numberOfQuestions(StartController.currentQuizId).size();

        // set maxResultLabel
        maxResult.setText("" + maxResultNo);

        // set totQuestionsLabel
        totQuestions.setText("" + maxResultNo);

        // Create CorrectQuizObject
        sendQuizButton.setOnAction(event -> {
            sendQuiz();
            sendUserQuiz();

            if (takeQuizList.get(0).getSelfCorrectingList().equals("yes")) {
                selfCorrect = true;
            } else {
                selfCorrect = false;
            }

            if (takeQuizList.get(0).getShowCorrectingList().equals("yes")) {
                showCorrect = true;
            } else {
                showCorrect = false;
            }

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("TakeQuizResult.fxml"));
                BorderPane takeQuiz = loader.load();
                Main.mainLayout.setCenter(takeQuiz);
            } catch (IOException f) {
                System.out.println("Couldn't load TakeQuizResult.fxml: " + f);
            }
        });

        if (takeQuizList.get(0).getTimeLimit() != 0) {
            startQuizTimer();
        }
        quizLabel.setText("" + takeQuizList.get(takeQuizList.size() - 1).getQuizName());
    }

    // store correct answer and useranswer
    private void sendQuiz() {
        int noMultiple = 0;
        int noSingle = 0;
        int noOpen = 0;
        int counter = 0;

        for (int i = 0; i < takeQuizList.size(); i++) {

            correctQuiz = new CorrectQuiz();
            correctQuiz.setAnswerId(Integer.parseInt(takeQuizList.get(i).getAnswerId()));
            correctQuiz.setCorrectAnswer(takeQuizList.get(i).getCorrectAnswer());
            correctQuiz.setUserId(LoginController.getCurrentUser().getUserId());
            correctQuiz.setQuizId(Integer.parseInt(takeQuizList.get(0).getQuizId()));

            // check if multi, single or open question
            if (takeQuizList.get(i).getQuestionType().equals("multiple")) {
                correctQuiz.setUserAnswer(String.valueOf(multiAnswerList.get(i - noSingle - noOpen).isSelected() ? 1 : 0));
                noMultiple++;
            } else if (takeQuizList.get(i).getQuestionType().equals("single")) {
                correctQuiz.setUserAnswer(String.valueOf(singleAnswerList.get(i - noMultiple - noOpen).isSelected() ? 1 : 0));
                noSingle++;
            } else if (takeQuizList.get(i).getQuestionType().equals("open")) {
                correctQuiz.setUserAnswer(openAnswerList.get(i - noMultiple - noSingle).getText());
                noOpen++;
            }

            // create table
            CorrectQuizService correctQuizService = new CorrectQuizService();
            correctQuizService.correctQuiz(correctQuiz);
        }
    }

    // correct the quiz and save it in a new table
    private void sendUserQuiz() {
        if (takeQuizList.get(0).getTimeLimit() != 0) {
            quizTimer.killTimer();
        }

        UserQuiz userQuiz = new UserQuiz();
        userQuiz.setUserId(LoginController.getCurrentUser().getUserId());
        userQuiz.setQuizName(takeQuizList.get(0).getQuizName());
        userQuiz.setQuizId(takeQuizList.get(0).getQuizId());
        userQuiz.setUserName(LoginController.getCurrentUser().getFirstName());
        userQuiz.setUserLastName(LoginController.getCurrentUser().getLastName());

        int points = 1;
        int countedPoints = 0;
        int questionId = Integer.parseInt(takeQuizList.get(0).getQuestionId());
        int newQuestion = 4;
        int noMultiple = 0;
        int noSingle = 0;

        if(takeQuizList.get(0).getSelfCorrectingList().equals("yes")) {
            // get points
            for (int i = 0; i < takeQuizList.size(); i++) {

                // check if its a new Question
                if (i % 4 == 0) {
                    points = 0;
                }

                int selected = 0;

                // check if multi or single question
                if (takeQuizList.get(i).getQuestionType().equals("multiple")) {
                    selected = (multiAnswerList.get(i - noSingle).isSelected()) ? 1 : 0;
                    noMultiple++;
                } else {
                    selected = singleAnswerList.get(i - noMultiple).isSelected() ? 1 : 0;
                    noSingle++;
                }
                if (selected == Integer.parseInt(takeQuizList.get(i).getCorrectAnswer())) {
                    points++;
                }

                // if all is correct - get point, otherwise dont
                if (points >= 4) {
                    countedPoints++;
                }
            }
        } else {
            countedPoints = -1;
        }

        int theResult = countedPoints;
        int questionNumber = takeQuizList.size() / 4;

        userQuiz.setNoOfQuestions(maxResultNo);
        userQuiz.setMaxPoints(maxResultNo);
        userQuiz.setPoints(countedPoints);

        // create table
        UserQuizService userQuizService = new UserQuizService();

        userQuizService.userQuiz(userQuiz);
    }

    private void startQuizTimer() {
        QuizTimer.quizTimerClock(takeQuizList.get(0).getTimeLimit(), quizTimeLabel, sendQuizButton);
    }

    static boolean getSelfCorrect() {
        return selfCorrect;
    }

    static boolean getShowCorrect() {
        return showCorrect;
    }
}