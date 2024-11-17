package com.game.tcpclient.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import com.game.tcpclient.run.ClientRun;

public class LoginView {
    private Scene scene;
    private TextField tfUsername;
    private PasswordField tfPassword;

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        // Tạo biểu tượng chính trên cùng
        ImageView mainIcon = new ImageView(new Image("user-icon.png"));
        mainIcon.setFitWidth(75);
        mainIcon.setFitHeight(75);

        Label lblTitle = new Label("LOGIN");
        lblTitle.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Username field with icon
        ImageView usernameIcon = new ImageView(new Image("user-icon.png"));
        usernameIcon.setFitWidth(30);
        usernameIcon.setFitHeight(30);
        usernameIcon.setClip(createCircularClip(usernameIcon.getFitWidth(), usernameIcon.getFitHeight()));

        tfUsername = new TextField();
        tfUsername.setPromptText("Username");
        tfUsername.setPrefWidth(320);
        tfUsername.setStyle("-fx-font-size: 14px; -fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 5;");

        GridPane usernamePane = new GridPane();
        usernamePane.setHgap(10);
        usernamePane.setAlignment(Pos.CENTER_LEFT);
        usernamePane.add(usernameIcon, 0, 0);
        usernamePane.add(tfUsername, 1, 0);

        // Password field with icon
        ImageView passwordIcon = new ImageView(new Image("password-icon.png"));
        passwordIcon.setFitWidth(30);
        passwordIcon.setFitHeight(30);
        passwordIcon.setClip(createCircularClip(passwordIcon.getFitWidth(), passwordIcon.getFitHeight()));

        tfPassword = new PasswordField();
        tfPassword.setPromptText("Password");
        tfPassword.setPrefWidth(320);
        tfPassword.setStyle("-fx-font-size: 14px; -fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 5;");

        GridPane passwordPane = new GridPane();
        passwordPane.setHgap(10);
        passwordPane.setAlignment(Pos.CENTER_LEFT);
        passwordPane.add(passwordIcon, 0, 0);
        passwordPane.add(tfPassword, 1, 0);

        // Buttons
        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 8 16;");
        btnLogin.setOnAction(e -> btnLoginAction());

        Button btnChangeRegister = new Button("Register");
        btnChangeRegister.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 8 16;");
        btnChangeRegister.setOnAction(e -> btnChangeRegisterAction());

        // Layout
        VBox root = new VBox(20, mainIcon, lblTitle, usernamePane, passwordPane, btnLogin, btnChangeRegister);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Tạo Scene từ layout
        scene = new Scene(root, 400, 450);
    }

    private Rectangle createCircularClip(double width, double height) {
        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(width);
        clip.setArcHeight(height);
        return clip;
    }

    public Scene getScene() {
        return scene;
    }

    private void btnLoginAction() {
        String userName = tfUsername.getText();
        String password = tfPassword.getText();

        if (userName.isEmpty()) {
            tfUsername.requestFocus();
        } else if (password.isEmpty()) {
            tfPassword.requestFocus();
        } else {
            tfUsername.setText("");
            tfPassword.setText("");
            ClientRun.socketHandler.login(userName, password);
        }
    }

    private void btnChangeRegisterAction() {
        ClientRun.openScene(ClientRun.SceneName.REGISTER);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
