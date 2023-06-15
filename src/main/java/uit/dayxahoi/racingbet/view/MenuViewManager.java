package uit.dayxahoi.racingbet.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uit.dayxahoi.racingbet.controller.CommonController;
import uit.dayxahoi.racingbet.model.DXHButton;
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

    //List<ShipPicker> shipsList;
    //private SHIP choosenShip;

    public MenuViewManager() {

        if (CommonController.getInstance().isExistData()) {
            user = (User) CommonController.getInstance().readObjectFromFile();
        } else {
            user = new User("abc", "abc", 100);
            CommonController.getInstance().writeObjectToFile(user);
        }


        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
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
                //showSubScene(shipChooserSubscene);
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
