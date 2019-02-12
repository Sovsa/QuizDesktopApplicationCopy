package com.bananpiren.quiz.Entity;

/**
 * This is an entity class for creating user statistics
 */

public class StatisticsUser {
    private String firstName;
    private String lastName;
    private String correctPercentage;
    private String grade;
    private int userId;

    public StatisticsUser(String firstName, String lastName, String correctPercentage, int userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.correctPercentage = correctPercentage;
        this.userId = userId;
        double storedCorrectPercentage = Double.parseDouble(correctPercentage);

        if (storedCorrectPercentage < 50) {
            this.grade = "IG";
        } else if (storedCorrectPercentage < 80) {
            this.grade = "G";
        } else {
            this.grade = "VG";
        }
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}