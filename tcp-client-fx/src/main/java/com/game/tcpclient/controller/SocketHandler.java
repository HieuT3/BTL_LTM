package com.game.tcpclient.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import com.game.tcpclient.run.ClientRun;

public class SocketHandler {

    Socket s;
    DataInputStream dis;
    DataOutputStream dos;

    String loginUser = null; // lưu tài khoản đăng nhập hiện tại
    String roomIdPresent = null; // lưu room hiện tại
    float score = 0;

    Thread listener = null;

    private void showAlert(String content, String title) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public String connect(String addr, int port) {
        try {
            // getting ip
            InetAddress ip = InetAddress.getByName(addr);

            // establish the connection with server port
            s = new Socket();
            s.connect(new InetSocketAddress(ip, port), 4000);
            System.out.println("Connected to " + ip + ":" + port + ", local port:" + s.getLocalPort());

            // obtaining input and output streams
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            // close old listener
            if (listener != null && listener.isAlive()) {
                listener.interrupt();
            }

            // listen to server
            listener = new Thread(this::listen);
            listener.start();

            // connect success
            return "success";

        } catch (IOException e) {

            // connect failed
            return "failed;" + e.getMessage();
        }
    }

    private void listen() {
        boolean running = true;

        while (running) {
            try {
                // receive the data from server
                String received = dis.readUTF();

                System.out.println("RECEIVED: " + received);

                String type = received.split(";")[0];

                switch (type) {
                    case "LOGIN":
                        onReceiveLogin(received);
                        break;
                    case "REGISTER":
                        onReceiveRegister(received);
                        break;
                    case "GET_LIST_ONLINE":
                        onReceiveGetListOnline(received);
                        break;
                    case "LOGOUT":
                        onReceiveLogout(received);
                        break;
                    case "INVITE_TO_CHAT":
                        onReceiveInviteToChat(received);
                        break;
                    case "GET_INFO_USER":
                        onReceiveGetInfoUser(received);
                        break;
                    case "ACCEPT_MESSAGE":
                        onReceiveAcceptMessage(received);
                        break;
                    case "NOT_ACCEPT_MESSAGE":
                        onReceiveNotAcceptMessage(received);
                        break;
                    case "LEAVE_TO_CHAT":
                        onReceiveLeaveChat(received);
                        break;
                    case "CHAT_MESSAGE":
                        onReceiveChatMessage(received);
                        break;
                    case "INVITE_TO_PLAY":
                        onReceiveInviteToPlay(received);
                        break;
                    case "ACCEPT_PLAY":
                        onReceiveAcceptPlay(received);
                        break;
                    case "NOT_ACCEPT_PLAY":
                        onReceiveNotAcceptPlay(received);
                        break;
                    case "LEAVE_TO_GAME":
                        onReceiveLeaveGame(received);
                        break;
                    case "CHECK_STATUS_USER":
                        onReceiveCheckStatusUser(received);
                        break;
                    case "START_GAME":
                        onReceiveStartGame(received);
                        break;
                    case "RESULT_GAME":
                        onReceiveResultGame(received);
                        break;
                    case "ASK_PLAY_AGAIN":
                        onReceiveAskPlayAgain(received);
                        break;

                    case "EXIT":
                        running = false;
                }

            } catch (IOException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                running = false;
            }
        }

        try {
            // closing resources
            s.close();
            dis.close();
            dos.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        // alert if connect interup
        showAlert("Mất kết nối tới server", "Lỗi");
        ClientRun.openScene(ClientRun.SceneName.CONNECTSERVER);
    }

    /***
     * Handle from client
     */
    public void login(String email, String password) {
        // prepare data
        String data = "LOGIN" + ";" + email + ";" + password;
        // send data
        sendData(data);
    }

    public void register(String email, String password) {
        // prepare data
        String data = "REGISTER" + ";" + email + ";" + password;
        // send data
        sendData(data);
    }

    public void logout () {
        this.loginUser = null;
        sendData("LOGOUT");
    }

    public void close () {
        sendData("CLOSE");
    }

    public void getListOnline() {
        sendData("GET_LIST_ONLINE");
    }

    public void getInfoUser(String username) {
        sendData("GET_INFO_USER;" + username);
    }

    public void checkStatusUser(String username) {
        sendData("CHECK_STATUS_USER;" + username);
    }

    // Chat
    public void inviteToChat (String userInvited) {
        sendData("INVITE_TO_CHAT;" + loginUser + ";" + userInvited);
    }

    public void leaveChat (String userInvited) {
        sendData("LEAVE_TO_CHAT;" + loginUser + ";" + userInvited);
    }

    public void sendMessage (String userInvited, String message) {
        sendData("CHAT_MESSAGE;" + loginUser + ";" + userInvited  + ";" + message);
    }

    // Play game
    public void inviteToPlay (String userInvited) {
        sendData("INVITE_TO_PLAY;" + loginUser + ";" + userInvited);
    }

    public void leaveGame (String userInvited) {
        sendData("LEAVE_TO_GAME;" + loginUser + ";" + userInvited + ";" + roomIdPresent);
    }

    public void startGame (String userInvited) {
        sendData("START_GAME;" + loginUser + ";" + userInvited + ";" + roomIdPresent);
    }

    public void submitResult (String competitor) {
        String result1 = ClientRun.gameView.getResults();

        ClientRun.gameView.pauseTime();
        // Handle calculate time
        String timerText = ClientRun.gameView.lblTimer.getText();
        String[] split = timerText.split(":");
        String countDownTime = split[1];
        int time = 30 - Integer.parseInt(countDownTime);

        String data = result1 + ";" + time;

        sendData("SUBMIT_RESULT;" + loginUser + ";" + competitor + ";" + roomIdPresent + ";" + data);
        ClientRun.gameView.afterSubmit();
    }

    public void acceptPlayAgain() {
        sendData("ASK_PLAY_AGAIN;YES;" + loginUser);
    }

    public void notAcceptPlayAgain() {
        sendData("ASK_PLAY_AGAIN;NO;" + loginUser);
    }

    /***
     * Handle send data to server
     */
    public void sendData(String data) {
        try {
            dos.writeUTF(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /***
     * Handle receive data from server
     */
    private void onReceiveLogin(String received) {
        // get status from data
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("failed")) {
            // hiển thị lỗi
            String failedMsg = split[2];
            showAlert(failedMsg, "Lỗi đăng nhập");

        } else if (status.equals("success")) {
            // lưu user login
            this.loginUser = split[2];
            this.score = Float.parseFloat(split[3]);

            // chuyển scene
            ClientRun.openScene(ClientRun.SceneName.HOMEVIEW);

            // auto set info user
            ClientRun.homeView.setUsername(loginUser);
            ClientRun.homeView.setUserScore(score);
            ClientRun.homeView.setUserWin(Integer.parseInt(split[4]));
            ClientRun.homeView.setUserDraw(Integer.parseInt(split[5]));
            ClientRun.homeView.setUserLose(Integer.parseInt(split[6]));
        }
    }

    private void onReceiveRegister(String received) {
        // get status from data
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("failed")) {
            // hiển thị lỗi
            String failedMsg = split[2];
            showAlert(failedMsg, "Lỗi đăng ký");

        } else if (status.equals("success")) {
            showAlert("Register account successfully! Please login!", "Thông báo");
            // chuyển scene
            ClientRun.openScene(ClientRun.SceneName.LOGIN);
        }
    }

    private void onReceiveGetListOnline(String received) {
        // get status from data
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            int userCount = Integer.parseInt(split[2]);
            Vector<String> vdata = new Vector<>();

            if (userCount > 0) {
                for (int i = 3; i < userCount + 3; i++) {
                    String user = split[i];
                    if (!user.equals(loginUser) && !user.equals("null")) {
                        vdata.add(user);
                    }
                }
                // Cập nhật danh sách người dùng vào giao diện
                ClientRun.homeView.setListUser(vdata);
            } else {
                ClientRun.homeView.resetTblUser();
            }

        } else {
            showAlert("Have some error!", "Lỗi");
        }

    }

    private void onReceiveGetInfoUser(String received) {
        // get status from data
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            String userName = split[2];
            String userScore =  split[3];
            String userWin =  split[4];
            String userDraw =  split[5];
            String userLose =  split[6];
            String userAvgCompetitor =  split[7];
            String userAvgTime =  split[8];
            String userStatus = split[9];

            ClientRun.openScene(ClientRun.SceneName.INFOPLAYER);
            ClientRun.infoPlayerView.setInfoUser(userName, userScore, userWin, userDraw, userLose, userAvgCompetitor, userAvgTime, userStatus);

        }
    }

