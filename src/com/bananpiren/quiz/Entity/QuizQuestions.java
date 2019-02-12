package com.bananpiren.quiz.Entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * This is an entity class for creating question objects
 */

@Entity
public class QuizQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int questionId;

    private String question;
    private String questionType;

    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "question")
    private Collection<QuestionAnswers> questionAnswers;

    @ManyToOne()
    private Quiz quiz;

    @OneToMany(targetEntity = QuestionAnswers.class, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List questionAnswersList;

    public QuizQuestions(String question, Quiz quiz) {
        this.question = question;
        this.quiz = quiz;
    }

    public QuizQuestions(String question, String questionType, Quiz quiz) {
        this.question = question;
        this.questionType = questionType;
        this.quiz = quiz;
    }

    public QuizQuestions() {
        super();
    }

    // Getters and setters
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
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

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List getQuestionAnswersList() {
        return questionAnswersList;
    }

    public void setQuestionAnswersList(List questionAnswersList) {
        this.questionAnswersList = questionAnswersList;
    }

    public Collection<QuestionAnswers> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(Collection<QuestionAnswers> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}