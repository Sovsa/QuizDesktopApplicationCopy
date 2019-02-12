package com.bananpiren.quiz.Services;

import com.bananpiren.quiz.Entity.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a service class that handles database commands via JPA
 * This class handles everything about Quizes
 */

public class QuizService {

    public QuizService() {
    }

    public static void create(Quiz quiz) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(quiz);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }

    public static void create(Quiz quiz, ArrayList<QuizQuestions> questions, ArrayList<QuestionAnswers> answers) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(quiz);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();

        QuestionService.create(questions);
        AnswerService.create(answers);
    }

    // Find a quiz by ID
    public static Quiz findQuiz(int quizId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Quiz quiz = entityManager.find(Quiz.class, quizId);

        entityManager.close();

        return quiz;
    }

    // Delete a quiz by ID
    public void deleteQuiz(int quizId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        Quiz quiz = entityManager.find(Quiz.class, quizId);
        entityManager.remove(quiz);

        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    // Get all Quizes from database and return as list of Quiz objects
    public static List findAllQuiz() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("SELECT q FROM Quiz q");

        return query.getResultList();
    }

    public static List numberOfQuestions(int currentQuizId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        // Query quizName and put it in a list
        Query numberOfQuestions = entityManager.createQuery("SELECT qq FROM QuizQuestions qq WHERE qq.quiz.quizId = " + currentQuizId + " ");
        List number = numberOfQuestions.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        return number;
    }

    public ArrayList<TakeQuiz> currentQuiz(int currentQuizId) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        // Query quizName and put it in a list
        Query quizName = entityManager.createQuery("SELECT qq.quiz.quizName FROM QuizQuestions qq JOIN QuestionAnswers qa WHERE qq.questionId = qa.question.questionId AND qq.quiz.quizId = " + currentQuizId + " ");
        List quizNameList = quizName.getResultList();

        // Query question and put them in a list
        Query question = entityManager.createQuery("SELECT qq.question FROM QuizQuestions qq JOIN QuestionAnswers qa WHERE qq.questionId = qa.question.questionId AND qq.quiz.quizId = " + currentQuizId + "");
        List questionList = question.getResultList();

        // Query answers and put them in a list
        Query answer = entityManager.createQuery("SELECT qa.answer FROM QuizQuestions qq JOIN QuestionAnswers qa WHERE qq.questionId = qa.question.questionId AND qq.quiz.quizId = " + currentQuizId + "");
        List answerList = answer.getResultList();

        // Query questionType and put them in a list
        Query questionType = entityManager.createQuery("SELECT qa.question.questionType FROM QuizQuestions qq JOIN QuestionAnswers qa WHERE qq.questionId = qa.question.questionId AND qq.quiz.quizId = " + currentQuizId + "");
        List questionTypeList = questionType.getResultList();

        // Query all the answerId from the selected Quiz
        Query answerId = entityManager.createQuery("SELECT qa.answerId FROM QuizQuestions qq JOIN QuestionAnswers qa WHERE qq.questionId = qa.question.questionId AND qq.quiz.quizId = " + currentQuizId + "");
        List answerIdList = answerId.getResultList();

        // Query correctAnswer
        Query correctAnswer = entityManager.createQuery("SELECT qa.correctAnswer FROM QuizQuestions qq JOIN QuestionAnswers qa WHERE qq.questionId = qa.question.questionId AND qq.quiz.quizId = " + currentQuizId + "");
        List correctAnswerList = correctAnswer.getResultList();

        // Query questionId
        Query questionId = entityManager.createQuery("SELECT qq.questionId FROM QuizQuestions qq JOIN QuestionAnswers qa WHERE qq.questionId = qa.question.questionId AND qq.quiz.quizId = " + currentQuizId + "");
        List questionIdList = questionId.getResultList();

        // Query quizId
        Query quizId = entityManager.createQuery("SELECT qq.quiz.quizId FROM QuizQuestions qq JOIN QuestionAnswers qa WHERE qq.questionId = qa.question.questionId AND qq.quiz.quizId = " + currentQuizId + "");
        List quizIdList = quizId.getResultList();

        // Query timer
        Query timer = entityManager.createQuery("SELECT q.timeLimit FROM Quiz q JOIN QuestionAnswers qa WHERE q.quizId = " + currentQuizId + "");
        List timerList = timer.getResultList();

        // Query selfCorrecting
        Query selfCorrecting = entityManager.createQuery("SELECT q.selfcorrecting FROM Quiz q JOIN QuestionAnswers qa WHERE q.quizId = " + currentQuizId + "");
        List selfCorrectingList = selfCorrecting.getResultList();

        // Query showfCorrecting
        Query showCorrecting = entityManager.createQuery("SELECT q.showSelfCorrecting FROM Quiz q JOIN QuestionAnswers qa WHERE q.quizId = " + currentQuizId + "");
        List showCorrectingList = showCorrecting.getResultList();

        int count = 0;

        TakeQuiz takeQuiz;
        ArrayList<TakeQuiz> takeQuizList = new ArrayList<>();

        // skapar TakeQuiz med namn, frågor och svar
        for (int i = 0; i < quizNameList.size(); i++) {
            takeQuiz = new TakeQuiz();
            takeQuiz.setQuizName(quizNameList.get(i).toString());
            takeQuiz.setQuestion(questionList.get(i).toString());
            takeQuiz.setAnswer(answerList.get(i).toString());
            takeQuiz.setQuestionType(questionTypeList.get(i).toString());
            takeQuiz.setAnswerId(answerIdList.get(i).toString());
            takeQuiz.setCorrectAnswer(correctAnswerList.get(i).toString());
            takeQuiz.setQuestionId(questionIdList.get(i).toString());
            takeQuiz.setQuizId(quizIdList.get(i).toString());//            takeQuiz.setPoints(resultList.get(i).toString());
            takeQuiz.setTimeLimit((int) timerList.get(i));
            takeQuiz.setSelfCorrectingList(selfCorrectingList.get(i).toString());
            takeQuiz.setShowCorrectingList(showCorrectingList.get(i).toString());

            takeQuizList.add(takeQuiz);
        }

        entityManager.close();
        entityManagerFactory.close();

        return takeQuizList;
    }

    public static void updateQuiz(int quizId, String quizName, int timeLimit, String quizStartDate, String quizEndDate, String selfCorrecting, String showSelfCorrecting) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Quiz quiz = entityManager.find(Quiz.class, quizId);

        quiz.setQuizName(quizName);
        quiz.setTimeLimit(timeLimit);
        quiz.setQuizStartDate(quizStartDate);
        quiz.setQuizEndDate(quizEndDate);
        quiz.setSelfcorrecting(selfCorrecting);
        quiz.setShowSelfCorrecting(showSelfCorrecting);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static List getUserQuiz(int userId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List list = entityManager.createQuery(
                "SELECT q FROM UserQuiz q WHERE q.userId = " + userId)
                .getResultList();

        entityManager.close();
        entityManagerFactory.close();

        return list;
    }
}
