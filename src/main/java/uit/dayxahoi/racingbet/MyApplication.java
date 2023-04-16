package uit.dayxahoi.racingbet;

import javafx.application.Application;
import javafx.stage.Stage;
import uit.dayxahoi.racingbet.view.GameViewManager;
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
    @Override
    public void start(Stage primaryStage) {
        try {
            /*MenuViewManager manager = new MenuViewManager();
            primaryStage = manager.getMainStage();
            primaryStage.setMaximized(true);
            primaryStage.show();*/
            GameViewManager gameManager = new GameViewManager();
            gameManager.createNewGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}