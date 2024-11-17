module com.game.tcpclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.desktop;

    // Mở các gói cho FXML
    opens com.game.tcpclient to javafx.fxml;
    opens com.game.tcpclient.run to javafx.fxml; // Mở gói runFX cho FXML
    opens com.game.tcpclient.view to javafx.fxml; // Mở gói viewFX cho FXML

    // Xuất các gói
    exports com.game.tcpclient.controller;
    exports com.game.tcpclient;
    exports com.game.tcpclient.run;
    exports com.game.tcpclient.view;
}