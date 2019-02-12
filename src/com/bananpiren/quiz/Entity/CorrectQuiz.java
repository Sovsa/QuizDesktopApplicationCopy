package com.bananpiren.quiz.Entity;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;

/**
 * This is an entity class for correcting quiz
 */

@Entity
public class CorrectQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int correctId;
    @NotNull
    private int answerId;
    @NotNull
    private int userId;
    @NotNull
    private String correctAnswer;
    @NotNull
    private String userAnswer;
    @NotNull
    private int quizId;

    // Getters and setters
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getCorrectId() {
        return correctId;
    }

    public void setCorrectId(int correctId) {
        this.correctId = correctId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}