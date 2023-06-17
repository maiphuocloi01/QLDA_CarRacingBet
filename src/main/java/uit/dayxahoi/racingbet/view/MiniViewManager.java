package uit.dayxahoi.racingbet.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import uit.dayxahoi.racingbet.MyApplication;
import uit.dayxahoi.racingbet.controller.CommonController;
import uit.dayxahoi.racingbet.mini.*;
import uit.dayxahoi.racingbet.model.DXHButton;
import uit.dayxahoi.racingbet.model.User;
import uit.dayxahoi.racingbet.util.ResourceFile;
import uit.dayxahoi.racingbet.util.Resourses;

public class MiniViewManager {

    private static final Screen screen = Screen.getPrimary();
    private static final Rectangle2D bounds = screen.getVisualBounds();
    private static final double width = bounds.getWidth();
    private static final double height = bounds.getHeight();
    //private final double width = 1280, height = 720; // set screen size
    Resourses res = new Resourses();
    Pane root;
    boolean gameOver = false;
    private boolean incrementOnce = true;
    int score = 0;
    int highScore = 0;
    double FPS_30 = 30;
    int counter_30FPS = 0, counter_3FPS = 0;
    Bird bird;
    TranslateTransition jump;
    TranslateTransition fall;
    RotateTransition rotator;
    ArrayList<TwoTubes> listOfTubes = new ArrayList<>();
    ArrayList<Cloud> listOfClouds = new ArrayList<>();
    ScoreLabel scoreLabel = new ScoreLabel(width, 0);
    Timeline gameLoop;

    private String free = ResourceFile.getInstance().getImagePath("exit.png");
    private String press = ResourceFile.getInstance().getImagePath("exit_pressed.png");
    private DXHButton exitButton = new DXHButton(free, press);

    private User user;

    String userName = MyApplication.getInstance().getStorage().userName;

