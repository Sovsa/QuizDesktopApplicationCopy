package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.java.model.Alerts;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is a controller class that handles QuizBattle page
 */

public class SocketClientController {

    @FXML
    private TextArea textAreaConsole;

    @FXML
    private TextField textFieldHostAdress;

    @FXML
    private Button buttonConnect;

    @FXML
    private TextArea textAreaConnectedUsers;

    @FXML
    private TextField textFieldUserName;

    @FXML
    private Label labelCurrentUser;

    @FXML
    private Button buttonDisconnect;

    @FXML
    private Label labelConnectedUsers;

    @FXML
    private TextField textFieldInput;


    private static String USERNAME;

    private static int PORT;
    private static String HOST;

    Socket socket;
    Scanner input;
    PrintWriter output;

    @FXML
    private void initialize() {

        textFieldUserName.setText(LoginController.getCurrentUser().getFirstName());

        // Button Connect Action
        buttonConnect.setOnAction(event -> {
            if (!textFieldUserName.getText().equals("")) {
                PORT = 5565;
                HOST = textFieldHostAdress.getText();
                USERNAME = textFieldUserName.getText();
                labelCurrentUser.setText(USERNAME);
                buttonConnect.setDisable(true);
                buttonDisconnect.setDisable(false);
                textFieldInput.setEditable(true);
                textFieldInput.setDisable(false);
                connect();
            } else {
                Alerts.warningAlert("Information", "", "Please enter a username");
            }
        });

        // Button Disconnect Action
        buttonDisconnect.setOnAction(event -> {
            try {
                buttonConnect.setDisable(false);
                buttonDisconnect.setDisable(true);
                disconnect();
            } catch (Exception exception) {
                textAreaConsole.appendText("Couldn't disconnect, something went wrong \n");
            }
        });

        // Autoscroll message area
        textAreaConsole.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                textAreaConsole.setScrollTop(Double.MAX_VALUE);
            }
        });

        // Listen to textfield input, send message with "Enter Key"
        textFieldInput.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                if (textFieldInput.getText().equals("/DISCONNECT")) {
                    try {
                        disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    textFieldInput.clear();
                } else if (textFieldInput.getText().equals("/QUIT")) {
                    try {
                        disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(1);
                } else if (textFieldInput.getText().equals("/HELP")) {
                    textFieldInput.clear();
                    textAreaConsole.appendText("Available commands:\n" +
                            "/SCORE - print out scores\n" +
                            "/DISCONNECT - disconnect from server\n" +
                            "/SERVERCOMMANDS - sends server commands to online users\n" +
                            "/QUIT - disconnect and quit program\n"
                    );
                    buttonDisconnect.setDisable(true);
                    buttonConnect.setDisable(false);
                } else {
                    send(textFieldInput.getText());
                }
            }
        });
    }

    // Connect to server action
    private void connect() {
        try {
            Socket socket = new Socket(HOST, PORT);
            textAreaConsole.appendText("You are connected to: " + HOST + ":" + PORT + "\n");

            // Start new thread
            Runnable chatClient = () -> {
                try {
                    try {
                        input = new Scanner(socket.getInputStream());
                        output = new PrintWriter(socket.getOutputStream());
                        output.flush();
                        while (true) {
                            // Listens to input
                            receieve();
                        }
                    } finally {
                        socket.close();
                    }
                } catch (Exception exception) {
                    System.out.print(exception + "Något gick fel med I/O");
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            // Send name to "current online"
            PrintWriter tempOutput = new PrintWriter(socket.getOutputStream());
            tempOutput.println(USERNAME);
            tempOutput.flush();

            Thread clientThread = new Thread(chatClient);
            clientThread.start();
        } catch (Exception exception) {
            System.out.print(exception);
            Alerts.warningAlert("Information", "", "Server is not responding, check if server is online!");
        }
    }

    // Check incomming sockets for commands and text
    public void receieve() {
        // Messages from server
        if(input.hasNext()) {
            String message = input.nextLine();

            // Current users sent from Server
            if(message.contains("#CLEAR")) {
                textAreaConnectedUsers.setText("");
            } else if (message.contains("#USERNAME")) {
                String temp1 = message.substring(9);
                temp1 = temp1.replace("[","");
                temp1 = temp1.replace("]","");
                textAreaConnectedUsers.appendText(temp1 + "\n");
            } else if (message.contains("/SERVERCOMMANDS")) {
                textAreaConsole.appendText("Available commands:\n" +
                        "/SCORE - print out scores\n" +
                        "/DISCONNECT - disconnect from server\n" +
                        "/QUIT - disconnect and quit program\n"
                );
            } else {
                System.out.println(message);
                textAreaConsole.appendText(message + "\n");
            }
        }
    }

    // Disconnect from server
    public void disconnect() throws IOException {
        try {
            labelCurrentUser.setText("");
            output.println("/DISCONNECT " + USERNAME + " has disconnected.");
            output.flush();
            socket.close();
            Alerts.warningAlert("Information", "", "You have disconnected");
        } catch (Exception exception) {
            textAreaConsole.setText("Disconnect function failed! \n");
        }
    }

    public void send(String message) {
        try {
            output.println(USERNAME + ": " + message);
            output.flush();
            textFieldInput.clear();
        } catch (Exception exception) {
            output.println("message not sent! -something went wrong... \n");
        }
    }
}