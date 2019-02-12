package com.bananpiren.quiz.Entity;

/**
 * Class for holding statistic data for quiz taken by the current user.
 */

public class StatisticCurrentUser {
    private String quizName;
    private int points;
    private int noOfQuestions;
    private String correctPercentage;
    private String grade;

    public StatisticCurrentUser(String quizName, int points, int noOfQuestions, String correctPercentage, String grade) {
        this.quizName = quizName;
        this.points = points;
        this.noOfQuestions = noOfQuestions;
        this.correctPercentage = correctPercentage;
        this.grade = grade;
    }

    //Getters and setters
    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public String getCorrectPercentage() {
        return correctPercentage;
    }

    public void setCorrectPercentage(String correctPercentage) {
        this.correctPercentage = correctPercentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}