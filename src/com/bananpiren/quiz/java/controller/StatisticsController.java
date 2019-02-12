package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.*;
import com.bananpiren.quiz.Services.*;
import com.bananpiren.quiz.java.model.Alerts;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a controller class that handles statistics page
 */

public class StatisticsController {

    @FXML
    private Tab correctAnswersTab;

    @FXML
    private Button printPdfButton;

    @FXML
    private TableColumn<StatisticsUser, String> statisticsUserCorrectPercentageColumn;

    @FXML
    private TableColumn<StatisticsUser, String> statisticsUserFirstNameColumn;

    @FXML
    private TableColumn<StatisticsUser, String> statisticsUserLastNameColumn;

    @FXML
    private TableView<StatisticsUser> statisticsUserTableViev;

    @FXML
    private PieChart correctAnswersPieChart;

    @FXML
    private Tab usersTab;

    @FXML
    private TableView<Quiz> quizTableView;

    @FXML
    private TableColumn<Quiz, String> quizTableColumn;

    private ObservableList<Quiz> data = FXCollections.observableArrayList();
    private ObservableList<StatisticsUser> statisticsUserData = FXCollections.observableArrayList();
    private QuizService quizService = new QuizService();
    private UserQuizService userQuizService = new UserQuizService();
    private QuestionService questionService = new QuestionService();
    private List<QuizQuestions> questionsList = new ArrayList<>();
    private AnswerService answerService = new AnswerService();
    private List<QuestionAnswers> answersList = new ArrayList<>();
    private CorrectQuizService correctQuizService = new CorrectQuizService();
    private List<CorrectQuiz> correctQuizList2 = new ArrayList<>();
    private List<CorrectQuiz> correctQuizList = new ArrayList<>();
    private int quizID;
    private int questionCount;
    private List<UserQuiz> userQuizList = new ArrayList<>();
    private double averageScore;
    private int users;
    private double userPoints;
    private double maxPoints;
    private double pointsPercentage;
    private String pointsPercentageString;
    private boolean studentAnswerPrinted;
    private String studentAnswerString;

    FontSelector studentAnswerSelector = new FontSelector();

    @FXML
    private void initialize() {
        data.addAll(quizService.findAllQuiz());
        quizTableColumn.setCellValueFactory(new PropertyValueFactory<Quiz, String>("quizName"));
        statisticsUserFirstNameColumn.setCellValueFactory(new PropertyValueFactory<StatisticsUser, String>("firstName"));
        statisticsUserLastNameColumn.setCellValueFactory(new PropertyValueFactory<StatisticsUser, String>("lastName"));
        statisticsUserCorrectPercentageColumn.setCellValueFactory(new PropertyValueFactory<StatisticsUser, String>("correctPercentage"));
        quizTableView.setItems(data);

        quizTableView.getSelectionModel().selectedItemProperty().addListener(e -> {
            statisticsUserData.clear();
            averageScore = 0;
            users = 0;
            questionCount = 0;
            quizID = quizTableView.getSelectionModel().selectedItemProperty().getValue().getQuizId();
            questionCount = Integer.parseInt(QuestionService.getNumberOfQuestions(Integer.toString(quizID)));
            userQuizList = userQuizService.getAllUserQuizByQuizId(quizID);

            userQuizList.forEach(f -> {
                averageScore = averageScore + f.getPoints();
                users++;
            });

            averageScore = averageScore / users;
            double averageScorePercentage = (averageScore / questionCount) * 100;
            double wrongAnswerPercentage = 100 - averageScorePercentage;
            averageScorePercentage = Math.round(averageScorePercentage * 100.0) / 100.0;
            wrongAnswerPercentage = Math.round(wrongAnswerPercentage * 100.0) / 100.0;
            ObservableList<PieChart.Data> userData = FXCollections.observableArrayList(
                    new PieChart.Data("Rätt svar: " + averageScorePercentage + "%", averageScorePercentage),
                    new PieChart.Data("Fel svar: " + wrongAnswerPercentage + "%", wrongAnswerPercentage)
            );
            correctAnswersPieChart.setData(userData);

            userQuizList.forEach (y -> {
                userPoints = y.getPoints();
                maxPoints = y.getMaxPoints();
                pointsPercentage = (userPoints/maxPoints) * 100;
                pointsPercentage = Math.round(pointsPercentage * 100.0) / 100.0;
                pointsPercentageString = String.valueOf(pointsPercentage);
                if(y.getPoints()>=0) {
                    statisticsUserData.add(
                            new StatisticsUser(y.getUserName(), y.getUserLastName(), pointsPercentageString, y.getUserId()));
                }
            });
            statisticsUserTableViev.setItems(statisticsUserData);

        });
        printPdfButton.setDisable(true);
        statisticsUserTableViev.getSelectionModel().selectedItemProperty().addListener(o->{
            printPdfButton.setDisable(false);
        });
        printPdfButton.setOnAction(x -> {
            int selectedUserId = statisticsUserTableViev.getSelectionModel().selectedItemProperty().getValue().getUserId();
            String selectedUserFirstName = statisticsUserTableViev.getSelectionModel().selectedItemProperty().getValue().getFirstName();
            String selectedUserLastName = statisticsUserTableViev.getSelectionModel().selectedItemProperty().getValue().getLastName();
            String quizName = userQuizList.get(0).getQuizName();
            questionsList = questionService.read(quizID);
            try{
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(quizName+", "+selectedUserFirstName+" "+selectedUserLastName+".pdf"));
                document.open();

                //Title of quiz in document
                FontSelector headerFontSelector = new FontSelector();
                Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 24);
                headerFontSelector.addFont(headerFont);
                Phrase header = headerFontSelector.process(quizName+"\n\n\n");

                //Student info header
                FontSelector studentHeaderSelector = new FontSelector();
                Font studentHeaderFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 14);
                studentHeaderSelector.addFont(studentHeaderFont);
                Phrase studentHeader = studentHeaderSelector.process("Student info:\n");

                //Student info
                FontSelector studentInfoSelector = new FontSelector();
                Font studentInfoFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12);
                studentInfoSelector.addFont(studentInfoFont);
                Phrase studentInfo = studentInfoSelector.process("Förnamn: "+selectedUserFirstName+"\nEfternamn: "+selectedUserLastName+"\nStudentId: "+selectedUserId+"\n\n\n");

