package uit.dayxahoi.racingbet.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uit.dayxahoi.racingbet.MyApplication;
import uit.dayxahoi.racingbet.model.ItemStore;
import uit.dayxahoi.racingbet.model.User;
import uit.dayxahoi.racingbet.util.Toast;
import uit.dayxahoi.racingbet.view.MenuViewManager;

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
        Stage stage = (Stage) DangKyButton.getScene().getWindow();
        if (!validateUserName(txtTenDN.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Lỗi");
            alert.setContentText("Tên tài khoản không hợp lệ");
            alert.showAndWait();
            return;
        }
        if (!validatePassword(txtPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Lỗi");
            alert.setContentText("Mật khẩu không hợp lệ");
            alert.showAndWait();
            return;
        }
        if (CommonController.getInstance().isExistData(txtTenDN.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Thông báo");
            alert.setContentText("Tài khoản đã tồn tại!");
            alert.showAndWait();
            return;
        }
        User user = new User(txtTenDN.getText(), txtPassword.getText(), 100);
        ItemStore itemStore = new ItemStore();
        user.setItemStore(itemStore);
        CommonController.getInstance().writeObjectToFile(user, txtTenDN.getText());
        Toast.makeText(stage, "Đăng ký thành công", 1500, 800, 800);
        MyApplication.getInstance().getStorage().userName = txtTenDN.getText();
        MenuViewManager manager = new MenuViewManager();
        Stage primaryStage = manager.getMainStage();
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Racing Bet");
        stage.close();
        primaryStage.show();
    }

    @FXML
    void dangNhap(MouseEvent event) {
        Stage stage = (Stage) DangNhapButton.getScene().getWindow();
        if (!validateUserName(txtTenDN.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Lỗi");
            alert.setContentText("Tên đăng nhập không hợp lệ");
            alert.showAndWait();
            return;
        } else if (!validatePassword(txtPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Lỗi");
            alert.setContentText("Mật khẩu không hợp lệ");
            alert.showAndWait();
            return;
        }
        if (!CommonController.getInstance().isExistData(txtTenDN.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Thông báo");
            alert.setContentText("Tài khoản không tồn tại!");
            alert.showAndWait();
            return;
        }
        User user = (User) CommonController.getInstance().readObjectFromFile(txtTenDN.getText());
        if (!user.getPassword().equals(txtPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Lỗi");
            alert.setContentText("Sai mật khẩu rồi bạn ơi!");
            alert.showAndWait();
            return;
        }
        Toast.makeText(stage, "Đăng nhập thành công", 1500, 800, 800);
        MyApplication.getInstance().getStorage().userName = txtTenDN.getText();
        MenuViewManager manager = new MenuViewManager();
        Stage primaryStage = manager.getMainStage();
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Racing Bet");
        stage.close();
        primaryStage.show();
    }

    private boolean validateUserName(String username) {
        if (username == null || username.length() < 3 || username.length() > 20) {
            return false;
        }

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            return false;
        }
        return true;
    }

    public static boolean validatePassword(String password) {
        if (password == null || password.length() < 3 || password.length() > 20) {
            return false;
        }

        /*if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            return false;
        }*/

        return true;
    }

}
