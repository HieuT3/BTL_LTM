package com.game.tcpclient.run;

import com.game.tcpclient.controller.SocketHandler;
import com.game.tcpclient.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ClientRun extends Application {
    public enum SceneName {
        CONNECTSERVER,
        LOGIN,
        REGISTER,
        HOMEVIEW,
        INFOPLAYER,
        RANKINGVIEW,
        MESSAGEVIEW,
        GAMEVIEW
    }

    // Scenes
    public static ConnectServer connectServer;
    public static LoginView loginView;
    public static RegisterView registerView;
    public static HomeView homeView;
    public static RankingView rankingView;
    public static InfoPlayerView infoPlayerView;
    public static MessageView messageView;
    public static GameView gameView;

    // Controller
    public static SocketHandler socketHandler;

    // Primary Stage
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        socketHandler = new SocketHandler();
        initScenes();
        openScene(SceneName.CONNECTSERVER);
    }

    public void initScenes() {
        connectServer = new ConnectServer();
        loginView = new LoginView();
        registerView = new RegisterView();
        homeView = new HomeView();
        rankingView = new RankingView();
        infoPlayerView = new InfoPlayerView();
        messageView = new MessageView();
        gameView = new GameView();
    }

    public static void openScene(SceneName sceneName) {
        Platform.runLater(() -> {
            if (sceneName != null) {
                switch (sceneName) {
                    case CONNECTSERVER:
                        primaryStage.setScene(connectServer.getScene());
                        primaryStage.setTitle("Connect to Server");
                        primaryStage.centerOnScreen();
                        primaryStage.show();
                        break;
                    case LOGIN:
                        primaryStage.setScene(loginView.getScene());
                        primaryStage.setTitle("Login");
                        primaryStage.centerOnScreen();
                        primaryStage.show();
                        break;
                    case REGISTER:
                        primaryStage.setScene(registerView.getScene());
                        primaryStage.setTitle("Register");
                        primaryStage.centerOnScreen();
                        primaryStage.show();
                        break;
                    case HOMEVIEW:
                        primaryStage.setScene(homeView.getScene());
                        primaryStage.setTitle("Home");
                        primaryStage.centerOnScreen();
                        primaryStage.show();
                        break;
                    case RANKINGVIEW:
                        Platform.runLater(() -> {
                            openModalWindow(rankingView, "Ranking");
                        });
                        break;
                    case INFOPLAYER:
                        Platform.runLater(() -> {
                            openModalWindow(infoPlayerView, "Player Information");
                        });
                        break;
                    case MESSAGEVIEW:
                        Platform.runLater(() -> {
                            openModalWindow(messageView, "Message View");
                        });
                        break;
                    case GAMEVIEW:
                        Platform.runLater(() -> {
                            openModalWindow(gameView, "Game View");
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private static void openModalWindow(Object view, String title) {
        Stage modalStage;
        if (view instanceof InfoPlayerView) {
            modalStage = new Stage();
            modalStage.setScene(((InfoPlayerView) view).getScene());
        } else if (view instanceof MessageView) {
            modalStage = ((MessageView) view).getStage();
        } else if (view instanceof RankingView) {
            modalStage = ((RankingView) view).getStage();
        } else if((view instanceof GameView)) {
            modalStage = ((GameView) view).getStage();
        } else {
            return;
        }

        if (modalStage.isShowing()) {
            System.out.println("Close");
            modalStage.close();
        }
        modalStage.setTitle(title);
        modalStage.show();
    }


    public static void closeScene(SceneName sceneName) {
        Platform.runLater(() -> {
            if (sceneName != null) {
                switch (sceneName) {
//                case CONNECTSERVER:
//                    if (connectServer.getStage() != null) {
//                        connectServer.getStage().close();
//                    }
//                    break;
//                case LOGIN:
//                    if (loginView.getStage() != null) {
//                        loginView.getStage().close();
//                    }
//                    break;
//                case REGISTER:
//                    if (registerView.getStage() != null) {
//                        registerView.getStage().close();
//                    }
//                    break;
//                case HOMEVIEW:
//                    if (homeView.getStage() != null) {
//                        homeView.getStage().close();
//                    }
//                    break;
//                case INFOPLAYER:
//                    if (infoPlayerView.getStage() != null) {
//                        infoPlayerView.getStage().close();
//                    }
//                    break;
                    case MESSAGEVIEW:
                        if (messageView.getStage() != null) {
                            messageView.getStage().close();
                        }
                        break;
                    case GAMEVIEW:
                        if (gameView.getStage() != null) {
                            gameView.getStage().close();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
