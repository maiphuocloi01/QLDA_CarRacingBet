package uit.dayxahoi.racingbet.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StoredController implements Initializable {

    @FXML
    private Label HienThiTienLabel;

    @FXML
    private Label MapCoDienPane;

    @FXML
    private Button MuaMapCoDienButton;

    @FXML
    private Button MuaMapGayLuButton;

    @FXML
    private Button MuaMapNongBongButton;

    @FXML
    private Button MuaSkinDXHButton;

    @FXML
    private Button MuaSkinLichLamButton;

    @FXML
    private Button MuaSkinWibuButton;

    @FXML
    private Button QuayLaiButton;

    @FXML
    private Label SkinDXHPane;

    @FXML
    void muaMapCoDien(MouseEvent event) {
        //MuaMapCoDienButton.setStyle("-fx-background-color: rgba(143,131,121,0.5);");
        MuaMapCoDienButton.setDisable(true);
    }

    @FXML
    void muaMapGayLu(MouseEvent event) {
        MuaMapGayLuButton.setDisable(true);

    }

    @FXML
    void muaMapNongBong(MouseEvent event) {
        MuaMapNongBongButton.setDisable(true);

    }

    @FXML
    void muaSkinDXH(MouseEvent event) {
        MuaSkinDXHButton.setDisable(true);

    }

    @FXML
    void muaSkinLichLam(MouseEvent event) {
        MuaSkinLichLamButton.setDisable(true);

    }

    @FXML
    void muaSkinWibu(MouseEvent event) {
        MuaSkinWibuButton.setDisable(true);

    }

    @FXML
    void quayLai(MouseEvent event) {
        Stage stage = (Stage) QuayLaiButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private AnchorPane root;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lấy kích thước màn hình
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Thiết lập kích thước cho AnchorPane
        root.setPrefWidth(screenBounds.getWidth());
        root.setPrefHeight(screenBounds.getHeight());

    }
}
