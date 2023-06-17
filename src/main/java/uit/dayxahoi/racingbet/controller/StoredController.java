package uit.dayxahoi.racingbet.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uit.dayxahoi.racingbet.MyApplication;
import uit.dayxahoi.racingbet.model.ItemStore;
import uit.dayxahoi.racingbet.model.User;
import uit.dayxahoi.racingbet.util.Toast;
import uit.dayxahoi.racingbet.view.MenuViewManager;

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

    String userName = MyApplication.getInstance().getStorage().userName;

    @FXML
    void muaMapCoDien(MouseEvent event) {
        //MuaMapCoDienButton.setStyle("-fx-background-color: rgba(143,131,121,0.5);");
        MuaMapCoDienButton.setDisable(true);
    }

    @FXML
    void muaMapGayLu(MouseEvent event) {
        Stage stage = (Stage) MuaMapGayLuButton.getScene().getWindow();
        if (user.getGold() >= itemStore.getItemMap2Price()) {
            user.getItemStore().setItemMap2(true);
            user.setGold(user.getGold() - itemStore.getItemMap2Price());
            CommonController.getInstance().writeObjectToFile(user, userName);
            HienThiTienLabel.setText(user.getGold() + "$");
            MuaMapGayLuButton.setDisable(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Mua hàng thành công");
            alert.setContentText("Cám ơn bạn đã mua hàng");
            alert.showAndWait();



        } else {
            String toastMsg = "Hổng đủ tiền rồi bạn ơi :(( !";
            int toastMsgTime = 2000; //2 seconds
            int fadeInTime = 500; //0.5 seconds
            int fadeOutTime= 500; //0.5 seconds
            Toast.makeText(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
        }

    }

    @FXML
    void muaMapNongBong(MouseEvent event) {
        Stage stage = (Stage) MuaMapNongBongButton.getScene().getWindow();
        if (user.getGold() >= itemStore.getItemMap3Price()) {
            user.getItemStore().setItemMap3(true);
            user.setGold(user.getGold() - itemStore.getItemMap3Price());
            itemStore = user.getItemStore();
            CommonController.getInstance().writeObjectToFile(user, userName);
            HienThiTienLabel.setText(user.getGold() + "$");
            MuaMapNongBongButton.setDisable(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Mua hàng thành công");
            alert.setContentText("Cám ơn bạn đã mua hàng");
            alert.showAndWait();
        } else {
            String toastMsg = "Hổng đủ tiền rồi bạn ơi :(( !";
            int toastMsgTime = 2000; //2 seconds
            int fadeInTime = 500; //0.5 seconds
            int fadeOutTime= 500; //0.5 seconds
            Toast.makeText(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
        }
    }

    @FXML
    void muaSkinDXH(MouseEvent event) {
        MuaSkinDXHButton.setDisable(true);

    }

    @FXML
    void muaSkinLichLam(MouseEvent event) {
        Stage stage = (Stage) MuaSkinLichLamButton.getScene().getWindow();
        if (user.getGold() >= itemStore.getItemSkin2Price()) {
            user.getItemStore().setItemSkin2(true);
            user.setGold(user.getGold() - itemStore.getItemSkin2Price());
            itemStore = user.getItemStore();
            CommonController.getInstance().writeObjectToFile(user, userName);
            HienThiTienLabel.setText(user.getGold() + "$");
            MuaSkinLichLamButton.setDisable(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Mua hàng thành công");
            alert.setContentText("Cám ơn bạn đã mua hàng");
            alert.showAndWait();
        } else {
            String toastMsg = "Hổng đủ tiền rồi bạn ơi :(( !";
            int toastMsgTime = 2000; //2 seconds
            int fadeInTime = 500; //0.5 seconds
            int fadeOutTime= 500; //0.5 seconds
            Toast.makeText(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
        }

    }

    @FXML
    void muaSkinWibu(MouseEvent event) {
        Stage stage = (Stage) MuaSkinWibuButton.getScene().getWindow();
        if (user.getGold() >= itemStore.getItemSkin3Price()) {
            user.getItemStore().setItemSkin3(true);
            user.setGold(user.getGold() - itemStore.getItemSkin3Price());
            itemStore = user.getItemStore();
            CommonController.getInstance().writeObjectToFile(user, userName);
            HienThiTienLabel.setText(user.getGold() + "$");
            MuaSkinWibuButton.setDisable(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Mua hàng thành công");
            alert.setContentText("Cám ơn bạn đã mua hàng");
            alert.showAndWait();
        } else {
            String toastMsg = "Hổng đủ tiền rồi bạn ơi :(( !";
            int toastMsgTime = 2000; //2 seconds
            int fadeInTime = 500; //0.5 seconds
            int fadeOutTime= 500; //0.5 seconds
            Toast.makeText(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
        }
    }

    @FXML
    void quayLai(MouseEvent event) {
        Stage stage = (Stage) QuayLaiButton.getScene().getWindow();
        //stage.close();
        MenuViewManager manager = new MenuViewManager();
        manager.backMenu(stage);
    }

    @FXML
    private AnchorPane root;

    private User user;
    private ItemStore itemStore;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lấy kích thước màn hình
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Thiết lập kích thước cho AnchorPane
        root.setPrefWidth(screenBounds.getWidth());
        root.setPrefHeight(screenBounds.getHeight());

        user = (User) CommonController.getInstance().readObjectFromFile(userName);
        itemStore = user.getItemStore();

        HienThiTienLabel.setText(user.getGold() + "$");

        if (itemStore.isItemMap3()) {
            MuaMapGayLuButton.setDisable(true);
        }
        if (itemStore.isItemMap2()) {
            MuaMapNongBongButton.setDisable(true);
        }
        if (itemStore.isItemMap1()) {
            MuaMapCoDienButton.setDisable(true);
        }

        if (itemStore.isItemSkin3()) {
            MuaSkinWibuButton.setDisable(true);
        }
        if (itemStore.isItemSkin2()) {
            MuaSkinLichLamButton.setDisable(true);
        }
        if (itemStore.isItemSkin1()) {
            MuaSkinDXHButton.setDisable(true);
        }

    }
}
