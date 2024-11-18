package com.game.tcpclient.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.game.tcpclient.run.ClientRun;

import java.util.Vector;

public class HomeView {

    private Stage stage;
    private Scene scene;
    private TableView<String> tblUser;
    private Label infoUsername;
    private Label infoUserScore;
    private Label infoUserWin;
    private Label infoUserDraw;
    private Label infoUserLose;
    private String statusCompetitor = "";

    public HomeView() {
        initComponents();
    }

    private void initComponents() {
        // Header
        Text headerTitle = new Text("User Dashboard");
        headerTitle.setFont(Font.font("Verdana", 36));
        headerTitle.setFill(Color.WHITE);

        HBox header = new HBox(headerTitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #8e44ad, #3498db); -fx-border-radius: 10; -fx-background-radius: 10;");

        // User Info Box
        VBox userInfoBox = new VBox(10);
        userInfoBox.setAlignment(Pos.TOP_CENTER);
        userInfoBox.setPadding(new Insets(15));
        userInfoBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #8e44ad; -fx-border-width: 2px; -fx-border-radius: 10; -fx-background-radius: 10;");

        infoUsername = new Label("Welcome, User");
        infoUsername.setFont(Font.font("Verdana", 18));
        infoUsername.setTextFill(Color.DARKBLUE);

        infoUserScore = new Label("Score: 0.0");
        infoUserScore.setFont(Font.font("Verdana", 18));
        infoUserScore.setTextFill(Color.DARKBLUE);

        infoUserWin = new Label("Wins: 0");
        infoUserWin.setFont(Font.font("Verdana", 18));
        infoUserWin.setTextFill(Color.DARKBLUE);

        infoUserDraw = new Label("Draws: 0");
        infoUserDraw.setFont(Font.font("Verdana", 18));
        infoUserDraw.setTextFill(Color.DARKBLUE);

        infoUserLose = new Label("Losses: 0");
        infoUserLose.setFont(Font.font("Verdana", 18));
        infoUserLose.setTextFill(Color.DARKBLUE);

        userInfoBox.getChildren().addAll(infoUsername, infoUserScore, infoUserWin, infoUserDraw, infoUserLose);

        Button btnExit = new Button("Exit");
        btnExit.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        btnExit.setOnAction(e -> btnExitAction());
        userInfoBox.getChildren().add(btnExit);

        // User Table
        tblUser = new TableView<>();
        TableColumn<String, String> userColumn = new TableColumn<>("Users Online");
        userColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));
        tblUser.getColumns().add(userColumn);
        tblUser.setPrefHeight(300);
        tblUser.setStyle("-fx-border-color: #3498db; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Action Cards
        VBox actionCardPlay = createActionCard("Play", "#27ae60", "Start a game", this::btnPlayAction);
        VBox actionCardMessage = createActionCard("Message", "#2980b9", "Chat with a user", this::btnMessageAction);
        VBox actionCardRanking = createActionCard("Ranking", "#456358", "Ranking", this::btnRankingAction);
        VBox actionCardRefresh = createActionCard("Refresh", "#f1c40f", "Refresh user list", this::btnRefreshAction);
        VBox actionCardInfo = createActionCard("Info", "#9b59b6", "View user info", this::btnGetInfoAction);
        VBox actionCardLogout = createActionCard("Logout", "#e74c3c", "Sign out", this::btnLogoutAction);

        HBox actionCards = new HBox(20, actionCardPlay, actionCardMessage, actionCardRanking, actionCardRefresh, actionCardInfo, actionCardLogout);
        actionCards.setAlignment(Pos.CENTER);
        actionCards.setPadding(new Insets(20));

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setCenter(tblUser);
        mainLayout.setBottom(actionCards);
        mainLayout.setRight(userInfoBox);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f4f6f7;");


        // Scene
        scene = new Scene(mainLayout, 1000, 700);
        stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            event.consume();
            btnExitAction();
        });
        stage.centerOnScreen();
    }

    private VBox createActionCard(String title, String color, String description, Runnable action) {
        Label cardTitle = new Label(title);
        cardTitle.setFont(Font.font("Verdana", 18));
        cardTitle.setTextFill(Color.WHITE);

        Label cardDescription = new Label(description);
        cardDescription.setFont(Font.font("Verdana", 12));
        cardDescription.setTextFill(Color.WHITE);

        VBox card = new VBox(10, cardTitle, cardDescription);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 10; -fx-background-radius: 10;");

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #333333; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, #000000, 10, 0.5, 0, 0);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseClicked(e -> action.run());

        return card;
    }

    public Scene getScene() {
        return scene;
    }

    public void setStatusCompetitor(String status) {
        statusCompetitor = status;
    }

    public void setListUser(Vector<String> vdata) {
        tblUser.getItems().clear(); // Xóa các mục hiện tại
        tblUser.getItems().addAll(vdata);
    }

    public void resetTblUser() {
        tblUser.getItems().clear(); // Xóa bảng người dùng
    }

    public void setUsername(String username) {
        infoUsername.setText("Hello: " + username);
    }

    public void setUserScore(float score) {
        infoUserScore.setText("Score: " + score);
    }

    public void setUserWin(int win) {
        infoUserWin.setText("Win: " + win);
    }

    public void setUserDraw(int draw) {
        infoUserDraw.setText("Draw: " + draw);
    }

    public void setUserLose(int lose) {
        infoUserLose.setText("Lose: " + lose);
    }

    // Các phương thức hành động
    private void btnPlayAction() {
        String userSelected = tblUser.getSelectionModel().getSelectedItem();
        if (userSelected == null) {
            showAlert("You haven't chosen anyone yet! Please select one user.", "ERROR");
        } else {
            // Kiểm tra trạng thái người chơi
            ClientRun.socketHandler.checkStatusUser(userSelected);
            switch (statusCompetitor) {
                case "ONLINE":
                    ClientRun.socketHandler.inviteToPlay(userSelected);
                    break;
                case "OFFLINE":
                    showAlert("This user is offline.", "ERROR");
                    break;
                case "INGAME":
                    showAlert("This user is in game.", "ERROR");
                    break;
            }
        }
    }

    private void btnMessageAction() {
        String userSelected = tblUser.getSelectionModel().getSelectedItem();
        if (userSelected == null) {
            showAlert("You haven't chosen anyone yet! Please select one user.", "ERROR");
        } else {
            if (userSelected.equals(ClientRun.socketHandler.getLoginUser())) {
                showAlert("You cannot chat with yourself.", "ERROR");
            } else {
                ClientRun.socketHandler.inviteToChat(userSelected);
            }
        }
    }

    private void btnRankingAction() {
        ClientRun.socketHandler.getRanking();
    }

    private void btnRefreshAction() {
        // Gửi yêu cầu lấy danh sách người dùng online
        ClientRun.socketHandler.getListOnline();
    }

    private void btnLogoutAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Confirm if you want Logout");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ClientRun.socketHandler.logout();
            }
        });
    }

    private void btnGetInfoAction() {
        String userSelected = tblUser.getSelectionModel().getSelectedItem();
        if (userSelected == null) {
            showAlert("You haven't chosen anyone yet! Please select one user.", "ERROR");
        } else {
            if (userSelected.equals(ClientRun.socketHandler.getLoginUser())) {
                showAlert("You cannot see yourself.", "ERROR");
            } else {
                ClientRun.socketHandler.getInfoUser(userSelected);
            }
        }
    }

    private void btnExitAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("EXIT");
        alert.setHeaderText(null);
        alert.setContentText("Confirm if you want to exit");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ClientRun.socketHandler.close();
                System.exit(0);
            }
        });
    }

    private void showAlert(String content, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
