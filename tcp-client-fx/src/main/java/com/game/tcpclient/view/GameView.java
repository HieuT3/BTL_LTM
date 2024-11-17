package com.game.tcpclient.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        panel.setVisible(false);
        panelPlayAgain.setVisible(false);
        btnSubmit.setVisible(false);
        pbgTimer.setVisible(false);

        // close window event
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("LEAVE GAME");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure want to leave game? You will lose?");

            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                ClientRun.socketHandler.leaveGame(competitor);
                ClientRun.socketHandler.setRoomIdPresent(null);
                stage.close();
            } else {
                event.consume();
            }
        });
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

    private void initComponents() {
        // Label hiển thị thông tin người chơi
        infoPlayer = new Label("Play game with: hieucao135");
        infoPlayer.setFont(new Font("Tahoma", 18));

        // Nút thoát game
        btnLeaveGame = new Button("Leave Game");
        btnLeaveGame.setStyle("-fx-background-color: #FF3333; -fx-text-fill: white;");
        btnLeaveGame.setOnAction(evt -> btnLeaveGameActionPerformed());

        // Thanh Progress Bar cho bộ đếm thời gian
        pbgTimer = new ProgressBar(0);
        pbgTimer.setPrefWidth(200);

        // Label hiển thị thời gian còn lại
        lblTimer = new Label("02:27");
        lblTimer.setFont(new Font("Tahoma", 18));
        lblTimer.setTextFill(Color.rgb(255, 204, 51));

        // Panel chính chứa câu hỏi
        panel = new VBox(10);
        panel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        panel.setPadding(new Insets(10));

        // Center panel hiển thị các ô chữ trống
        centerPanel = new FlowPane();
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setHgap(10);

        // Bottom panel chứa các chữ cái xáo trộn
        bottomPanel = new FlowPane();
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setHgap(10);

        // Nút kiểm tra từ
        btnCheck = new Button("Check");
        btnCheck.setOnAction(evt -> btnCheckActionPerformed());

        // Nút bỏ qua từ hiện tại
        btnSkip = new Button("Skip");
        btnSkip.setOnAction(evt -> btnSkipActionPerformed());

        // Tạo layout cho panel câu hỏi
        HBox questionBox = new HBox(10, centerPanel, btnCheck, btnSkip);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPadding(new Insets(10));

        // Thêm các thành phần vào panel câu hỏi
        panel.getChildren().addAll(new Label("Question"), questionBox, bottomPanel);

        // Nút submit
        btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(evt -> btnSubmitActionPerformed());

        // Panel hỏi lại khi kết thúc game
        panelPlayAgain = new VBox(10);
        panelPlayAgain.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        panelPlayAgain.setPadding(new Insets(10));
        panelPlayAgain.setVisible(false);

        lbResult = new Label("Do you want to play again?");
        lbResult.setFont(new Font("Tahoma", 14));
        lbResult.setTextFill(Color.rgb(255, 204, 51));

        lbWaitingTimer = new Label("00:00");
        lbWaitingTimer.setFont(new Font("Tahoma", 14));
        lbWaitingTimer.setTextFill(Color.rgb(255, 204, 51));

        btnYes = new Button("Yes");
        btnYes.setOnAction(evt -> btnYesActionPerformed());

        btnNo = new Button("No");
        btnNo.setOnAction(evt -> btnNoActionPerformed());

        HBox playAgainButtons = new HBox(10, btnYes, btnNo);
        playAgainButtons.setAlignment(Pos.CENTER);

        HBox playAgainHeader = new HBox(10, lbResult, lbWaitingTimer);
        playAgainHeader.setAlignment(Pos.CENTER_LEFT);

        panelPlayAgain.getChildren().addAll(playAgainHeader, playAgainButtons);

        // Label chờ đợi người chơi khác
        lbWaiting = new Label("Waiting host start game....");
        lbWaiting.setFont(new Font("Tahoma", 18));

        // Nút bắt đầu game
        btnStart = new Button("Start");
        btnStart.setOnAction(evt -> btnStartActionPerformed());

        // Tạo bố cục chính
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(
                new HBox(10, infoPlayer, btnLeaveGame),
                pbgTimer,
                lblTimer,
                panel,
                new HBox(10, btnStart, lbWaiting, btnSubmit),
                panelPlayAgain
        );

        // Tạo scene và gán cho stage
        scene = new Scene(mainLayout, 800, 600);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Game");
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
