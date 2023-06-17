package uit.dayxahoi.racingbet;

import javafx.application.Application;
import javafx.stage.Stage;
import uit.dayxahoi.racingbet.util.Storage;
import uit.dayxahoi.racingbet.view.GameViewManager;
import uit.dayxahoi.racingbet.view.LoginView;
import uit.dayxahoi.racingbet.view.MenuViewManager;

public class MyApplication extends Application {
           // @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }

    private static MyApplication instance;
    private Storage storage;
    @Override
    public void start(Stage primaryStage) {
        storage = new Storage();
        instance = this;
        try {
            LoginView manager = new LoginView();
            manager.startLogin(primaryStage);
            primaryStage.setMaximized(true);
            primaryStage.setTitle("Racing Bet");
            primaryStage.show();

            /*MenuViewManager manager = new MenuViewManager();
            primaryStage = manager.getMainStage();
            primaryStage.setMaximized(true);
            primaryStage.setTitle("Racing Bet");
            primaryStage.show();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Storage getStorage() {
        return storage;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }
}