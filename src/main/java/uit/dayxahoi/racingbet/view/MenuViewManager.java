package uit.dayxahoi.racingbet.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uit.dayxahoi.racingbet.MyApplication;
import uit.dayxahoi.racingbet.controller.CommonController;
import uit.dayxahoi.racingbet.model.DXHButton;
import uit.dayxahoi.racingbet.model.ItemStore;
import uit.dayxahoi.racingbet.model.User;
import uit.dayxahoi.racingbet.util.ResourceFile;

import java.util.ArrayList;
import java.util.List;

public class MenuViewManager {
    private static final Screen screen = Screen.getPrimary();
    private static final Rectangle2D bounds = screen.getVisualBounds();
    private static final double WIDTH = bounds.getWidth();
    private static final double HEIGHT = bounds.getHeight();
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private final static int MENU_BUTTON_START_X = 100;
    private final static int MENU_BUTTON_START_Y = 150;


    //private SpaceRunnerSubScene creditsSubscene;
    //private SpaceRunnerSubScene helpSubscene;
    //private SpaceRunnerSubScene scoreSubscene;
    //private SpaceRunnerSubScene shipChooserSubscene;

    //private SpaceRunnerSubScene sceneToHide;


    List<DXHButton> menuButtons;

    User user;

    private static Label goldLabel = new Label();

    private static final String BTN_PRESSED = ResourceFile.getInstance().getImagePath("mute.png");
    private static final String BTN_FREE = ResourceFile.getInstance().getImagePath("volume.png");
    String BUTTON_PRESSED_STYLE_2 = "-fx-background-color: transparent; -fx-background-image: url('%s'); -fx-background-size:50px 50px; -fx-background-position: center;".formatted(BTN_PRESSED);
    String BUTTON_FREE_STYLE_2 = "-fx-background-color: transparent; -fx-background-image: url('%s'); -fx-background-size:50px 50px; -fx-background-position: center;".formatted(BTN_FREE);
    private Button volumnButton = new Button();

    //List<ShipPicker> shipsList;
    //private SHIP choosenShip;

    public MenuViewManager() {

        String userName = MyApplication.getInstance().getStorage().userName;
        if (CommonController.getInstance().isExistData(userName)) {
            user = (User) CommonController.getInstance().readObjectFromFile(userName);
        } else {
            user = new User(userName, userName, 100);
            ItemStore itemStore = new ItemStore();
            user.setItemStore(itemStore);
            CommonController.getInstance().writeObjectToFile(user, userName);
        }

        // Gold
        goldLabel.setText("$" + user.getGold());
        goldLabel.setTextFill(Color.GOLD);
        goldLabel.setLayoutX((12*bounds.getMaxX() / 15));
        goldLabel.setLayoutY((bounds.getMaxY() / 15));
        goldLabel.setFont(Font.font("Impact", FontWeight.BOLD, 60));

        volumnButton.setLayoutY(bounds.getMaxY() / 15);
        volumnButton.setLayoutX(11 * bounds.getMaxX() / 12);
        volumnButton.setPrefWidth(50);
        volumnButton.setPrefHeight(50);
        if (!MyApplication.getInstance().isPlayingMusic()) {
            volumnButton.setStyle(BUTTON_PRESSED_STYLE_2);
        } else {
            volumnButton.setStyle(BUTTON_FREE_STYLE_2);
        }
        volumnButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //mainStage.close();
                if (!MyApplication.getInstance().isPlayingMusic()) {
                    volumnButton.setStyle(BUTTON_FREE_STYLE_2);
                    MyApplication.getInstance().playMusic();
                } else {
                    volumnButton.setStyle(BUTTON_PRESSED_STYLE_2);
                    MyApplication.getInstance().stopMusic();
                }

            }
        });

        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainPane.getChildren().add(goldLabel);
        mainPane.getChildren().add(volumnButton);
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        //createSubScenes();
        createButtons();
        //createBackground();
        loadBackground();
        //createLogo();


    }

    private void createBackground() {
        String imgBg = ResourceFile.getInstance().getImagePath("deep_blue.png");
        Image backgroundImage = new Image(imgBg, 256, 256, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }

    private void loadBackground() {
        // Load the image from a specific file
        String imgBackground = ResourceFile.getInstance().getImagePath("menu.jpg");
        Image galaxy = new Image(imgBackground, bounds.getWidth(), bounds.getHeight(), false, true, false);

        // Painting the image
        BackgroundImage imageView = new BackgroundImage(galaxy,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, null);

        /*Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        imageView.setImage(galaxy);
        imageView.setX(bounds.getMinX());
        imageView.setY(bounds.getMinY());
        imageView.setFitWidth(bounds.getWidth());
        imageView.setFitHeight(bounds.getHeight());*/

        // Sets up picture background
        mainPane.setBackground(new Background(imageView));
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void backMenu(Stage stage) {
        mainStage = stage;
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        goldLabel.setText("$" + user.getGold());
        if (!MyApplication.getInstance().isPlayingMusic()) {
            volumnButton.setStyle(BUTTON_PRESSED_STYLE_2);
        } else {
            volumnButton.setStyle(BUTTON_FREE_STYLE_2);
        }
        mainPane.getChildren().add(goldLabel);
        mainPane.getChildren().add(volumnButton);
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage.setScene(mainScene);
        createButtons();
        loadBackground();
        mainStage.setMaximized(true);
        mainStage.show();
    }

    private void createButtons() {
        createStartButton();
        createStoreButton();
        createSettingButton();
        createExitButton();
    }

    private void addMenuButtons(DXHButton button) {
        button.setLayoutX(MENU_BUTTON_START_X);
        button.setLayoutY(MENU_BUTTON_START_Y + menuButtons.size() * 100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    private void createStartButton() {
        DXHButton startButton = new DXHButton("PLAY");
        addMenuButtons(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                GameViewManager gameManager = new GameViewManager();
                gameManager.startGame(mainStage);
            }
        });
    }

    private void createStoreButton() {
        DXHButton storeButton = new DXHButton("STORE");
        addMenuButtons(storeButton);

        storeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                StoredView storedView = new StoredView();
                storedView.startStore(mainStage);
            }
        });
    }

    private void createSettingButton() {
        DXHButton settingButton = new DXHButton("MINI GAME");
        addMenuButtons(settingButton);

        settingButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                MiniViewManager gameManager = new MiniViewManager();
                gameManager.startGame(mainStage);
            }
        });
    }

    private void createExitButton() {
        DXHButton exitButton = new DXHButton("EXIT");
        addMenuButtons(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                mainStage.close();
            }
        });

    }
}
