package com.bananpiren.quiz.Entity;

/**
 * This is an entity class for creating take quiz
 */

public class TakeQuiz {

    // Quiz
    private String quizId;
    private String quizName;
    private int timeLimit;
    private String quizStartDate;
    private String quizEndDate;
    private String selfCorrectingList;
    private String showCorrectingList;

    // QuizQuestions
    private String questionId;
    private String question;
    private String questionType;

    // QuestionAnswers
    private String answerId;
    private String answer;
    private String correctAnswer;

    // UserQuiz
    private int userId;
    private String points;

    public TakeQuiz() {
    }

    public String getShowCorrectingList() {
        return showCorrectingList;
    }

    public void setShowCorrectingList(String showSelfCorrectingList) {
        this.showCorrectingList = showSelfCorrectingList;
    }

    // Getters and setters
    public String getSelfCorrectingList() {
        return selfCorrectingList;
    }

    public void setSelfCorrectingList(String selfCorrectingList) {
        this.selfCorrectingList = selfCorrectingList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getQuizStartDate() {
        return quizStartDate;
    }

    public void setQuizStartDate(String quizStartDate) {
        this.quizStartDate = quizStartDate;
    }

    public String getQuizEndDate() {
        return quizEndDate;
    }

    public void setQuizEndDate(String quizEndDate) {
        this.quizEndDate = quizEndDate;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
