package uit.dayxahoi.racingbet.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private Button DangKyButton;

    @FXML
    private Button DangNhapButton;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtTenDN;

    @FXML
    void dangKy(MouseEvent event) {
        if(txtTenDN.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Vui lòng nhập tên đăng nhập");
            alert.setContentText("Vui lòng nhập tên đăng nhập");
            alert.showAndWait();
        } else if(txtPassword.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Vui lòng nhập mật khẩu");
            alert.setContentText("Vui lòng nhập mật khẩu");
            alert.showAndWait();
        }
    }

    @FXML
    void dangNhap(MouseEvent event) {
        if(txtTenDN.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Vui lòng nhập tên đăng nhập");
            alert.setContentText("Vui lòng nhập tên đăng nhập");
            alert.showAndWait();
        } else if(txtPassword.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Vui lòng nhập mật khẩu");
            alert.setContentText("Vui lòng nhập mật khẩu");
            alert.showAndWait();
        }
    }

}
