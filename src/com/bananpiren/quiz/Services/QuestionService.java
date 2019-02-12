package com.bananpiren.quiz.Services;

import com.bananpiren.quiz.Entity.QuizQuestions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a service class that handles database commands via JPA
 * This class handles everything about questions
 */

public class QuestionService {

    public static void create(QuizQuestions quizQuestions) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(quizQuestions);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    // Delete a Question by ID
    public static void deleteQuestion(int questionId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        QuizQuestions question = entityManager.find(QuizQuestions.class, questionId);
        entityManager.remove(question);

        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    public static void create(ArrayList<QuizQuestions> quizQuestions) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        for (QuizQuestions quizQuestion : quizQuestions) {
            entityManager.persist(quizQuestion);
        }
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    // Find a question by ID
    public static QuizQuestions findQuestion(int questionId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        QuizQuestions question = entityManager.find(QuizQuestions.class, questionId);

        entityManager.close();

        return question;
    }

    // Get all Questions from database and return as list of Question objects
    public static List findAllQuestions(int questionId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("Select q from QuizQuestions q where q.quiz.quizId = " + questionId);

        return query.getResultList();
    }

    // Returns List of Questions based on testId
    public static List read(int testId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("Select q from QuizQuestions q where q.quiz.quizId = " + testId);
        List<QuizQuestions> quizQuestions = (List<QuizQuestions>) query.getResultList();

        return quizQuestions;
    }

    public static void update(int questionId, String newQuestion) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        QuizQuestions question = entityManager.find(QuizQuestions.class, questionId);

        entityManager.getTransaction().begin();
        question.setQuestion(newQuestion);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    public static void delete(QuizQuestions quizQuestions) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.remove(quizQuestions);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    public static String getNumberOfQuestions(String quizId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List list = entityManager.createQuery(
                "SELECT count(q) FROM QuizQuestions q WHERE q.quiz.quizId =" + quizId)
                .getResultList();

        String numberOfQuestions = list.get(0).toString();

        entityManager.close();
        entityManagerFactory.close();

        return numberOfQuestions;
    }
}