    public void startGame(Stage primaryStage) {
        user = (User) CommonController.getInstance().readObjectFromFile(userName);
        // Exit button
        exitButton.setLayoutY((bounds.getMaxY() / 15) - 10);
        exitButton.setLayoutX(13 * bounds.getMaxX() / 14);
        //exitButton.setFitWidth(bounds.getWidth() / 2);
        //exitButton.setFitHeight(2 * bounds.getHeight() / 3);

        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //mainStage.close();
                MenuViewManager manager = new MenuViewManager();
                manager.backMenu(primaryStage);
            }
        });
        root = new Pane();
        root.setStyle("-fx-background-color: #4EC0CA");
        Scene scene = new Scene(root, width, height);
        primaryStage.setTitle("Flappy bird");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
        initGame();
        root.setPrefSize(width, height);
        root.setOnMouseClicked(e -> {
            if (!gameOver) {
                jumpflappy();
            } else {
                initializeGame();
            }
        });
    }

    private void updateCounters() {
        if (counter_30FPS % 4 == 0) {
            bird.refreshBird();
            counter_30FPS = 1;
        }
        counter_30FPS++;
    }

    private void jumpflappy() {
        rotator.setDuration(Duration.millis(100));
        rotator.setToAngle(-40);
        rotator.stop();
        rotator.play();
        jump.setByY(-50);
        jump.setCycleCount(1);
        bird.jumping = true;
        fall.stop();
        jump.stop();
        jump.play();
        jump.setOnFinished((finishedEvent) -> {
            rotator.setDuration(Duration.millis(500));
            rotator.setToAngle(40);
            rotator.stop();
            rotator.play();
            bird.jumping = false;
            fall.play();
        });
    }

    private void checkCollisions() {
        TwoTubes tube = listOfTubes.get(0);
        if (tube.getTranslateX() < 35 && incrementOnce) {
            score++;
            user.setGold(user.getGold() + 1);
            CommonController.getInstance().writeObjectToFile(user, userName);
            scoreLabel.setText("Score: " + score);
            incrementOnce = false;
        }
        Path p1 = (Path) Shape.intersect(bird.getBounds(), tube.topBody);
        Path p2 = (Path) Shape.intersect(bird.getBounds(), tube.topHead);
        Path p3 = (Path) Shape.intersect(bird.getBounds(), tube.lowerBody);
        Path p4 = (Path) Shape.intersect(bird.getBounds(), tube.lowerHead);
        boolean intersection = !(p1.getElements().isEmpty()
                && p2.getElements().isEmpty()
                && p3.getElements().isEmpty()
                && p4.getElements().isEmpty());
        if (bird.getBounds().getCenterY() + bird.getBounds().getRadiusY() > height || bird.getBounds().getCenterY() - bird.getBounds().getRadiusY() < 0) {
            intersection = true;
        }
        if (intersection) {
            GameOverLabel gameOverLabel = new GameOverLabel(width / 2, height / 2);
            highScore = Math.max(highScore, score);
            gameOverLabel.setText("Tap to retry. Score: " + score + "\n\tHighScore: " + highScore);
            saveHighScore();
            root.getChildren().add(gameOverLabel);
            root.getChildren().get(1).setOpacity(0);
            gameOver = true;
            gameLoop.stop();
        }

    }

    void initializeGame() {
        listOfTubes.clear();
        listOfClouds.clear();
        root.getChildren().clear();
        bird.getGraphics().setTranslateX(100);
        bird.getGraphics().setTranslateY(150);
        scoreLabel.setOpacity(0.8);
        scoreLabel.setText("Score: 0");
        root.getChildren().addAll(bird.getGraphics(), scoreLabel);
        for (int i = 0; i < 5; i++) {
            Cloud cloud = new Cloud();
            cloud.setX(Math.random() * width);
            cloud.setY(Math.random() * height * 0.5 + 0.1);
            listOfClouds.add(cloud);
            root.getChildren().add(cloud);
        }
        for (int i = 0; i < 5; i++) {
            SimpleDoubleProperty y = new SimpleDoubleProperty(0);
            y.set(root.getHeight() * Math.random() / 2.0);
            TwoTubes tube = new TwoTubes(y, root, false, false);
            tube.setTranslateX(i * (width / 4 + 10) + 400);
            listOfTubes.add(tube);
            root.getChildren().add(tube);
        }
        root.getChildren().add(exitButton);
        score = 0;
        incrementOnce = true;
        gameOver = false;
        bird.jumping = false;
        fall.stop();
        fall.play();
        gameLoop.play();
    }

    void initGame() {
        bird = new Bird(res.birdImgs);
        rotator = new RotateTransition(Duration.millis(500), bird.getGraphics());
        jump = new TranslateTransition(Duration.millis(450), bird.getGraphics());
        fall = new TranslateTransition(Duration.millis(5 * height), bird.getGraphics());
        jump.setInterpolator(Interpolator.LINEAR);
        fall.setByY(height + 20);
        rotator.setCycleCount(1);
        bird.getGraphics().setRotationAxis(Rotate.Z_AXIS);
        gameLoop = new Timeline(new KeyFrame(Duration.millis(1000 / FPS_30), new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                updateCounters();
                checkCollisions();
                if (listOfTubes.get(0).getTranslateX() <= -width / 12.3) {
                    listOfTubes.remove(0);
                    SimpleDoubleProperty y = new SimpleDoubleProperty(0);
                    y.set(root.getHeight() * Math.random() / 2.0);
                    TwoTubes tube;
                    if (Math.random() < 0.4) {
                        tube = new TwoTubes(y, root, true, false);
                    } else if (Math.random() > 0.85) {
                        tube = new TwoTubes(y, root, true, true);
                    } else {
                        tube = new TwoTubes(y, root, false, false);
                    }
                    tube.setTranslateX(listOfTubes.get(listOfTubes.size() - 1).getTranslateX() + (width / 4 + 10));
                    listOfTubes.add(tube);
                    incrementOnce = true;
                    root.getChildren().remove(7);
                    root.getChildren().add(tube);
                }
                for (int i = 0; i < listOfTubes.size(); i++) {
                    if (listOfClouds.get(i).getX() < -listOfClouds.get(i).getImage().getWidth() * listOfClouds.get(i).getScaleX()) {
                        listOfClouds.get(i).setX(listOfClouds.get(i).getX() + width + listOfClouds.get(i).getImage().getWidth() * listOfClouds.get(i).getScaleX());
                    }
                    listOfClouds.get(i).setX(listOfClouds.get(i).getX() - 1);
                    listOfTubes.get(i).setTranslateX(listOfTubes.get(i).getTranslateX() - 2);
                }
            }
        }));
        gameLoop.setCycleCount(-1);
        initializeGame();
        loadHighScore();
    }

    void loadHighScore() {
        try {
            highScore = new DataInputStream(new FileInputStream("highScore.score")).readInt();
        } catch (Exception ignored) {
        }
    }

    void saveHighScore() {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream("highScore.score"));
            out.writeInt(score);
            out.flush();
        } catch (Exception ignored) {
        }
    }
}