package com.bananpiren.quiz.Entity;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;

/**
 * This is an entity class for creating answer objects
 */

@Entity
public class QuestionAnswers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int answerId;

    @NotNull
    private String answer;
    @NotNull
    private int correctAnswer;

    @ManyToOne()
    private QuizQuestions question;

    public QuestionAnswers(String answer, int correctAnswer, QuizQuestions question) {
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.question = question;
    }

    // Getters and setters
    public QuestionAnswers() {}

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int taId) {
        this.answerId = taId;
    }

    public QuizQuestions getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestions question) {
        this.question = question;
    }
}