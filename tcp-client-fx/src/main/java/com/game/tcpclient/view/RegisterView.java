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

public class RegisterView {

    private Scene scene;
    private TextField tfUsername;
    private PasswordField tfPassword;
    private PasswordField tfConfirmPassword;

    public RegisterView() {
        initComponents();
    }

    private void initComponents() {
        // Tạo biểu tượng chính trên cùng
        ImageView mainIcon = new ImageView(new Image("register-icon.png"));
        mainIcon.setFitWidth(75);
        mainIcon.setFitHeight(75);

        Label lblTitle = new Label("REGISTER");
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

        // Confirm Password field with icon
        ImageView confirmPasswordIcon = new ImageView(new Image("password-icon.png"));
        confirmPasswordIcon.setFitWidth(30);
        confirmPasswordIcon.setFitHeight(30);
        confirmPasswordIcon.setClip(createCircularClip(confirmPasswordIcon.getFitWidth(), confirmPasswordIcon.getFitHeight()));

        tfConfirmPassword = new PasswordField();
        tfConfirmPassword.setPromptText("Confirm Password");
        tfConfirmPassword.setPrefWidth(320);
        tfConfirmPassword.setStyle("-fx-font-size: 14px; -fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 5;");

        GridPane confirmPasswordPane = new GridPane();
        confirmPasswordPane.setHgap(10);
        confirmPasswordPane.setAlignment(Pos.CENTER_LEFT);
        confirmPasswordPane.add(confirmPasswordIcon, 0, 0);
        confirmPasswordPane.add(tfConfirmPassword, 1, 0);

        // Buttons
        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 8 16;");
        btnRegister.setOnAction(e -> btnRegisterAction());

        Button btnChangeLogin = new Button("Login");
        btnChangeLogin.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 8 16;");
        btnChangeLogin.setOnAction(e -> btnChangeLoginAction());

        // Layout
        VBox root = new VBox(20, mainIcon, lblTitle, usernamePane, passwordPane, confirmPasswordPane, btnRegister, btnChangeLogin); // Chỉnh sửa khoảng cách giữa các thành phần bằng cách giảm VBox spacing từ 20 xuống 10
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10)); // Giảm padding trên và dưới bằng cách thay đổi từ 20 xuống 10
        root.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Tạo Scene từ layout
        scene = new Scene(root, 400, 500);
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

    private void btnRegisterAction() {
        String userName = tfUsername.getText();
        String password = tfPassword.getText();
        String confirmPassword = tfConfirmPassword.getText();

        if (userName.isEmpty()) {
            tfUsername.requestFocus();
        } else if (password.isEmpty()) {
            tfPassword.requestFocus();
        } else if (confirmPassword.isEmpty()) {
            tfConfirmPassword.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            showAlert("Confirm Password is incorrect!", "Error");
            tfConfirmPassword.requestFocus();
        } else {
            tfUsername.setText("");
            tfPassword.setText("");
            tfConfirmPassword.setText("");
            ClientRun.socketHandler.register(userName, password);
        }
    }

    private void btnChangeLoginAction() {
        ClientRun.openScene(ClientRun.SceneName.LOGIN);
    }

    // Hiển thị thông báo lỗi
    private void showAlert(String content, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

