package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.Quiz;
import com.bananpiren.quiz.Entity.TakeQuiz;
import com.bananpiren.quiz.Entity.UserQuiz;
import com.bananpiren.quiz.Services.QuizService;
import com.bananpiren.quiz.Services.UserQuizService;
import com.bananpiren.quiz.java.model.Alerts;
import com.bananpiren.quiz.java.view.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This is a controller class that handles the start page
 */

public class StartController {

    private ObservableList<Quiz> data = FXCollections.observableArrayList();
    private static ArrayList<TakeQuiz> takeQuizList = new ArrayList<>();
    private static ArrayList<CheckBox> multiAnswerList = new ArrayList<>();
    private static ArrayList<RadioButton> singleAnswerList = new ArrayList<>();
    private static ArrayList<TextField> openAnswerList = new ArrayList<>();

    private ScrollPane scrollPane;

    static int currentQuizId;

    private QuizService quizService = new QuizService();

    @FXML
    private TableColumn<Quiz, String> quizNameColumn;

    @FXML
    private TableView<Quiz> quizTableView;

    @FXML
    private TableColumn<Quiz, String> quizEndDateColumn;

    @FXML
    private TableColumn<Quiz, String> quizStartDateColumn;

    @FXML
    private Button takeQuizButton;
    private boolean runQuiz;

    private boolean quizCorrectExists = true;

    public StartController() {
        data.addAll(QuizService.findAllQuiz());
    }

