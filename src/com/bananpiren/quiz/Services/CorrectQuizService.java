package com.bananpiren.quiz.Services;

import com.bananpiren.quiz.Entity.CorrectQuiz;
import com.bananpiren.quiz.Entity.UserQuiz;
import com.bananpiren.quiz.java.controller.LoginController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * This is a service class that handles database commands via JPA
 * This class handles everything about correcting quizes
 */

public class CorrectQuizService {

    public CorrectQuizService() {}

    public void correctQuiz(CorrectQuiz correctQuiz) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // starta överföringen
        entityManager.getTransaction().begin();

        // sparar
        entityManager.persist(correctQuiz);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }


    public List<CorrectQuiz> findAllCorrectQuizByAnswerId(int answerId){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("SELECT u FROM CorrectQuiz u WHERE u.answerId ="+answerId);
        List<CorrectQuiz> correctQuizList = (List<CorrectQuiz>) query.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        return correctQuizList;
    }


    public static List<CorrectQuiz> findCorrectQuizUserIdQuizId(int currentQuizId){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query userAnswers = entityManager.createQuery("SELECT cq FROM CorrectQuiz cq WHERE cq.quizId= " + currentQuizId + " AND cq.userId = " + LoginController.getCurrentUser().getUserId());
        List userAnswersList = userAnswers.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        return userAnswersList;
    }

    // update points
    public static void updatePoints(String id, int points) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Query userAnswer = entityManager.createQuery("SELECT uq FROM UserQuiz uq WHERE uq.quizId= " + id);
        UserQuiz uq = (UserQuiz) userAnswer.getSingleResult();

        uq.setPoints(points);

        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}