                //Add to document
                document.add(header);
                document.add(studentHeader);
                document.add(studentInfo);

                //Prints the questions and the answers
                questionsList.forEach(l -> {
                    studentAnswerString = "";
                    try {
                        //Print the question
                        FontSelector questionSelector = new FontSelector();
                        Font questionFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14);
                        questionSelector.addFont(questionFont);
                        Phrase question = questionSelector.process(l.getQuestion()+"\n");
                        document.add(question);

                        //Print the questiontype
                        FontSelector questionTypeSelector = new FontSelector();
                        Font questionTypeFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 11);
                        questionTypeSelector.addFont(questionTypeFont);

                        if(l.getQuestionType().equals("single")){
                            Phrase questionType = questionTypeSelector.process("(Envalsfråga)\n");
                            document.add(questionType);
                        }else if(l.getQuestionType().equals("multiple")) {
                            Phrase questionType = questionTypeSelector.process("(Flervalsfråga)\n");
                            document.add(questionType);
                        }else if(l.getQuestionType().equals("open")){
                            Phrase questionType = questionTypeSelector.process("(Öppen fråga)\n");
                            document.add(questionType);
                        }


                        int questionId = l.getQuestionId();
                        studentAnswerPrinted=true;
                        answersList = answerService.read(questionId);

                        answersList.forEach(p -> {
                            correctQuizList2 = correctQuizService.findAllCorrectQuizByAnswerId(p.getAnswerId());
                            correctQuizList.clear();
                            correctQuizList2.forEach(e->{
                                if(e.getUserId()==selectedUserId){
                                    correctQuizList.add(e);
                                }
                            });
                            //Set font for answer and students answer
                            try{
                            FontSelector answerSelector = new FontSelector();
                            Font answerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
                            answerSelector.addFont(answerFont);

                            Font studentAnswerFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12);
                            studentAnswerSelector.addFont(studentAnswerFont);

                            if(l.getQuestionType().equals("single")){
                                if (correctQuizList.get(0).getCorrectAnswer().equals("1")){
                                    Phrase answer = answerSelector.process(p.getAnswer()+"      (Rätt svar)\n");
                                    document.add(answer);
                                }else{
                                    Phrase answer = answerSelector.process(p.getAnswer()+"\n");
                                    document.add(answer);
                                }
                                if(studentAnswerPrinted && correctQuizList.get(0).getUserAnswer().equals("1")){
                                    studentAnswerString = "Studentens svar: "+p.getAnswer()+"\n";
                                    studentAnswerPrinted=false;
                                }else{

                                }
                            }else if(l.getQuestionType().equals("multiple")){
                                if(correctQuizList.get(0).getCorrectAnswer().equals("1")){
                                    Phrase answer = answerSelector.process(p.getAnswer()+"      (Rätt svar)\n");
                                    document.add(answer);
                                }else{
                                    Phrase answer = answerSelector.process(p.getAnswer()+"\n");
                                    document.add(answer);
                                }

                                if(correctQuizList.get(0).getUserAnswer().equals("1")){
                                    studentAnswerString += "Studentens svar: "+p.getAnswer()+"\n";
                                }else{

                                }
                            }else if(l.getQuestionType().equals("open")){
                                Phrase answer = studentAnswerSelector.process("Studentens öppna svar:\n");
                                document.add(answer);
                                Phrase studentAnswer = answerSelector.process(correctQuizList.get(0).getUserAnswer());
                                document.add(studentAnswer);
                            }
                        }catch(DocumentException n){
                                System.out.println(n+" in answerslist");
                            }

                        });
                        Phrase studentAnswer = studentAnswerSelector.process(studentAnswerString);
                        document.add(studentAnswer);
                    }catch(DocumentException t){
                        System.out.println(t);
                    }
                });

                document.close();
            }catch(DocumentException m){
                System.out.println(m);
            }catch(FileNotFoundException k){
                Alerts.errorAlert("Fel", "Fil öppen", "Filen är redan öppnad i ettt annat program. Stäng ner programmet och försök igen");
            }
        });
    }
}