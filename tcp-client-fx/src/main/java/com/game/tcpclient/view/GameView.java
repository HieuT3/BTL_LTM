package com.game.tcpclient.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.game.tcpclient.helper.CountDownTimer;
import com.game.tcpclient.helper.CustumDateTimeFormatter;
import com.game.tcpclient.run.ClientRun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class GameView {

    public void resetComponents() {
        Platform.runLater(() -> {
            i = 0;
            res = "";
            answer = false;
            centerLabels.clear();
            letterButtons.clear();
            centerToBottomMap.clear();

            // Hide panels and buttons initially
            panel.setVisible(false);
            panelPlayAgain.setVisible(false);
            btnSubmit.setVisible(false);
            pbgTimer.setProgress(0);
            pbgTimer.setVisible(true);
            lbWaiting.setText("Waiting host start game....");
            lbWaiting.setVisible(true);
            lbWaitingTimer.setVisible(false);

            // Clear center and bottom panels
            centerPanel.getChildren().clear();
            bottomPanel.getChildren().clear();
            panel.setPrefWidth(scene.getWidth() * 3 / 4);
        });
    }

    private FlowPane bottomPanel;
    private Button btnCheck;
    private Button btnLeaveGame;
    private Button btnNo;
    private Button btnSkip;
    private Button btnStart;
    private Button btnSubmit;
    private Button btnYes;
    private FlowPane centerPanel;
    private Label infoPlayer;
    private Label lbResult;
    private Label lbWaiting;
    private Label lbWaitingTimer;
    private VBox panel;
    private VBox panelPlayAgain;
    public static ProgressBar pbgTimer;
    public static Label lblTimer;
    private Scene scene;

    String competitor = "";
    CountDownTimer matchTimer;
    CountDownTimer waitingClientTimer;
    private ArrayList<Label> centerLabels;
    private ArrayList<Button> letterButtons;
    private String[] words;
    private int i = 0;
    private Map<Label, Button> centerToBottomMap = new HashMap<>();
    private String res = "";

    boolean answer = false;

    private Stage stage;

    public GameView() {
        initComponents();

        centerLabels = new ArrayList<>();
        letterButtons = new ArrayList<>();

        panel.setPrefWidth(1000);
        panel.setVisible(false);

        panelPlayAgain.setVisible(false);
        btnSubmit.setVisible(false);
        pbgTimer.setVisible(false);
        lblTimer.setVisible(false);
        lbWaiting.setVisible(false);
        lbWaitingTimer.setVisible(false);

        // close window event
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("LEAVE GAME");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to leave the game? You will lose.");

            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                ClientRun.socketHandler.leaveGame(competitor);
                ClientRun.socketHandler.setRoomIdPresent(null);
                stage.close();
            } else {
                event.consume();
            }
        });
    }

    private void initComponents() {
        // Main layout styling
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setBackground(new Background(new BackgroundFill(Color.web("#f0f8ff"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Header - Player Info, Timer, and Leave Button
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #333;");

        HBox playerInfoBox = new HBox(20);
        playerInfoBox.setAlignment(Pos.CENTER);

        infoPlayer = new Label("Play game with: hieucao135");
        infoPlayer.setFont(new Font("Arial", 20));
        infoPlayer.setTextFill(Color.WHITE);

        btnLeaveGame = new Button("Leave Game");
        btnLeaveGame.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 5, 0, 0, 1);");
        btnLeaveGame.setOnAction(evt -> btnLeaveGameActionPerformed());

        playerInfoBox.getChildren().addAll(infoPlayer, btnLeaveGame);

        // Timer Panel
        HBox timerPanel = new HBox(20);
        timerPanel.setAlignment(Pos.CENTER);
        timerPanel.setPadding(new Insets(10));
        timerPanel.setBackground(new Background(new BackgroundFill(Color.web("#e0f7fa"), new CornerRadii(10), Insets.EMPTY)));

        pbgTimer = new ProgressBar(0);
        pbgTimer.setPrefWidth(300);
        pbgTimer.setStyle("-fx-accent: #32cd32;");

        lblTimer = new Label("02:27");
        lblTimer.setFont(new Font("Arial", 24));
        lblTimer.setTextFill(Color.rgb(255, 69, 0));
        lblTimer.setVisible(false);
        timerPanel.getChildren().addAll(pbgTimer);

        header.getChildren().addAll(playerInfoBox, timerPanel);
        mainLayout.setTop(header);

        // Game Panel
        panel = new VBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(900);
        panel.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), Insets.EMPTY)));
        panel.setEffect(new DropShadow(10, Color.GRAY));

        centerPanel = new FlowPane();
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setPrefWidth(400);
        centerPanel.setHgap(15);
        centerPanel.setVgap(15);
        centerPanel.setPadding(new Insets(10));
        centerPanel.setBackground(new Background(new BackgroundFill(Color.web("#fafad2"), new CornerRadii(10), Insets.EMPTY)));

        bottomPanel = new FlowPane();
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setHgap(15);
        bottomPanel.setVgap(15);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setBackground(new Background(new BackgroundFill(Color.web("#fafad2"), new CornerRadii(10), Insets.EMPTY)));

        // Add Question Controls
        btnCheck = new Button("Check");
        btnCheck.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 16; -fx-pref-width: 100px; -fx-pref-height: 40px;");
        btnCheck.setOnAction(evt -> btnCheckActionPerformed());

        btnSkip = new Button("Skip");
        btnSkip.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16; -fx-pref-width: 100px; -fx-pref-height: 40px;");
        btnSkip.setOnAction(evt -> btnSkipActionPerformed());

        HBox questionBox = new HBox(20, centerPanel, btnCheck, btnSkip);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPadding(new Insets(10));
        questionBox.setBackground(new Background(new BackgroundFill(Color.web("#e0f7fa"), new CornerRadii(10), Insets.EMPTY)));

        panel.getChildren().addAll(new Label("Question"), questionBox, bottomPanel);
        mainLayout.setCenter(panel);

        // Bottom Controls
        HBox controls = new HBox(20);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));
        controls.setBackground(new Background(new BackgroundFill(Color.web("#e0f7fa"), new CornerRadii(10), Insets.EMPTY)));

        btnStart = new Button("Start");
        btnStart.setStyle("-fx-background-color: #32cd32; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 5, 0, 0, 1);");
        btnStart.setOnAction(evt -> btnStartActionPerformed());

        btnSubmit = new Button("Submit");
        btnSubmit.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 5, 0, 0, 1);");
        btnSubmit.setOnAction(evt -> btnSubmitActionPerformed());

        lbWaiting = new Label("Waiting host start game....");
        lbWaiting.setFont(new Font("Arial", 18));
        lbWaiting.setTextFill(Color.rgb(255, 204, 51));

        lbWaitingTimer = new Label("00:00");
        lbWaitingTimer.setFont(new Font("Arial", 14));
        lbWaitingTimer.setTextFill(Color.rgb(255, 204, 51));

        controls.getChildren().addAll(btnStart, lbWaiting, lbWaitingTimer, btnSubmit);
        mainLayout.setBottom(controls);

        // Ask Play Again Panel
        panelPlayAgain = new VBox(20);
        panelPlayAgain.setAlignment(Pos.CENTER);
        panelPlayAgain.setPadding(new Insets(20));
        panelPlayAgain.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        panelPlayAgain.setEffect(new DropShadow(10, Color.GRAY));
        panelPlayAgain.setVisible(false);

        lbResult = new Label("Do you want to play again?");
        lbResult.setFont(new Font("Arial", 20));
        lbResult.setTextFill(Color.DARKBLUE);

        btnYes = new Button("Yes");
        btnYes.setStyle("-fx-background-color: #32cd32; -fx-text-fill: white; -fx-font-weight: bold;");
        btnYes.setOnAction(evt -> btnYesActionPerformed());

        btnNo = new Button("No");
        btnNo.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNo.setOnAction(evt -> btnNoActionPerformed());

        HBox playAgainControls = new HBox(20, btnYes, btnNo);
        playAgainControls.setAlignment(Pos.CENTER);

        panelPlayAgain.getChildren().addAll(lbResult, playAgainControls);
        mainLayout.setRight(panelPlayAgain);

        // Scene and Stage Setup
        scene = new Scene(mainLayout, 1000, 700);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Game");
    }

    public void setWaitingRoom() {
        Platform.runLater(() -> {
            panel.setVisible(false);
            btnSubmit.setVisible(false);
            pbgTimer.setVisible(false);
            btnStart.setVisible(false);
            lbWaiting.setText("waiting competitor...");
            waitingReplyClient();
        });
    }

    public void showAskPlayAgain(String msg) {
        Platform.runLater(() -> {
            panelPlayAgain.setVisible(true);
            lbResult.setText(msg);
        });
    }

    public void hideAskPlayAgain() {
        panelPlayAgain.setVisible(false);
    }

    public void setInfoPlayer(String username) {
        competitor = username;
        infoPlayer.setText("Play game with: " + username);
    }

    public void setStateHostRoom() {
        answer = false;
        btnStart.setVisible(true);
        lbWaiting.setVisible(false);
    }

    public void setStateUserInvited() {
        answer = false;
        btnStart.setVisible(false);
        lbWaiting.setVisible(true);
    }

    public void afterSubmit() {
        panel.setVisible(false);
        btnSubmit.setVisible(false);
        lbWaiting.setVisible(true);

        if (i < words.length) {
            for (int t = i; t <= words.length; t += 1) {
                res += "0";
            }
        }
        System.out.println("Results: " + res);
        lbWaiting.setText("Waiting result from server...");
    }

    public void setStartGame(int matchTimeLimit) {
        Platform.runLater(() -> {
            answer = false;

            btnStart.setVisible(false);
            lbWaiting.setVisible(false);
            panel.setVisible(true);
            btnSubmit.setVisible(true);
            pbgTimer.setVisible(true);
            lblTimer.setVisible(true);

            matchTimer = new CountDownTimer(matchTimeLimit);
            matchTimer.setTimerCallBack(
                    null,
                    (Callable<Void>) () -> {
                        Platform.runLater(() -> {
                            pbgTimer.setProgress((double) matchTimer.getCurrentTick() / matchTimer.getTimeLimit());
                            lblTimer.setText(CustumDateTimeFormatter.secondsToMinutes(matchTimer.getCurrentTick()));
                            if ("00:00".equals(lblTimer.getText())) {
                                afterSubmit();
                            }
                        });
                        return null;
                    },
                    1
            );

            loadNextWord();
        });
    }

    public void waitingReplyClient() {
        waitingClientTimer = new CountDownTimer(10);
        waitingClientTimer.setTimerCallBack(
                null,
                (Callable<Void>) () -> {
                    Platform.runLater(() -> {
                        lbWaitingTimer.setText(CustumDateTimeFormatter.secondsToMinutes(waitingClientTimer.getCurrentTick()));
                        if (lbWaitingTimer.getText().equals("00:00") && !answer) {
                            hideAskPlayAgain();
                        }
                    });
                    return null;
                },
                1
        );
    }

    public void showMessage(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }

    public void pauseTime() {
        matchTimer.pause();
    }

    private String[] getScrambledLetters() {
        String word = words[i++];
        System.out.println("-------" + word + "--------");
        ArrayList<String> letters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            letters.add(String.valueOf(c));
        }
        Collections.shuffle(letters);
        return letters.toArray(new String[0]);
    }

    private void loadNextWord() {
        if(i >= words.length) return;
        Platform.runLater(() -> {
            String[] scrambledLetters = getScrambledLetters();

            // Cập nhật lại các ô hiển thị ở giữa màn hình
            centerPanel.getChildren().clear();
            centerLabels.clear();

            for (String letter : scrambledLetters) {
                Label label = new Label("");
                label.setAlignment(Pos.CENTER);
                label.setPrefSize(50, 50);
                label.setFont(new Font("Arial", 25));
                label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

                // Thêm sự kiện nhấn vào label ở centerPanel
                label.setOnMouseClicked(new CenterLabelMouseHandler(label));

                centerPanel.getChildren().add(label);
                centerLabels.add(label);
            }

            // Cập nhật lại các nút chữ cái ở dưới màn hình
            bottomPanel.getChildren().clear();
            letterButtons.clear();

            for (String letter : scrambledLetters) {
                Button button = new Button(letter);
                button.setPrefSize(50, 50);
                button.setFont(new Font("Arial", 25));
                button.setOnAction(new LetterButtonHandler());
                bottomPanel.getChildren().add(button);
                letterButtons.add(button);
            }
        });
    }

    private class CenterLabelMouseHandler implements EventHandler<MouseEvent> {
        private final Label label;

        public CenterLabelMouseHandler(Label label) {
            this.label = label;
        }

        @Override
        public void handle(MouseEvent e) {
            Label clickedLabel = (Label) e.getSource();
            String letter = clickedLabel.getText();

            if (!letter.isEmpty()) {
                clickedLabel.setText("");

                Button correspondingButton = centerToBottomMap.get(clickedLabel);
                if (correspondingButton != null) {
                    correspondingButton.setText(letter);
                }
            }
        }
    }

    private class LetterButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            Button button = (Button) e.getSource();
            String letter = button.getText();

            for (Label label : centerLabels) {
                if (label.getText().isEmpty()) {
                    label.setText(letter);
                    centerToBottomMap.put(label, button);
                    button.setText("");
                    break;
                }
            }
        }
    }

    private void checkWord() {
        StringBuilder sb = new StringBuilder();
        for (Label label : centerLabels) {
            sb.append(label.getText());
        }

        String guessedWord = sb.toString();
        String correctWord = words[i - 1];

        if (guessedWord.equals(correctWord)) {
            showAlert("Correct!", Alert.AlertType.INFORMATION);
            res += "1";
            loadNextWord();
        } else {
            showAlert("Incorrect!", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void btnLeaveGameActionPerformed() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("LEAVE GAME");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to leave game? You will lose?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ClientRun.socketHandler.leaveGame(competitor);
                ClientRun.socketHandler.setRoomIdPresent(null);
                stage.close();
            }
        });
    }

    private void btnStartActionPerformed() {
        ClientRun.socketHandler.startGame(competitor);
        resetComponents();
    }

    private void btnSubmitActionPerformed() {
        ClientRun.socketHandler.submitResult(competitor);
    }

    private void btnNoActionPerformed() {
        ClientRun.socketHandler.notAcceptPlayAgain();
        answer = true;
        hideAskPlayAgain();
    }

    private void btnYesActionPerformed() {
        ClientRun.socketHandler.acceptPlayAgain();
        answer = true;
        hideAskPlayAgain();
        resetComponents();
    }

    private void btnSkipActionPerformed() {
        res += "0";
        if (i >= words.length) {
            showMessage("Bạn đang ở câu hỏi cuối cùng.");

        } else {
            loadNextWord();
        }
    }

    private void btnCheckActionPerformed() {
        checkWord();
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public String getResults() {
        return res;
    }

    public void setQuestion(String[] s) {
        words = s;
    }

    public Scene getScene() {
        return this.scene;
    }

    public Stage getStage() {
        return stage;
    }
}
