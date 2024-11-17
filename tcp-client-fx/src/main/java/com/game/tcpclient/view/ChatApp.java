package com.game.tcpclient.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ChatApp extends Application {

    private VBox chatBox;

    @Override
    public void start(Stage primaryStage) {
        // Header
        HBox header = new HBox();
        header.getStyleClass().add("header");
        Label chatTitle = new Label("Chat with hieucb98");
        chatTitle.getStyleClass().add("chat-title");
        Button leaveButton = new Button("Leave");
        leaveButton.getStyleClass().add("leave-button");
        header.getChildren().addAll(chatTitle, leaveButton);
        HBox.setHgrow(chatTitle, Priority.ALWAYS);

        // Chat area
        chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));
        chatBox.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("chat-scroll-pane");

        // Footer with input field and send button
        HBox footer = new HBox(10);
        footer.setPadding(new Insets(10));
        TextField inputField = new TextField();
        inputField.setPromptText("Type your message...");
        Button sendButton = new Button("Send");
        footer.getChildren().addAll(inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        // Sample chat messages
        addMessage("Hi my friend!!!", false);
        addMessage("Do you mind sending me the brief for the new campaign? bla bla bla...", false);
        addMessage("Hey, what's up!!!", true);
        addMessage("Of course. bla bla bla bla...", true);

        // Send button action
        sendButton.setOnAction(e -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                addMessage(message, true);
                inputField.clear();
            }
        });

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(header);
        layout.setCenter(scrollPane);
        layout.setBottom(footer);

        // Scene and CSS
        Scene scene = new Scene(layout, 400, 600);
        scene.getStylesheets().add("style.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Application");
        primaryStage.show();
    }

    // Method to add a message bubble to the chat box
    private void addMessage(String message, boolean isUser) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        if (!isUser) {
            // Add icon for the other person's message
            ImageView icon = new ImageView(new Image("avatar.png"));
            icon.setFitWidth(30);
            icon.setFitHeight(30);
            messageBox.getChildren().add(icon);
        }

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(250);
        messageLabel.getStyleClass().add(isUser ? "user-message" : "friend-message");

        messageBox.getChildren().add(messageLabel);
        chatBox.getChildren().add(messageBox);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
