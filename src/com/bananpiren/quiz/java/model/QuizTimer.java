package com.bananpiren.quiz.java.model;

import javafx.application.Platform;
import javafx.scene.control.*;

/**
 * Class to add time to a quiz if wanted.
 * Takes in a time, a label to post on and button to click if the time runs out.
 */

public class QuizTimer {
    private static Thread updateQuizTimeThread;

    public static void quizTimerClock(int quizTime, Label quizTimeLabel, Button sendQuizButton) {
        //Creating a new thread to not interfere with JavaFZ thread.
        Runnable updateQuizTimeRunnable = () -> {
            int timeMin = quizTime -1;
            int timeSek = 59;

            while (timeMin > 0 || timeSek > 0){
                int finalTimeMin = timeMin; //Storing time for using in Lambda
                int finalTimeSek = timeSek; //Storing time for using in Lambda
                    Platform.runLater(() -> quizTimeLabel.setText(finalTimeMin + ":" + finalTimeSek));
                    try {
                        Thread.sleep(1000); //Sleep thread for 1 sek.
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if (timeSek == 0){
                        timeSek = 59;
                        timeMin--;
                    } else {
                        timeSek--;
                    }
                }
            //When times up, pressing the button to tern in quiz.
            Platform.runLater(sendQuizButton::fire);
        };

        updateQuizTimeThread = new Thread(updateQuizTimeRunnable);
        updateQuizTimeThread.start();
    }

    //Method for terning of the clock if quiz were terned in before times up.
    public void killTimer() {
        updateQuizTimeThread.stop();
    }
}