    private void onReceiveLogout(String received) {
        // get status from data
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            ClientRun.openScene(ClientRun.SceneName.LOGIN);
        }
    }

    // chat
    private void onReceiveInviteToChat(String received) {
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            String userHost = split[2];
            String userInvited = split[3];

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Chat Invitation");
                alert.setHeaderText(null);
                alert.setContentText(userHost + " wants to chat with you.");

                if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    ClientRun.openScene(ClientRun.SceneName.MESSAGEVIEW);
                    ClientRun.messageView.setInfoUserChat(userHost);
                    sendData("ACCEPT_MESSAGE;" + userHost + ";" + userInvited);
                } else {
                    sendData("NOT_ACCEPT_MESSAGE;" + userHost + ";" + userInvited);
                }
            });
        }
    }

    private void onReceiveAcceptMessage(String received) {
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            String userHost = split[2];
            String userInvited = split[3];

            Platform.runLater(() -> {
                ClientRun.openScene(ClientRun.SceneName.MESSAGEVIEW);
                ClientRun.messageView.setInfoUserChat(userInvited);
            });
        }
    }

    private void onReceiveNotAcceptMessage(String received) {
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            String userHost = split[2];
            String userInvited = split[3];

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Chat Declined");
                alert.setHeaderText(null);
                alert.setContentText(userInvited + " doesn't want to chat with you.");
                alert.showAndWait();
            });
        }
    }

    private void onReceiveLeaveChat(String received) {
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            String userHost = split[2];
            String userInvited = split[3];

            Platform.runLater(() -> {
                ClientRun.closeScene(ClientRun.SceneName.MESSAGEVIEW);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Chat Ended");
                alert.setHeaderText(null);
                alert.setContentText(userHost + " has left the chat.");
                alert.showAndWait();
            });
        }
    }

    private void onReceiveChatMessage(String received) {
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            String userHost = split[2];
            String userInvited = split[3];
            String message = split[4];

            String chat = message + "\n";

            Platform.runLater(() -> {
                ClientRun.messageView.setContentChat(chat);
            });
        }
    }


