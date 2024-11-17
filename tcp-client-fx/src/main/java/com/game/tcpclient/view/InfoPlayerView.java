package com.game.tcpclient.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoPlayerView {

    private Scene scene;
    private Label lblScoreValue = new Label("0");
    private Label lblWinValue = new Label("0");
    private Label lblDrawValue = new Label("0");
    private Label lblLoseValue = new Label("0");
    private Label lblAvgTimeValue = new Label("0.0s");
    private Label lblAvgCompetitorValue = new Label("0.0");
    private Label infoUserName = new Label("Username");
    private Label infoUserStatus = new Label("Status");

    public InfoPlayerView() {
        initComponents();
    }

    private void initComponents() {
        // Header Section
        Label lblTitle = new Label("Player Info");
        lblTitle.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        ImageView icon = new ImageView(new Image("user-icon.png"));
        icon.setFitWidth(50);
        icon.setFitHeight(50);

        HBox header = new HBox(10, icon, lblTitle);
        header.setAlignment(Pos.CENTER);

        // Personal Information Section
        GridPane personalInfoGrid = new GridPane();
        personalInfoGrid.setPadding(new Insets(20));
        personalInfoGrid.setHgap(10);
        personalInfoGrid.setVgap(15);

        addField(personalInfoGrid, "Username:", infoUserName, 0, 0);
        addField(personalInfoGrid, "Account status:", infoUserStatus, 1, 0);

        personalInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-border-radius: 10; -fx-padding: 15;");

        // Stats Section Split Across Two Rows
        GridPane statsGrid = new GridPane();
        statsGrid.setPadding(new Insets(20));
        statsGrid.setHgap(15);
        statsGrid.setVgap(15);

        statsGrid.add(createStatCard("Score", lblScoreValue, "fire-icon.png"), 0, 0);
        statsGrid.add(createStatCard("Win", lblWinValue, "win-icon.png"), 1, 0);
        statsGrid.add(createStatCard("Draw", lblDrawValue, "draw-icon.jpg"), 2, 0);
        statsGrid.add(createStatCard("Lose", lblLoseValue, "lose-icon.png"), 0, 1);
        statsGrid.add(createStatCard("AVG Time", lblAvgTimeValue, "clock-icon.png"), 1, 1);
        statsGrid.add(createStatCard("AVG Competitor", lblAvgCompetitorValue, "competitor-icon.jpg"), 2, 1);

        statsGrid.setAlignment(Pos.CENTER);

        // OK Button
        Button btnOk = new Button("OK");
        btnOk.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        btnOk.setOnAction(e -> close());

        VBox layout = new VBox(20, header, personalInfoGrid, statsGrid, btnOk);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f7f9fc; -fx-border-radius: 10; -fx-background-radius: 10;");

        scene = new Scene(layout, 600, 700);
    }

    private void addField(GridPane grid, String labelText, Label valueLabel, int row, int col) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        if (labelText.equals("Account status:")) {
            valueLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2ecc71; -fx-background-color: #d4f5d1; -fx-border-color: #2ecc71; -fx-border-radius: 5; -fx-padding: 5 10; -fx-background-radius: 5;");
        } else {
            valueLabel.setStyle("-fx-font-size: 14px; -fx-border-color: #bdc3c7; -fx-background-color: #ffffff; -fx-border-radius: 5; -fx-padding: 5 10; -fx-background-radius: 5;");
        }
        grid.add(label, col * 2, row);
        grid.add(valueLabel, col * 2 + 1, row);
    }

    private VBox createStatCard(String title, Label valueLabel, String iconPath) {
        ImageView icon = new ImageView(new Image(iconPath));
        icon.setFitWidth(40);
        icon.setFitHeight(40);

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");

        VBox card = new VBox(7, icon, valueLabel, lblTitle);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefSize(150, 100);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-border-radius: 10; -fx-background-radius: 10;");

        return card;
    }

    public Scene getScene() {
        return scene;
    }

    public void setInfoUser(String username, String score, String win, String draw, String lose, String avgCompetitor, String avgTime, String status) {
        infoUserName.setText(username);
        infoUserStatus.setText(status);
        lblScoreValue.setText(score);
        lblWinValue.setText(win);
        lblDrawValue.setText(draw);
        lblLoseValue.setText(lose);
        lblAvgTimeValue.setText(avgTime);
        lblAvgCompetitorValue.setText(avgCompetitor);
    }

    public void show(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(getScene());
        dialog.setTitle("Player Information");
        dialog.centerOnScreen();
        dialog.show();
    }

    private void close() {
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
