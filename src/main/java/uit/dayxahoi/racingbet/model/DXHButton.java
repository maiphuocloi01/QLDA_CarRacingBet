package uit.dayxahoi.racingbet.model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import uit.dayxahoi.racingbet.util.ResourceFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DXHButton extends Button {

    private static final String FONT_PATH = ResourceFile.getInstance().getFontPath("fffforward.TTF");
    private static final String BTN_PRESSED = ResourceFile.getInstance().getImagePath("red_button_pressed.png");
    private static final String BTN_FREE = ResourceFile.getInstance().getImagePath("red_button.png");
    private static final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('%s');".formatted(BTN_PRESSED);
    private static final String BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('%s');".formatted(BTN_FREE);

    public DXHButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(190);
        setPrefHeight(49);
        setStyle(BUTTON_FREE_STYLE);
        initializeButtonListeners();
    }

    public DXHButton(String free, String pressed) {
        String BUTTON_PRESSED_STYLE_2 = "-fx-background-color: transparent; -fx-background-image: url('%s'); -fx-background-size:50px 50px; -fx-background-position: center;".formatted(pressed);
        String BUTTON_FREE_STYLE_2 = "-fx-background-color: transparent; -fx-background-image: url('%s'); -fx-background-size:50px 50px; -fx-background-position: center;".formatted(free);
        setPrefWidth(50);
        setPrefHeight(50);
        setStyle(BUTTON_FREE_STYLE_2);
        initializeButtonListeners2(BUTTON_FREE_STYLE_2, BUTTON_PRESSED_STYLE_2);
    }

    private void setButtonFont() {
        setFont(Font.loadFont(FONT_PATH, 18));
    }

    private void setButtonPressedStyle() {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);

    }

    private void setButtonPressedStyle2(String pressedStyle) {
        setStyle(pressedStyle);
        //setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);

    }

    private void setButtonReleasedStyle() {
        setStyle(BUTTON_FREE_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() - 4);

    }

    private void setButtonReleasedStyle2(String free) {
        setStyle(free);
        //setPrefHeight(45);
        setLayoutY(getLayoutY() - 4);

    }

    private void initializeButtonListeners() {

        setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle();
                }

            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleasedStyle();
                }

            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setEffect(new DropShadow());

            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setEffect(null);

            }
        });


    }

    private void initializeButtonListeners2(String free, String pressed) {

        setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle2(pressed);
                }

            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleasedStyle2(free);
                }

            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setEffect(new DropShadow());

            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setEffect(null);

            }
        });


    }
}