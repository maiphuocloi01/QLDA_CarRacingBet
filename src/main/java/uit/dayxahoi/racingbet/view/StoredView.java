package uit.dayxahoi.racingbet.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uit.dayxahoi.racingbet.controller.StoredController;
import uit.dayxahoi.racingbet.util.ResourceFile;

public class StoredView {


    public StoredView() {
    }

    public void startStore(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(ResourceFile.getInstance().getResURL("view/Stored.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