//    // play game
private void onReceiveInviteToPlay(String received) {
    String[] split = received.split(";");
    String status = split[1];

    if (status.equals("success")) {
        String userHost = split[2];
        String userInvited = split[3];
        String roomId = split[4];

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Invitation");
            alert.setHeaderText(null);
            alert.setContentText(userHost + " wants to play a game with you.");

            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    ClientRun.openScene(ClientRun.SceneName.GAMEVIEW);
                    ClientRun.gameView.setInfoPlayer(userHost);
                    roomIdPresent = roomId;
                    ClientRun.gameView.setStateUserInvited();
                    sendData("ACCEPT_PLAY;" + userHost + ";" + userInvited + ";" + roomId);
                } else {
                    sendData("NOT_ACCEPT_PLAY;" + userHost + ";" + userInvited + ";" + roomId);
                }
            });
        });
    }
}



    private void onReceiveAcceptPlay(String received) {
        // get status from data
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            String userHost = split[2];
            String userInvited = split[3];
            roomIdPresent = split[4];
            ClientRun.openScene(ClientRun.SceneName.GAMEVIEW);
            ClientRun.gameView.setInfoPlayer(userInvited);
            ClientRun.gameView.setStateHostRoom();
        }
    }

    private void onReceiveNotAcceptPlay(String received) {
        // get status from data
        String[] splitted = received.split(";");
        String status = splitted[1];

        if (status.equals("success")) {
            String userHost = splitted[2];
            String userInvited = splitted[3];

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Invitation Declined");
                alert.setHeaderText(null);
                alert.setContentText(userInvited + " doesn't want to play with you!");
                alert.showAndWait();
            });

        }
    }

    private void onReceiveLeaveGame(String received) {
        String[] splitted = received.split(";");
        String status = splitted[1];

        if (status.equals("success")) {
            String user1 = splitted[2];
            String user2 = splitted[3];

            roomIdPresent = null;
            Platform.runLater(() -> {
                ClientRun.closeScene(ClientRun.SceneName.GAMEVIEW);
                ClientRun.gameView.resetComponents();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Ended");
                alert.setHeaderText(null);
                alert.setContentText(user1 + " has left the game!");
                alert.showAndWait();
            });
        }
    }


    private void onReceiveCheckStatusUser(String received) {
        // get status from data
        String[] splitted = received.split(";");
        String status = splitted[2];
        ClientRun.homeView.setStatusCompetitor(status);
    }

    private void onReceiveStartGame(String received) {
        // get status from data
        String[] split = received.split(";");
        String status = split[1];

        if (status.equals("success")) {
            String[] s = Arrays.copyOfRange(split, 3, 8);

            ClientRun.gameView.setQuestion(s);

            ClientRun.gameView.setStartGame(150);
        }
    }

    private void onReceiveResultGame(String received) {
        // get status from data
        String[] splitted = received.split(";");
        String status = splitted[1];
        String result = splitted[2];
        String user1 = splitted[3];
        String user2 = splitted[4];
        String roomId = splitted[5];

        if (status.equals("success")) {
            ClientRun.gameView.setWaitingRoom();
            if (result.equals("DRAW")) {
                ClientRun.gameView.showAskPlayAgain("The game is draw. Do you want to play continue?");
            } else if (result.equals(loginUser)) {
                ClientRun.gameView.showAskPlayAgain("You win. Do you want to play continue?");
            } else {
                ClientRun.gameView.showAskPlayAgain("You lose. Do you want to play continue?");
            }
        }
    }

    private void onReceiveAskPlayAgain(String received) {
        String[] splitted = received.split(";");
        String status = splitted[1];

        Platform.runLater(() -> {
            if (status.equals("NO")) {
                ClientRun.closeScene(ClientRun.SceneName.GAMEVIEW);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("End Game");
                alert.setHeaderText(null);
                alert.setContentText("End Game!");
                alert.showAndWait();
            } else {
                ClientRun.gameView.resetComponents();
                if (loginUser.equals(splitted[2])) {
                    ClientRun.gameView.setStateHostRoom();
                } else {
                    ClientRun.gameView.setStateUserInvited();
                }
            }
        });
    }

    // get set
    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }

    public String getRoomIdPresent() {
        return roomIdPresent;
    }

    public void setRoomIdPresent(String roomIdPresent) {
        this.roomIdPresent = roomIdPresent;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }


}
