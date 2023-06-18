package uit.dayxahoi.racingbet;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import uit.dayxahoi.racingbet.util.Storage;
import uit.dayxahoi.racingbet.view.GameViewManager;
import uit.dayxahoi.racingbet.view.LoginView;
import uit.dayxahoi.racingbet.view.MenuViewManager;

import java.io.File;

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
    private Media media = new Media(getClass().getResource("/uit/dayxahoi/racingbet/sound/gameMusic.wav").toExternalForm());
    private MediaPlayer mediaPlayer = new MediaPlayer(media);

    @Override
    public void init() throws Exception {
        super.init();
        // Loop
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));


        mediaPlayer.setOnRepeat(() -> {
            mediaPlayer.seek(Duration.ZERO);
        });

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @Override
    public void start(Stage primaryStage) {
        storage = new Storage();
        instance = this;
        try {
            LoginView manager = new LoginView();
            manager.startLogin(primaryStage);
            primaryStage.setMaximized(true);
            primaryStage.setTitle("Racing Bet");
            mediaPlayer.play();
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

    public void playMusic() {
        if (!isPlayingMusic()) {
            System.out.println("#playMusic");
            mediaPlayer.play();
        }
    }

    public void stopMusic() {
        if (isPlayingMusic()) {
            System.out.println("#stopMusic");
            mediaPlayer.stop();
        }
    }

    public boolean isPlayingMusic() {
        boolean playing = mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
        System.out.println("#isPlayingMusic" + playing);
        return playing;
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