package com.bananpiren.quiz.Entity;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;

/**
 * This is an entity class for creating user objects
 */

@Entity
@Table
public class User {
    @TableGenerator(name = "autoGenerator", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "autoGenerator")
    private int userId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String accountLevel;

    public User(int userId, String firstName, String lastName, String email, String password, String accountLevel) {
        super();
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountLevel = accountLevel;
    }

    public User() {
        super();
    }

    // Getters and setters
    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}