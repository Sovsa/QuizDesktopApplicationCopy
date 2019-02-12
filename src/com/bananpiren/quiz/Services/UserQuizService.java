package com.bananpiren.quiz.Services;

import com.bananpiren.quiz.Entity.UserQuiz;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * This is a service class that handles database commands via JPA
 * This class handles everything about specifik user taken quizes
 */

public class UserQuizService {

    public UserQuizService() {}

    public void userQuiz(UserQuiz userQuiz) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        entityManager.persist(userQuiz);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    public List<UserQuiz> getAllUserQuizByQuizId(int quizID){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("SELECT u FROM UserQuiz u WHERE u.quizId ="+quizID);
        List<UserQuiz> userQuiz = (List<UserQuiz>) query.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        return userQuiz;
    }

    public static List<UserQuiz> getAllUserQuizByUserId(int userId) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Query userQuizById = entityManager.createQuery("SELECT u FROM UserQuiz u WHERE u.userId ="+userId+"");
        List<UserQuiz> userQuiz = userQuizById.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        return userQuiz;
    }

    public static List<UserQuiz> getAllUserQuiz(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("SELECT u FROM UserQuiz u");
        List<UserQuiz> userQuiz = query.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        return userQuiz;
    }
}