    @FXML
    private void initialize() {

        boolean theAdmin;

        theAdmin = LoginController.getCurrentUser().getAccountLevel().equals("Admin");

        // are there tests to correct?
        for (UserQuiz uq : UserQuizService.getAllUserQuiz()) {
            // alert if you have quiz to correct
            if ((uq.getPoints() == -1) && quizCorrectExists && theAdmin) {
                quizCorrectExists = false;
                new Alert(Alert.AlertType.INFORMATION, "Det finns nya orättade quiz att rätta!").showAndWait();
                break;
            }
        }

        // Setting data to right column "cellvalue"
        quizNameColumn.setCellValueFactory(new PropertyValueFactory<Quiz, String>("quizName"));
        quizEndDateColumn.setCellValueFactory(new PropertyValueFactory<Quiz, String>("quizEndDate"));
        quizStartDateColumn.setCellValueFactory(new PropertyValueFactory<Quiz, String>("quizStartDate"));
        quizTableView.setItems(data);
        takeQuizButton.setDisable(true);
        quizTableView.getSelectionModel().selectedIndexProperty().addListener(y -> {
            takeQuizButton.setDisable(false);
        });

        takeQuizButton.setOnAction((ActionEvent e) -> {
            runQuiz = false;

            String quizEndDate = quizTableView.getSelectionModel().selectedItemProperty().getValue().getQuizEndDate();
            String quizStartDate = quizTableView.getSelectionModel().selectedItemProperty().getValue().getQuizStartDate();
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateQuizEndDate = inputFormat.parse(quizEndDate);
                Date dateQuizStartDate = inputFormat.parse(quizStartDate);
                Calendar calQuizEndDate = Calendar.getInstance();
                Calendar calquizStartDate = Calendar.getInstance();
                calQuizEndDate.setTime(dateQuizEndDate);
                calquizStartDate.setTime(dateQuizStartDate);
                Calendar calToday = Calendar.getInstance();
                calQuizEndDate.set(Calendar.HOUR_OF_DAY, 23);
                calQuizEndDate.set(Calendar.MINUTE, 59);
                calQuizEndDate.set(Calendar.SECOND, 59);
                if (calQuizEndDate.compareTo(calToday) <= 0) {
                    Alerts.informationAlert("Fel", "Quiz har passerat datum", "Quizet du har valt har passerat sitt slutdatum");
                } else if (calquizStartDate.compareTo(calToday) > 0) {
                    Alerts.informationAlert("Fel", "Quiz har inte startat", "Quizet du har valt har inte börjat än");
                } else {

                    // get the Id of the current Quiz
                    currentQuizId = quizTableView.getSelectionModel().selectedItemProperty().getValue().getQuizId();

                    // get all the quiz with the loggedIn userId
                    List<UserQuiz> uq = UserQuizService.getAllUserQuizByUserId(LoginController.getCurrentUser().getUserId());

                    // if no tests are done
                    if (uq.size() == 0) {
                        runQuiz = true;
                    }

                    for (UserQuiz q : uq) {
                        // If the quizId in database is the same as the currentQuizid
                        if (Integer.parseInt(q.getQuizId()) != currentQuizId) {
                            runQuiz = true;
                        } else {
                            runQuiz = false;
                            new Alert(Alert.AlertType.INFORMATION, "Du har redan gjort detta Quiz!").showAndWait();
                            break;
                        }
                    }

                    if (runQuiz) {

                        VBox newCoolVbox = new VBox();

                        // get the list of the current quiz from the database
                        // building the querys
                        takeQuizList = quizService.currentQuiz(currentQuizId);

                        // displaying the question and answers on vbox
                        scrollPane = createQuizQuestions();
                        newCoolVbox.getChildren().addAll(scrollPane);

                        // create the TakeQuizController
                        TakeQuizController takeQuizController = new TakeQuizController();

                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("TakeQuiz.fxml"));
                            BorderPane takeQuiz = loader.load();
                            takeQuiz.setCenter(newCoolVbox);
                            Main.mainLayout.setCenter(takeQuiz);
                        } catch (IOException f) {
                            System.out.println("Couldn't load TakeQuiz.fxml: " + f);
                        }

                    }

                }
            } catch (Exception f) {
                System.out.println(f);
            }
        });
    }

    private ScrollPane createQuizQuestions() {

        int openQuestions = 0;

        // reset the lists
        multiAnswerList.clear();
        singleAnswerList.clear();
        openAnswerList.clear();

        // check the number of open questions
        for (TakeQuiz a : takeQuizList) {
            if (a.getQuestionType().equals("open")) {
                openQuestions++;
            }
        }

        // length of the list divided with the number of questions plus number of open questions
        int len = (takeQuizList.size() / 4) + openQuestions;

        String[] questionName = new String[len];
        Label[] questionLabel = new Label[len];
        String questionType = "";

        String[] answer = new String[takeQuizList.size()];
        Label[] answerLabel = new Label[takeQuizList.size()];
        CheckBox[] answerCheckbox = new CheckBox[takeQuizList.size()];
        TextField[] answerTextField = new TextField[takeQuizList.size()];
        RadioButton[] answerButton = new RadioButton[takeQuizList.size()];
        ToggleGroup[] toggleGroups = new ToggleGroup[len]; // set with the number of questions

        HBox[] answerBox = new HBox[takeQuizList.size()];

        VBox questionBox = new VBox();

        ScrollPane scrollPane = new ScrollPane();

        int incQuest = 0;
        int incAnswer = 0;
        int answerNo = 4;

        // loop through the questions
        for (int i = 0; i < len; i++) {
            // increment the question with the number of answers and get the question
            questionName[i] = takeQuizList.get(incQuest).getQuestion();
            questionType = takeQuizList.get(incQuest).getQuestionType();

            questionLabel[i] = new Label(questionName[i]);
            questionBox.getChildren().add(questionLabel[i]);
            questionBox.setSpacing(5);
            toggleGroups[i] = new ToggleGroup();
            Separator separator = new Separator();
            separator.setValignment(VPos.CENTER);

            if (questionType.equals("multiple") || questionType.equals("single")) {
                answerNo = 4;
            } else if (questionType.equals("open")) {
                answerNo = 1;
            }

            // loop through the answers
            for (int j = 0; j < answerNo; j++) {
                incAnswer = incQuest + j;

                answer[j] = takeQuizList.get(incAnswer).getAnswer();

                answerLabel[j] = new Label(answer[j]);
                answerBox[j] = new HBox();

                answerBox[j].setSpacing(5);

                // checks what kind of question
                if (questionType.equals("multiple")) {
                    answerCheckbox[j] = new CheckBox();
                    answerBox[j].getChildren().add(answerCheckbox[j]);
                    answerBox[j].getChildren().add(answerLabel[j]);

                    multiAnswerList.add(answerCheckbox[j]);

                } else if (questionType.equals("single")) {
                    answerButton[j] = new RadioButton();
                    answerButton[j].setToggleGroup(toggleGroups[i]);

                    answerBox[j].getChildren().add(answerButton[j]);
                    answerBox[j].getChildren().add(answerLabel[j]);

                    singleAnswerList.add(answerButton[j]);

                } else if (questionType.equals("open")) {
                    answerTextField[j] = new TextField();
                    answerBox[j].getChildren().add(answerTextField[j]);

                    openAnswerList.add(answerTextField[j]);
                }

                questionBox.getChildren().add(answerBox[j]);
            }
            questionBox.getChildren().add(separator);

            // increment with the number of answers
            incQuest += answerNo;
        }
        scrollPane.setContent(questionBox);

        return scrollPane;
    }

    static ArrayList<TakeQuiz> getTakeQuizList() {
        return takeQuizList;
    }

    static ArrayList<CheckBox> getMultiAnswerList() {
        return multiAnswerList;
    }

    static ArrayList<RadioButton> getSingleAnswerList() {
        return singleAnswerList;
    }

    static ArrayList<TextField> getOpenAnswerList() {
        return openAnswerList;
    }

    static int getCurrentQuizId() {
        return currentQuizId;
    }
}