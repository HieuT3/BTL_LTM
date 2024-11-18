package com.game.tcpclient.view;


import com.game.tcpclient.controller.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.List;

public class RankingView {

    private Stage stage;
    private Scene scene;
    private TableView<User> rankingTable;
    private BorderPane mainLayout;

    public RankingView() {
        initComponents();
    }

    private void initComponents() {
        // Header
        Label headerTitle = new Label("Bảng Xếp Hạng");
        headerTitle.setFont(Font.font("Verdana", 36));
        headerTitle.setTextFill(Color.WHITE);

        HBox header = new HBox(headerTitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #8e44ad, #3498db); -fx-border-radius: 10; -fx-background-radius: 10;");

        // Set up the ranking table
        rankingTable = new TableView<>();
        rankingTable.setStyle("-fx-background-color: #ffffff; -fx-table-cell-border-color: transparent;");
        rankingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<User, Integer> winCol = new TableColumn<>("Win");
        winCol.setCellValueFactory(new PropertyValueFactory<>("win"));

        TableColumn<User, Integer> drawCol = new TableColumn<>("Draw");
        drawCol.setCellValueFactory(new PropertyValueFactory<>("draw"));

        TableColumn<User, Integer> loseCol = new TableColumn<>("Lose");
        loseCol.setCellValueFactory(new PropertyValueFactory<>("lose"));

        TableColumn<User, Double> avgCompetitorCol = new TableColumn<>("Avg Competitor");
        avgCompetitorCol.setCellValueFactory(new PropertyValueFactory<>("avgCompetitor"));

        TableColumn<User, Double> avgTimeCol = new TableColumn<>("Avg Time");
        avgTimeCol.setCellValueFactory(new PropertyValueFactory<>("avgTime"));

        rankingTable.getColumns().addAll(usernameCol, scoreCol, winCol, drawCol, loseCol, avgCompetitorCol, avgTimeCol);
        rankingTable.setPrefHeight(300);

        VBox tableBox = new VBox();
        tableBox.setPadding(new Insets(20));
        tableBox.setSpacing(10);
        tableBox.getChildren().add(rankingTable);
        tableBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #87ceeb; -fx-border-radius: 5; -fx-padding: 20;");

        // Main Layout
        mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setCenter(tableBox);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f4f6f7;");

        // Scene
        scene = new Scene(mainLayout, 900, 700);
        stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void setRankingTable(List<User> userList) {
        ObservableList<User> users = FXCollections.observableArrayList(userList);
        rankingTable.setItems(users);
    }

    public Stage getStage() {
        return this.stage;
    }
}
