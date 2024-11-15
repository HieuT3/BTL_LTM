package com.game.tcpclient.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.game.tcpclient.run.ClientRun;

public class MessageView {
    private Stage stage;
    private String userChat = "";
    private Scene scene;
    private VBox chatContent;
    private TextField tfMessage;
    private Label infoUserChat;

    public MessageView() {
        stage = new Stage();
        initComponents();
    }

    private void initComponents() {
        // Header Section
        Label lblTitle = new Label("Chat with: ");
        lblTitle.setFont(Font.font("Arial", 18));
        lblTitle.setTextFill(Color.DARKBLUE);

        infoUserChat = new Label();
        infoUserChat.setFont(Font.font("Arial", 18));
        infoUserChat.setTextFill(Color.BLACK);

        Button btnLeaveChat = new Button("Leave");
        btnLeaveChat.setStyle("-fx-background-color: linear-gradient(to right, #ffd194, #70e1f5); -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        btnLeaveChat.setOnAction(e -> leaveChat());

        HBox header = new HBox(10, lblTitle, infoUserChat, btnLeaveChat);
        HBox.setMargin(btnLeaveChat, new Insets(0, 0, 0, 70));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-width: 1px;");

        // Chat Content Section
        chatContent = new VBox(10);
        chatContent.setPadding(new Insets(10));
        chatContent.setStyle("-fx-background-color: transparent;");

        ScrollPane chatScrollPane = new ScrollPane(chatContent);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: transparent;");

        chatScrollPane.vvalueProperty().bind(chatContent.heightProperty()); // Auto-scroll to bottom

        // Message Input Section
        tfMessage = new TextField();
        tfMessage.setPromptText("Type your message...");
        tfMessage.setStyle("-fx-background-color: #ecf0f1; -fx-border-radius: 5; -fx-padding: 10;");
        tfMessage.setPrefWidth(520);
        tfMessage.setOnKeyPressed(this::handleKeyPress);

        Button btnSend = new Button("Send");
        btnSend.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5;");
        btnSend.setOnAction(e -> eventSendMessage());

        HBox messageBox = new HBox(10, tfMessage, btnSend);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(10));

        // Main Layout
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(chatScrollPane);
        root.setBottom(messageBox);
        root.setStyle("-fx-background-color: #ffffff;");

        scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Message View");
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setInfoUserChat(String username) {
        userChat = username;
        infoUserChat.setText(username);
    }

    public void addMessage(String message, boolean isUser) {
        Label lblMessage = new Label(message);
        lblMessage.setWrapText(true);
        lblMessage.setMaxWidth(300);
        lblMessage.setStyle("-fx-background-color: " + (isUser ? "#d6f5d6;" : "#fce4ec;") + " -fx-padding: 10; -fx-border-radius: 15; -fx-background-radius: 15;");
        lblMessage.setTextFill(Color.BLACK);

        HBox messageBox;
        if (!isUser) {
            ImageView userIcon = new ImageView(new Image("user-icon.png"));
            userIcon.setFitWidth(30);
            userIcon.setFitHeight(30);
            messageBox = new HBox(10, userIcon, lblMessage);
        } else {
            messageBox = new HBox(lblMessage);
        }
        messageBox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5));
        Platform.runLater(() -> chatContent.getChildren().add(messageBox));
    }

    public void setContentChat(String chat) {
        // Ensure only messages from others are displayed on the left
        if (!chat.startsWith("[" + userChat + "]")) {
            String messageWithoutPrefix = chat.replaceFirst("\\[.*?]:", "").trim();
            addMessage(messageWithoutPrefix, false);
        } else {
            String messageWithoutPrefix = chat.replaceFirst("\\[.*?]:", "").trim();
            addMessage(messageWithoutPrefix, true);
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            eventSendMessage();
        }
    }

    private void eventSendMessage() {
        String message = tfMessage.getText().trim();
        if (!message.isEmpty()) {
            ClientRun.socketHandler.sendMessage(infoUserChat.getText(), message);
            addMessage(message, true);
            tfMessage.clear();
        } else {
            tfMessage.requestFocus();
        }
    }

    private void leaveChat() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Leave chat?");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to leave chat?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                chatContent.getChildren().clear(); // Clear all chat messages
                ClientRun.socketHandler.leaveChat(userChat);
                Platform.runLater(() -> stage.close());
            }
        });
    }
}
