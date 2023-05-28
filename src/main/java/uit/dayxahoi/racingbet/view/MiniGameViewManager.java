package uit.dayxahoi.racingbet.view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import uit.dayxahoi.racingbet.minigame.Coin;
import uit.dayxahoi.racingbet.minigame.GameController;
import uit.dayxahoi.racingbet.minigame.Obstacle;
import uit.dayxahoi.racingbet.minigame.Player;
import uit.dayxahoi.racingbet.util.ResourceFile;

import java.util.ArrayList;

public class MiniGameViewManager {

    private static final Screen screen = Screen.getPrimary();
    private static final Rectangle2D bounds = screen.getVisualBounds();
    private static final double GAME_WIDTH = bounds.getWidth();
    private static final double GAME_HEIGHT = bounds.getHeight();
    private final double SCENE_WIDTH = 640, SCENE_HEIGHT = 440;
    private final double CANVAS_WIDTH = 640, CANVAS_HEIGHT = 400;
    private double bgStartingX = 0, bgEndingX = CANVAS_WIDTH;
    private GameController controller;
    private Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    private GraphicsContext context = canvas.getGraphicsContext2D();

    private Scene gameScene;

    public MiniGameViewManager() {
        controller = new GameController(this);

        BorderPane root = new BorderPane();
        gameScene = new Scene(root, this.SCENE_WIDTH, this.SCENE_HEIGHT);
        HBox gameMenu = createMenu();
        ImageView pauseImg = createPauseImg(); // Pause text

        // Audio
        //String musicFile = ResourceFile.getInstance().getSoundPath("gameMusic.wav");
        //Media sound = new Media(new File(musicFile).toURI().toString());
        //MediaPlayer mediaPlayer = new MediaPlayer(sound);

        root.setCenter(canvas);
        root.setBottom(gameMenu);
        root.getChildren().add(pauseImg);

        setSceneFocus(gameScene); // Redirects game's focus onto canvas upon clicking
        listenToPlayerInput(gameScene);

        AnimationTimer timer = new AnimationTimer() {

            private long lastTime = 0; // Time since last update
            private double deltaTime;

            /**
             * Gets called every second of the game in order to update the menu and draw every component of the scene.
             */
            @Override
            public void handle(long now) {

                Player player = controller.getGameModel().getPlayer();
                ArrayList<Obstacle> obsList = controller.getGameModel().getObstacles();
                ArrayList<Coin> coinList = controller.getGameModel().getCoins();

                if (lastTime == 0) { // First frame of game
                    lastTime = now;
                }

                if (controller.getGameModel().isPaused()) { // When paused
                    deltaTime = 0;
                    blurEffect(); // Blur canvas
                    pauseImg.setVisible(true);
                } else {
                    deltaTime = (now - lastTime) * 1e-9;
                    context.setEffect(null); // Clears blur
                    pauseImg.setVisible(false);
                }

                controller.update(deltaTime); // Updates game's logic
                updateMenu(gameMenu);
                drawGameScene(deltaTime, obsList, coinList, player); // Draw's every component of canvas
                //gameMusic(mediaPlayer);
                lastTime = now;
            }
        };
        timer.start();
    }

    public void startGame(Stage stage) {

        stage.getIcons().add(new Image(ResourceFile.getInstance().getImagePath("player.png"))); // Sets window's icon
        stage.setTitle("Flappy Ghost");
        stage.setResizable(false); // Cannot resize game stage
        stage.setScene(gameScene);
        stage.show();
    }

