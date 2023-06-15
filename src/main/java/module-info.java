module uit.dayxahoi.racingbet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens uit.dayxahoi.racingbet to javafx.fxml;
    exports uit.dayxahoi.racingbet;
    exports uit.dayxahoi.racingbet.model;
    opens uit.dayxahoi.racingbet.model to javafx.fxml;
    exports uit.dayxahoi.racingbet.view;
    opens uit.dayxahoi.racingbet.view to javafx.fxml;
    exports uit.dayxahoi.racingbet.controller;
    opens uit.dayxahoi.racingbet.controller to javafx.fxml;
    exports uit.dayxahoi.racingbet.mini;
    opens uit.dayxahoi.racingbet.mini to javafx.fxml;
}