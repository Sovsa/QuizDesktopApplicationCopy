package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.Quiz;
import com.bananpiren.quiz.Entity.UserQuiz;
import com.bananpiren.quiz.Services.UserQuizService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.List;

/**
 * This is a controller class that handles the view of results after taking a quiz
 */

public class TakeQuizResultController {
    @FXML
    private Text headerTextLabel;

    @FXML
    private Text quizNameText;

    @FXML
    private Text correctQuizAnswerText;

    @FXML
    private TableView<Quiz> quizTableView;

    private UserQuizService userQuizService = new UserQuizService();

    @FXML
    private void initialize() {

        int quizID = StartController.getCurrentQuizId();
        List<UserQuiz> userQuizList = userQuizService.getAllUserQuizByQuizId(quizID);

        if (TakeQuizController.getSelfCorrect() && TakeQuizController.getShowCorrect()) {
            quizNameText.setText("" + userQuizList.get(userQuizList.size() - 1).getQuizName());

            int numberOfQuestions = userQuizList.get(userQuizList.size() - 1).getNoOfQuestions();
            double maxScore = userQuizList.get(userQuizList.size() - 1).getMaxPoints();
            double userScore = userQuizList.get(userQuizList.size() - 1).getPoints();
            String passed;

            if ((userScore/maxScore) * 100 >= 60 ) {
                passed = "Grattis, du är godkänd!";
            } else {
                passed = "Underkänd, tyvärr fick du inte tillräcklig många rätt";
            }

            correctQuizAnswerText.setText(
                    passed + "\n" +
                    "\nDin poäng: " + userScore +
                    "\nMaxpoäng: " + maxScore
            );
        } else if (!TakeQuizController.getSelfCorrect()) {
            quizNameText.setText("" + userQuizList.get(userQuizList.size() - 1).getQuizName());
            correctQuizAnswerText.setText("Du har genomfört ett prov som inte är självrättande och provet måste därför rättas innan du kan se dina resultat!");

        } else if (TakeQuizController.getSelfCorrect() && !TakeQuizController.getShowCorrect()) {
            quizNameText.setText("" + userQuizList.get(userQuizList.size() - 1).getQuizName());
            correctQuizAnswerText.setText("Skaparen av quizet har valt att inte visa resultaten!");
        }
    }
}