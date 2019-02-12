package com.bananpiren.quiz.java.model;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Class for sending mail information about quiz to all user that is not admins.
 */

public class SendMail {
    public void sendMailToUser(String userMail, String quizName, String quizStartTime, String quizEndTime) {
        //Connection to google mail and user inputs.
        String host = "smtp.gmail.com";
        final String user = "bananpirenquizservice@gmail.com";
        final String password = "banan1234";

        //Connection properties.
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        //Session and authenticating.
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        //Creating a messages to be sent.
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userMail));
            message.setSubject("Quiz: " + quizName);
            message.setText("Hej, du har ett nytt quiz att ta!\n" +
                    "Det är giltigt från och med " + quizStartTime + " till och med " + quizEndTime + ".\nLycka till!");

            //Sending message
            Transport.send(message);

            System.out.println("Mail sent correct.");

        } catch (Exception e) {
            System.out.println("Unable to create/send mail message");
        }
    }
}