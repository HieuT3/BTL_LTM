package com.game.tcpclient.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.game.tcpclient.run.ClientRun;

public class ConnectServer {

    private Scene scene;
    private TextField txIP;
    private TextField txPort;

    public ConnectServer() {
        initComponents();
    }

    private void initComponents() {
        // Khởi tạo các thành phần giao diện
        Label lblTitle = new Label("CONNECT TO SERVER");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label lblIP = new Label("IP");
        lblIP.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        txIP = new TextField("127.0.0.1");
        txIP.setPrefWidth(200);
        txIP.setStyle("-fx-font-size: 14px; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5;");

        Label lblPort = new Label("PORT");
        lblPort.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        txPort = new TextField("2000");
        txPort.setPrefWidth(200);
        txPort.setStyle("-fx-font-size: 14px; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5;");

        Button btnConnect = new Button("CONNECT");
        btnConnect.setStyle("-fx-background-color: linear-gradient(to right, #3498db, #2ecc71); -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8 16;");
        btnConnect.setOnAction(e -> btnConnectAction());

        // Sắp xếp layout bằng GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-background-radius: 10;");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        col1.setHgrow(Priority.NEVER);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(lblIP, 0, 0);
        grid.add(txIP, 1, 0);
        grid.add(lblPort, 0, 1);
        grid.add(txPort, 1, 1);

        // Gộp tất cả các thành phần vào VBox để căn giữa dọc
        VBox root = new VBox(20, lblTitle, grid, btnConnect);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Tạo Scene từ layout và lưu trữ
        scene = new Scene(root, 400, 300);
    }

    public Scene getScene() {
        return scene;
    }

    private void btnConnectAction() {
        String ip;
        int port;

        // Kiểm tra đầu vào
        try {
            ip = txIP.getText();
            port = Integer.parseInt(txPort.getText());

            if (port < 0 || port > 65535) {
                showAlert("Sai port", "Port phải từ 0 - 65535");
                txPort.requestFocus();
                return;
            }

        } catch (NumberFormatException e) {
            showAlert("Sai port", "Port phải là số nguyên");
            txPort.requestFocus();
            return;
        }

        System.out.println("Kết nối tới server");
        connect(ip, port);
    }

    private void connect(String ip, int port) {
        // Tạo luồng để kết nối tới server
        System.out.println();
        new Thread(() -> {
            String result = ClientRun.socketHandler.connect(ip, port);
            if (result.equals("success")) {
                onSuccess();
            } else {
                String failedMsg = result.split(";")[1];
                onFailed(failedMsg);
            }
        }).start();
    }

    private void onSuccess() {
        javafx.application.Platform.runLater(() -> {
            ClientRun.openScene(ClientRun.SceneName.LOGIN);
            System.out.println("Kết nối thành công đến server");
        });
    }

    private void onFailed(String failedMsg) {
        javafx.application.Platform.runLater(() -> showAlert("Lỗi kết nối", failedMsg));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
