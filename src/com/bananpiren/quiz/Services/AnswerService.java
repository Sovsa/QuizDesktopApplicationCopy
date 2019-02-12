package com.bananpiren.quiz.Services;

import com.bananpiren.quiz.Entity.QuestionAnswers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a service class that handles database commands via JPA
 * This class handles everything about answers
 */

public class AnswerService {

    public static void create(QuestionAnswers questionAnswers) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(questionAnswers);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    public static void create(ArrayList<QuestionAnswers> questionAnswers) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        for(QuestionAnswers questionAnswer: questionAnswers) {
            entityManager.persist(questionAnswer);
        }
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    // Returns a List of Answers based on questionId
    public static List<QuestionAnswers> read(int questionId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery( "Select a from QuestionAnswers a where a.question.questionId = " + questionId);
        List<QuestionAnswers> questionAnswers = (List<QuestionAnswers>)query.getResultList();

        return questionAnswers;
    }

    public static void update(int answerId, String newAnswer, int correctAnswer) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        QuestionAnswers answer = entityManager.find(QuestionAnswers.class, answerId);

        entityManager.getTransaction().begin();
        answer.setAnswer(newAnswer);
        answer.setCorrectAnswer(correctAnswer);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    public static void update(QuestionAnswers questionAnswers) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(questionAnswers);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    public static void delete(QuestionAnswers questionAnswers) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.remove(questionAnswers);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}