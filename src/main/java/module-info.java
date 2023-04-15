module uit.dayxahoi.racingbet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens uit.dayxahoi.racingbet to javafx.fxml;
    exports uit.dayxahoi.racingbet;
}