    public void gameMusic(MediaPlayer mediaPlayer) {

        mediaPlayer.setOnEndOfMedia(new Runnable() { // Loop
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });

        if (controller.getGameModel().isPaused()) { // Stops playing when on pause
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
    }

    /**
     * Sets the scene forcus back to the canvas after the user clicks on the canvas so the space bar doesn't trigger the
     * button again.
     * @param gameScene Scene of the game.
     */
    public void setSceneFocus(Scene gameScene) {

        Platform.runLater(() -> {
            canvas.requestFocus();
        });
        gameScene.setOnMouseClicked((event) -> {
            canvas.requestFocus();
        });
    }

    /**
     * Listens to player input and reacts to SPACE and ESCAPE by either jumping or shutting the application.
     * @param gameScene Scene of the game.
     */
    public void listenToPlayerInput(Scene gameScene) {

        Player player = controller.getGameModel().getPlayer();

        gameScene.setOnKeyPressed((value) -> {
            if (value.getCode() == KeyCode.SPACE) { // Player jump when SPACE is pressed
                player.setVy(player.getJumpSpeed());
            } else if (value.getCode() == KeyCode.ESCAPE) { // Closes game when ESCAPE is pressed
                Platform.exit();
            }
        });
    }

    /**
     * Creates a blur effect when the game is paused.
     */
    public void blurEffect() {

        BoxBlur blurEffect = new BoxBlur();

        blurEffect.setWidth(8);
        blurEffect.setHeight(8);
        context.setEffect(blurEffect); // Adds blur effect to canvas
    }

    /**
     * Creates the pause text and places it at the right coordinates in the game.
     * @return The ImageView of the pause text.
     */
    public ImageView createPauseImg() {

        ImageView pauseTxt = new ImageView(new Image(ResourceFile.getInstance().getImagePath("pauseTxt.png")));

        pauseTxt.setX(CANVAS_WIDTH / 2 - (pauseTxt.getImage().getWidth() / 2));   // Set x to middle of canvas
        pauseTxt.setY(CANVAS_HEIGHT / 2 - (pauseTxt.getImage().getHeight() / 2)); // Set y to middle of canvas
        pauseTxt.setVisible(false);

        return pauseTxt;
    }

    /**
     * Creates every button/checkbox/text that make the game menu.
     * @return The HBox that holds all the components.
     */
    public HBox createMenu() {

        HBox gameMenu = new HBox();
        gameMenu.setSpacing(5);

        // Create pause button and adds image
        Button pauseButton = new Button();
        ImageView pauseImg = new ImageView(new Image(ResourceFile.getInstance().getImagePath("pauseButton.png")));
        pauseButton.setGraphic(pauseImg);

        // Create check box
        CheckBox debugBox = new CheckBox();
        ImageView debugImg = new ImageView(new Image(ResourceFile.getInstance().getImagePath("debugModeTxt.png")));
        debugBox.setGraphic(debugImg);

        // Create score text with an image
        ImageView scoreImg = new ImageView(new Image(ResourceFile.getInstance().getImagePath("scoreTxt.png")));
        Text scoreTxt = new Text();
        scoreTxt.setFill(Color.BLACK);
        scoreTxt.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        // Create high score with an image
        ImageView highScoreImg = new ImageView(new Image(ResourceFile.getInstance().getImagePath("highscoreTxt.png")));
        Text highScoreTxt = new Text();
        highScoreTxt.setFill(Color.BLACK);
        highScoreTxt.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        // Create separator with vertical orientation
        Separator sprt1 = new Separator();
        Separator sprt2 = new Separator();
        Separator sprt3 = new Separator();
        sprt1.setOrientation(Orientation.VERTICAL);
        sprt2.setOrientation(Orientation.VERTICAL);
        sprt3.setOrientation(Orientation.VERTICAL);

        gameMenu.setBackground(new Background(
                new BackgroundFill(Color.web("#d9d9d9"), CornerRadii.EMPTY, Insets.EMPTY)));
        // Adds all menu components of menu to HBox
        gameMenu.getChildren().
                addAll(pauseButton, sprt1, debugBox, sprt2, scoreImg, scoreTxt, sprt3, highScoreImg, highScoreTxt);
        gameMenu.setAlignment(Pos.CENTER);

        return gameMenu;
    }

    /**
     * Updates every components of the menu.
     * @param gameMenu The game menu.
     */
    public void updateMenu(HBox gameMenu) {

        updatePause(gameMenu);
        updateDebugMode(gameMenu);
        updateScoreTxt(gameMenu);
        updateHighScoreTxt(gameMenu);
    }

    /**
     * Updates the pause button.
     * @param gameMenu The game menu.
     */
    public void updatePause(HBox gameMenu) {

        Button pauseButton = (Button) gameMenu.getChildren().get(0);
        boolean isPaused = controller.getGameModel().isPaused();

        pauseButton.setOnAction(new EventHandler<ActionEvent>() { // Pauses game when button is pressed
            @Override public void handle(ActionEvent e) {
                controller.getGameModel().setPaused(!isPaused);
            }
        });

        if (isPaused) { // Changes the pause image
            ImageView playImg = new ImageView(new Image(ResourceFile.getInstance().getImagePath("playButton.png")));
            pauseButton.setGraphic(playImg);
        } else {
            ImageView pauseImg = new ImageView(new Image(ResourceFile.getInstance().getImagePath("pauseButton.png")));
            pauseButton.setGraphic(pauseImg);
        }
    }

    /**
     * Updates the debug mode checkbox.
     * @param gameMenu The game menu.
     */
    public void updateDebugMode(HBox gameMenu) {

        CheckBox debugBox = (CheckBox) gameMenu.getChildren().get(2);
        boolean debugModeUsed = controller.getGameModel().isDebugMode();
        boolean debugMode = controller.getGameModel().isDebugMode();

        debugBox.setOnAction(new EventHandler<ActionEvent>() { // Enables or disables debug mode
            @Override public void handle(ActionEvent e) {
                if (!debugModeUsed) {
                    controller.getGameModel().setDebugModeUsed(!debugModeUsed);
                }
                controller.getGameModel().setDebugMode(!debugMode);
            }
        });
    }

    /**
     * Updates the score text.
     * @param gameMenu The game menu.
     */
    public void updateScoreTxt(HBox gameMenu) {

        Text scoreTxt = (Text) gameMenu.getChildren().get(5);

        // Updates the player's score in the menu
        scoreTxt.setText(" " + controller.getGameModel().getPlayer().getScore());
    }

    /**
     * Updates the high score text.
     * @param gameMenu The game menu.
     */
    public void updateHighScoreTxt(HBox gameMenu) {

        Text highScoreTxt = (Text) gameMenu.getChildren().get(8);

        // Updates player's high score in the menu
        highScoreTxt.setText(" " + controller.getGameModel().getPlayer().getHighScore());
    }

    /**
     * Draws every component which makes the game.
     * @param dt The amount of time it took to render the last frame.
     * @param obsList ArrayList which holds every obstacle.
     * @param coinList ArrayList which holds every coin.
     * @param player The user's player.
     */
    public void drawGameScene(double dt, ArrayList<Obstacle> obsList, ArrayList<Coin> coinList, Player player) {

        drawBg(dt);
        drawObstacles(obsList);
        drawCoins(coinList, player);
        drawPlayer(player);
        drawStartImg(obsList, player);
        drawPlusCoinImg(dt, player);
        drawJumpInstruction(obsList);
    }

    /**
     * Draws the game background to the canvas and makes it loop endlessly.
     * @param dt The amount of time it took to render the last frame.
     */
    public void drawBg(double dt) {

        Image bgImg = new Image(ResourceFile.getInstance().getImagePath("background.png"));
        double scrollingSpeed = controller.getGameModel().getPlayer().getAx() * dt;

        if (bgEndingX <= 0) { // Loops back the background
            bgStartingX = 0;
            bgEndingX = CANVAS_WIDTH;
        }

        bgStartingX -= scrollingSpeed;
        bgEndingX -= scrollingSpeed;

        context.drawImage(bgImg, bgStartingX, 0);
        context.drawImage(bgImg, bgStartingX + CANVAS_WIDTH, 0);
    }

    /**
     * Draws every obstacle in the ArrayList to the canvas.
     * @param obsList ArrayList of obstacles which holds every obstacle.
     */
    public void drawObstacles(ArrayList<Obstacle> obsList) {

        for (int i = 0; i < obsList.size(); i++) {
            Obstacle obs = obsList.get(i);
            Image obsImg = new Image(obs.getImg(), obs.getDiameter(), obs.getDiameter(), false, false);
            double obsX = obs.getX() - (obsImg.getWidth() / 2);
            double obsY = obs.getY() - (obsImg.getHeight() / 2);

            // Draws obstacles like cercles when debug mode on and changes color when player intersects
            if (controller.getGameModel().isDebugMode()) {
                if (obs.intersects(controller.getGameModel().getPlayer())) {
                    context.setFill(Color.web("#ff0000"));
                    context.fillOval(obsX, obsY, obs.getDiameter(), obs.getDiameter());
                } else {
                    context.setFill(Color.web("#2f1165"));
                    context.fillOval(obsX, obsY, obs.getDiameter(), obs.getDiameter());
                }
            } else {
                context.drawImage(obsImg, obsX, obsY);
            }
        }
    }

    /**
     * Draws every coin in the ArrayList to the canvas.
     * @param coinList ArrayList of coins which holds every coin.
     * @param player The user's player.
     */
    public void drawCoins(ArrayList<Coin> coinList, Player player) {

        for (int i = 0; i < coinList.size(); i++) {
            Coin coin = coinList.get(i);
            Image coinImg = new Image(coin.getImg(), coin.getDiameter(), coin.getDiameter(), false, false);
            double coinX = coin.getX() - (coinImg.getWidth() / 2);
            double coinY = coin.getY() - (coinImg.getHeight() / 2);

            // Draws coins like cercles when debug mode on and changes color when player intersects
            if (controller.getGameModel().isDebugMode()) {
                if (coin.intersects(player)) {
                    context.setFill(Color.web("#ff0000"));
                    context.fillOval(coinX, coinY, coin.getDiameter(), coin.getDiameter());
                    if (!coin.isTaken()) {
                        player.setScore(player.getScore() + coin.getValue());
                        coin.setTaken(true);
                    }
                } else {
                    context.setFill(Color.web("#ffd700"));
                    context.fillOval(coinX, coinY, coin.getDiameter(), coin.getDiameter());
                }
            } else {
                context.drawImage(coinImg, coinX, coinY);
            }
        }
    }

    /**
     * Draws the player to the canvas.
     * @param player The user's player.
     */
    public void drawPlayer(Player player) {

        Image playerImg = new Image(player.getImg(), player.getDiameter(), player.getDiameter(), false, false);
        double playerX = player.getX() - (playerImg.getWidth() / 2);
        double playerY = player.getY() - (playerImg.getHeight() / 2);

        if (controller.getGameModel().isDebugMode()) { // Draws player like cercle when debug mode on
            context.setFill(Color.web("#ffffff"));
            context.fillOval(playerX, playerY, player.getDiameter(), player.getDiameter());
        } else {
            context.drawImage(playerImg, playerX, playerY);
        }
    }

    /**
     * Draws the start text to the canvas at the beginning of each round.
     * @param obsList ArrayList of obstacles which holds every obstacle.
     * @param player The user's player.
     */
    public void drawStartImg(ArrayList<Obstacle> obsList, Player player) {

        if (obsList.size() == 0) {
            Image startImg = new Image(ResourceFile.getInstance().getImagePath("startTxt.png"));
            double startImgX = player.getX() - (startImg.getWidth() / 2);
            double startImgY = player.getY() + (startImg.getHeight() / 2 - 80);

            context.drawImage(startImg, startImgX, startImgY); // Draws the start text on top of the player
        }
    }

    /**
     * Draws a "+10" image over the player's head upon taking a coin.
     * @param dt The amount of time it took to render the last frame.
     * @param player The user's player.
     */
    public void drawPlusCoinImg(double dt, Player player) {

        double timer = controller.getGameModel().getPlusCoinTimer();

        if (controller.getGameModel().isCoinTaken()) {
            Image plusCoinImg = new Image(ResourceFile.getInstance().getImagePath("plusCoin.png"));
            double plusCoinImgX = player.getX() - (plusCoinImg.getWidth() / 2 + 5);
            double plusCoinImgY = player.getY() + (plusCoinImg.getHeight() / 2 - 65);

            controller.getGameModel().setPlusCoinTimer(timer + dt); // Increments timer
            context.drawImage(plusCoinImg, plusCoinImgX, plusCoinImgY); // Draws the start text on top of the player
        }

        if (timer > 1) { // Keeps text for 1 second
            controller.getGameModel().setCoinTaken(false);
            controller.getGameModel().setPlusCoinTimer(0);
        }
    }

    /**
     * Draws the jump instruction to the canvas only for the first round of every game.
     * @param obsList ArrayList of obstacles which holds every obstacle.
     */
    public void drawJumpInstruction(ArrayList<Obstacle> obsList) {

        // Draws the game instructions only on first round of the game
        if (controller.getGameModel().isFirstStart() && obsList.size() == 0) {
            Image jumpInstructionImg = new Image(ResourceFile.getInstance().getImagePath("jumpInstructionImg.png"));
            double jumpInstructionImgX = CANVAS_WIDTH / 2 - (jumpInstructionImg.getWidth() / 2);
            double jumpInstructionImgY = CANVAS_HEIGHT / 5 - (jumpInstructionImg.getHeight() / 2);

            context.drawImage(jumpInstructionImg, jumpInstructionImgX, jumpInstructionImgY);
        }
    }

    // GETTERS & SETTERS
    public double getBgStartingX() {
        return bgStartingX;
    }

    public void setBgStartingX(double bgStartingX) {
        this.bgStartingX = bgStartingX;
    }

    public double getBgEndingX() {
        return bgEndingX;
    }

    public void setBgEndingX(double bgEndingX) {
        this.bgEndingX = bgEndingX;
    }

    public GameController getController() {
        return controller;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public GraphicsContext getContext() {
        return context;
    }

    public void setContext(GraphicsContext context) {
        this.context = context;
    }

    public double getSCENE_WIDTH() {
        return SCENE_WIDTH;
    }

    public double getSCENE_HEIGHT() {
        return SCENE_HEIGHT;
    }

    public double getCANVAS_WIDTH() {
        return CANVAS_WIDTH;
    }

    public double getCANVAS_HEIGHT() {
        return CANVAS_HEIGHT;
    }

}
