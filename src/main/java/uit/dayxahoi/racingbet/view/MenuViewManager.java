package uit.dayxahoi.racingbet.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uit.dayxahoi.racingbet.model.DXHButton;
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

    //List<ShipPicker> shipsList;
    //private SHIP choosenShip;

    public MenuViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        //createSubScenes();
        createButtons();
        createBackground();
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

    public Stage getMainStage() {
        return mainStage;
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
                gameManager.createNewGame(mainStage);
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
        DXHButton settingButton = new DXHButton("SETTING");
        addMenuButtons(settingButton);

        settingButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //showSubScene(shipChooserSubscene);
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
