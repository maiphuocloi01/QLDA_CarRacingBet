package uit.dayxahoi.racingbet.view;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import uit.dayxahoi.racingbet.MyApplication;
import uit.dayxahoi.racingbet.controller.CommonController;
import uit.dayxahoi.racingbet.model.DXHButton;
import uit.dayxahoi.racingbet.model.User;
import uit.dayxahoi.racingbet.util.ResourceFile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameViewManager {

    private static final Screen screen = Screen.getPrimary();
    private static final Rectangle2D bounds = screen.getVisualBounds();
    private static final double GAME_WIDTH = bounds.getWidth();
    private static final double GAME_HEIGHT = bounds.getHeight();
    private final ObservableList<String> options = FXCollections.observableArrayList("1ST", "2ND", "3RD", "4TH", "5TH");

    private final ObservableList<String> optionsMap = FXCollections.observableArrayList("1ST", "2ND", "3RD");

    private final ObservableList<String> optionsSkin = FXCollections.observableArrayList("1ST", "2ND", "3RD");

    //Parent
    private final Pane mainPane = new Pane();

    //Child
    private final Pane paneDialog = new Pane();
    private final Pane paneRacing = new Pane();
    private final Group paneRace = new Group();

    //View
    private ImageView menuPanelBackground = new ImageView();
    private ComboBox<String> comboBox = new ComboBox<>(options);
    private ComboBox<String> comboBoxMap = new ComboBox<>(optionsMap);
    private ComboBox<String> comboBoxSkin = new ComboBox<>(optionsSkin);
    private Label instructionLabel = new Label("Enter a Betting Amount: ");
    private Label selectCarLabel = new Label("Select Car: ");
    private Label selectMapLabel = new Label("Select Map: ");
    private Label selectSkinLabel = new Label("Select Skin: ");
    private Label changingLabel = new Label("");
    private TextField textField = new TextField();
    private static Label timerLabel = new Label();
    private static Label goldLabel = new Label();
    private DXHButton startButton = new DXHButton("Play");
    private DXHButton resetButton = new DXHButton("Reset");

    private String free = ResourceFile.getInstance().getImagePath("exit.png");
    private String press = ResourceFile.getInstance().getImagePath("exit_pressed.png");
    private DXHButton exitButton = new DXHButton(free, press);

    private ImageView imgSelectLine = new ImageView();

    static final Integer STARTTIME = 3;
    static IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
    static Timeline timeline;

    // Threads
    Thread threadRace;
    Thread threadResult;

    // Rest of Variables
    boolean racing = false;
    List<String> finishedOrder = new ArrayList<>();
    int bet;
    String userChoice;
    String mapChoice;
    String skinChoice = "0";

    ImageView imageViewBackground = new ImageView();

    private Stage mainStage;
    private final User user;

    private IntegerProperty goldObs = new SimpleIntegerProperty();

    String userName = MyApplication.getInstance().getStorage().userName;

    public GameViewManager() {

        user = (User) CommonController.getInstance().readObjectFromFile(userName);
        goldObs.setValue(user.getGold());
        loadBackground(mainPane);
        initView();
        mainPane.getChildren().add(imgSelectLine);
        mainPane.getChildren().add(goldLabel);
        showDialog();
        drawAllCar(false, "0");
    }

    public void startGame(Stage gameStage) {
        mainStage = gameStage;
        AnchorPane gamePane = new AnchorPane();
        Scene gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        mainStage.setScene(gameScene);
        Scene scene = new Scene(mainPane, GAME_WIDTH, GAME_HEIGHT);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();
    }

    private void initView() {
        // Ảnh nền dialog
        String pathPanel = ResourceFile.getInstance().getImagePath("red_panel.png");
        Image imgPanel = new Image(pathPanel, 800.0, 800.0, false, true, false);
        menuPanelBackground.setImage(imgPanel);
        menuPanelBackground.setY(bounds.getMaxY() / 6);
        menuPanelBackground.setX(bounds.getMaxX() / 4);
        menuPanelBackground.setFitWidth(bounds.getWidth() / 2);
        menuPanelBackground.setFitHeight(2 * bounds.getHeight() / 3);

        // Đếm thời gian bắt đầu
        goldLabel.textProperty().bind(goldObs.asString());
        goldLabel.setTextFill(Color.GOLD);
        goldLabel.setLayoutX((bounds.getMaxX() / 15));
        goldLabel.setLayoutY((bounds.getMaxY() / 15));
        goldLabel.setFont(Font.font("Impact", FontWeight.BOLD, 60));

        // Select line
        String pathSelectLine = ResourceFile.getInstance().getImagePath("select_line.png");
        Image imgLine = new Image(pathSelectLine, 1140, 88, false, true, false);
        imgSelectLine.setImage(imgLine);
        imgSelectLine.setY(-200);
        imgSelectLine.setX(bounds.getMaxX() / 8);
        //imgSelectLine.setFitHeight(2 * bounds.getHeight() / 3);

        // Title Text Field
        selectCarLabel.setLayoutX((bounds.getMaxX() / 2) - 200);
        selectCarLabel.setLayoutY((bounds.getMaxY() / 5) + 50);
        selectCarLabel.setFont(Font.font("Impact", FontWeight.BOLD, 20));

        // Title Text Field
        selectMapLabel.setLayoutX((bounds.getMaxX() / 2) - 200);
        selectMapLabel.setLayoutY((bounds.getMaxY() / 5) + 100);
        selectMapLabel.setFont(Font.font("Impact", FontWeight.BOLD, 20));

        // Title Text Field
        selectSkinLabel.setLayoutX((bounds.getMaxX() / 2) - 200);
        selectSkinLabel.setLayoutY((bounds.getMaxY() / 5) + 150);
        selectSkinLabel.setFont(Font.font("Impact", FontWeight.BOLD, 20));

        // Combox chọn xe
        comboBox.setPromptText("Select Car");
        comboBox.setLayoutX((2 * bounds.getMaxX() / 4) + 100);
        comboBox.setLayoutY((bounds.getMaxY() / 5) + 50);
        comboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            public ListCell<String> call(ListView<String> p) {
                final ListCell<String> cell = new ListCell<String>() {
                    {
                        super.setPrefWidth(20);
                        super.setFont(Font.font("Impact", FontWeight.BOLD, 14));
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            if (item.contains("1ST")) setTextFill(Color.ORANGERED);
                            else if (item.contains("2ND")) setTextFill(Color.DEEPPINK);
                            else if (item.contains("3RD")) setTextFill(Color.GREENYELLOW);
                            else if (item.contains("4TH")) setTextFill(Color.PURPLE);
                            else setTextFill(Color.DEEPSKYBLUE);
                        } else setText(null);
                    }
                };
                return cell;
            }
        });
        comboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Object>() {
            public void changed(@SuppressWarnings("rawtypes") ObservableValue observable, Object oldValue, Object newValue) {
                userChoice = newValue.toString();
                pickUpCarShowLine(userChoice);
            }
        });

        // Combox chọn map
        comboBoxMap.setPromptText("Select Map");
        comboBoxMap.setLayoutX((2 * bounds.getMaxX() / 4) + 100);
        comboBoxMap.setLayoutY((bounds.getMaxY() / 5) + 100);
        comboBoxMap.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            public ListCell<String> call(ListView<String> p) {
                final ListCell<String> cell = new ListCell<String>() {
                    {
                        super.setPrefWidth(20);
                        super.setFont(Font.font("Impact", FontWeight.BOLD, 14));
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            if (item.contains("1ST")) setTextFill(Color.ORANGERED);
                            else if (item.contains("2ND")) setTextFill(Color.DEEPPINK);
                            else if (item.contains("3RD")) setTextFill(Color.GREENYELLOW);
                            else if (item.contains("4TH")) setTextFill(Color.PURPLE);
                            else setTextFill(Color.DEEPSKYBLUE);
                        } else setText(null);
                    }
                };
                return cell;
            }
        });
        comboBoxMap.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Object>() {
            public void changed(@SuppressWarnings("rawtypes") ObservableValue observable, Object oldValue, Object newValue) {
                mapChoice = newValue.toString();
                changeImageBackground(mapChoice);
            }
        });

        comboBoxSkin.setPromptText("Select Skin");
        comboBoxSkin.setLayoutX((2 * bounds.getMaxX() / 4) + 100);
        comboBoxSkin.setLayoutY((bounds.getMaxY() / 5) + 150);
        comboBoxSkin.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            public ListCell<String> call(ListView<String> p) {
                final ListCell<String> cell = new ListCell<String>() {
                    {
                        super.setPrefWidth(20);
                        super.setFont(Font.font("Impact", FontWeight.BOLD, 14));
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            if (item.contains("1ST")) setTextFill(Color.ORANGERED);
                            else if (item.contains("2ND")) setTextFill(Color.DEEPPINK);
                            else if (item.contains("3RD")) setTextFill(Color.GREENYELLOW);
                            else if (item.contains("4TH")) setTextFill(Color.PURPLE);
                            else setTextFill(Color.DEEPSKYBLUE);
                        } else setText(null);
                    }
                };
                return cell;
            }
        });
        comboBoxSkin.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Object>() {
            public void changed(@SuppressWarnings("rawtypes") ObservableValue observable, Object oldValue, Object newValue) {
                skinChoice = newValue.toString();
                drawAllCar(false, skinChoice);
            }
        });

        // Title Text Field
        instructionLabel.setLayoutX((bounds.getMaxX() / 2) - 100);
        instructionLabel.setLayoutY((bounds.getMaxY() / 2) - 30);
        instructionLabel.setFont(Font.font("Impact", FontWeight.BOLD, 20));

        // Text thông báo trạng thái cuộc đua
        changingLabel.setLayoutX((bounds.getMaxX() / 2) - 100);
        changingLabel.setLayoutY((bounds.getMaxY() / 2) + 50);
        changingLabel.setFont(Font.font("Impact", FontWeight.BOLD, 18));

        // Nhập tiền cược
        textField.setLayoutX((bounds.getMaxX() / 2) - 100);
        textField.setLayoutY((bounds.getMaxY() / 2));
        textField.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        textField.setPromptText("$");

        // Đếm thời gian bắt đầu
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.ORANGERED);
        timerLabel.setLayoutX((bounds.getMaxX() / 2) - 80);
        timerLabel.setLayoutY((bounds.getMaxY() / 2) - 80);
        timerLabel.setFont(Font.font("Impact", FontWeight.BOLD, 200));

        // StartButton Properties
        startButton.setLayoutX((bounds.getMaxX() / 4) + 100);
        startButton.setLayoutY((4 * bounds.getMaxY() / 6) + 10);

        // ResetButton Properties
        resetButton.setLayoutX((2 * bounds.getMaxX() / 4) + 100);
        resetButton.setLayoutY((4 * bounds.getMaxY() / 6) + 10);

        // Exit button
        exitButton.setLayoutY(bounds.getMaxY() / 15);
        exitButton.setLayoutX(11 * bounds.getMaxX() / 12);
        //exitButton.setFitWidth(bounds.getWidth() / 2);
        //exitButton.setFitHeight(2 * bounds.getHeight() / 3);

        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //mainStage.close();
                MenuViewManager manager = new MenuViewManager();
                manager.backMenu(mainStage);
            }
        });

        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String text = textField.getText(); // Get Text from
                // TextField
                try {
                    // If reset button hasn't been pressed, you can't
                    // start a new race
                    // Try to convert String to Double
                    bet = Integer.parseInt(text);

                    if (bet > user.getGold()) // If value is not valid,
                        // changingLabel prints...
                        changingLabel.setText("You don't have enough money. Try again!!");
                    else if (bet < 1) changingLabel.setText("Wrong!! Try we positive values!!");
                    else if (userChoice == null) // If not userChoice,
                        // changingLabel
                        // prints...
                        changingLabel.setText("Choose one Car First!!");
                    else {
                        // Ok, race is good to go
                        // Can't press buttons until race is over
                        startButton.setDisable(true);
                        resetButton.setDisable(true);

                        racing = true;

                        timeSeconds.set(STARTTIME); // Countdown start
                        timeline = new Timeline();
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(STARTTIME + 4000), // Timer Duration
                                new KeyValue(timeSeconds, 0))); // Countdown
                        // finish at 0
                        timeline.playFromStart(); // Play from beginning

                        // Check userChoice and print his/her option.
                        switch (userChoice) {
                            case "0":
                                changingLabel.setText("Thanks for Betting!! \n You bet $" + bet + " on the RED Car.");
                                break;
                            case "1":
                                changingLabel.setText("Thanks for Betting!! \n You bet $" + bet + " on the PINK Car.");
                                break;
                            case "2":
                                changingLabel.setText("Thanks for Betting!! \n You bet $" + bet + " on the GREEN Car.");
                                break;
                            case "3":
                                changingLabel.setText("Thanks for Betting!! \n You bet $" + bet + " on the PURPLE Car.");
                                break;
                            case "4":
                                changingLabel.setText("Thanks for Betting!! \n You bet $" + bet + " on the BLUE Car.");
                                break;
                        }

                        user.setGold(user.getGold() - bet);
                        goldObs.setValue(user.getGold());
                        CommonController.getInstance().writeObjectToFile(user, userName);
                        // Remove old pane
                        //paneRacing.getChildren().remove(paneRace);

                        // Create a task to run the Thread that make
                        Runnable race = new MakeRockets(finishedOrder);
                        threadRace = new Thread(race);
                        threadRace.start(); // Start Thread

                        // Add a new pane
                        //paneRacing.getChildren().add(paneRace);

                        // Create a task to run the Thread that post
                        // the results of the race
                        Runnable results = new PostResults(paneDialog, finishedOrder, changingLabel, userChoice, bet, resetButton);
                        threadResult = new Thread(results);
                        threadResult.start(); // Start Thread
                    }
                } catch (NumberFormatException e) {
                    // If there is no bet, changingLabel prints...
                    changingLabel.setText("C'mon! You need to bet something");
                }
            }
        });

        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!racing) // If not racing, do nothing
                    return;

                // Turn off reset until next race starts
                racing = false;

                // Reset TextField
                textField.setText(null);

                // If user hasn't click Reset race doesn't start
                startButton.setDisable(false);// allow new race to start

                // Reset Timer when Click reset
                if (timeline != null) timeline.stop();
                timeSeconds.set(STARTTIME);
                timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(STARTTIME + 4000), new KeyValue(timeSeconds, 0)));

                // Reset Label and queue
                changingLabel.setText("Ok...Make new bet");
                finishedOrder = new ArrayList<>();

                // Stop the threads from finishing, in case you
                // interrupted a race
                threadRace.interrupt();
                threadResult.interrupt();

                drawAllCar(false, skinChoice);
            }
        });
        // Add everything to the pane
    }

    private void pickUpCarShowLine(String pickUpCar) {
        switch (pickUpCar) {
            case "0":
                imgSelectLine.setY(145);
                break;
            case "1":
                imgSelectLine.setY(273);
                break;
            case "2":
                imgSelectLine.setY(390);
                break;
            case "3":
                imgSelectLine.setY(507);
                break;
            case "4":
                imgSelectLine.setY(625);
                break;
        }
    }


    private void showDialog() {
        if (paneDialog.getChildren().isEmpty()) {
            paneDialog.getChildren().addAll(menuPanelBackground, selectCarLabel,
                    comboBoxSkin, selectSkinLabel, comboBox, selectMapLabel,
                    comboBoxMap, startButton, resetButton, textField,
                    instructionLabel, changingLabel, exitButton);
            mainPane.getChildren().add(paneDialog);
        }
    }

    private void hideDialog() {
        if (!paneDialog.getChildren().isEmpty()) {
            paneDialog.getChildren().clear();
            mainPane.getChildren().remove(paneDialog);
        }
    }

    public void loadBackground(Pane pane) {
        // Painting the image
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        String imgBackground = ResourceFile.getInstance().getImagePath("Map1.png");
        Image galaxy = new Image(imgBackground, 800.0, 800.0, false, true, false);
        imageViewBackground.setImage(galaxy);
        imageViewBackground.setX(bounds.getMinX());
        imageViewBackground.setY(bounds.getMinY());
        imageViewBackground.setFitWidth(bounds.getWidth());
        imageViewBackground.setFitHeight(bounds.getHeight());

        // Sets up picture background
        pane.getChildren().add(imageViewBackground);
    }

    private void changeImageBackground(String map) {
        System.out.println("#changeImageBackground IN: " + map);
        String imgBackground;
        if (map.equals("1")) {
            imgBackground = ResourceFile.getInstance().getImagePath("Map2.png");
        } else if (map.equals("2")) {
            imgBackground = ResourceFile.getInstance().getImagePath("Map3.png");
        } else {
            imgBackground = ResourceFile.getInstance().getImagePath("Map1.png");
        }
        // Load the image from a specific file
        Image galaxy = new Image(imgBackground, 800.0, 800.0, false, true, false);
        imageViewBackground.setImage(galaxy);
        imageViewBackground.setX(bounds.getMinX());
        imageViewBackground.setY(bounds.getMinY());
        imageViewBackground.setFitWidth(bounds.getWidth());
        imageViewBackground.setFitHeight(bounds.getHeight());
        System.out.println("#changeImageBackground OUT: " + imgBackground);
    }

    private static String changeSkinCar(Color stripesColor, String skin) {
        System.out.println("#changeSkinCar IN: " + skin);
        String imgBackground = ResourceFile.getInstance().getImagePath("set1_genshin01.png");
        if (skin.equals("0")) {
            if (stripesColor.equals(Color.ORANGERED)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set1_genshin01.png");
            } else if (stripesColor.equals(Color.DEEPPINK)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set1_genshin02.png");
            } else if (stripesColor.equals(Color.GREENYELLOW)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set1_genshin03.png");
            } else if (stripesColor.equals(Color.MEDIUMPURPLE)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set1_genshin04.png");
            } else if (stripesColor.equals(Color.DEEPSKYBLUE)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set1_genshin05.png");
            }
        } else if (skin.equals("1")) {
            if (stripesColor.equals(Color.ORANGERED)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set2_pokemon01.png");
            } else if (stripesColor.equals(Color.DEEPPINK)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set2_pokemon02.png");
            } else if (stripesColor.equals(Color.GREENYELLOW)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set2_pokemon03.png");
            } else if (stripesColor.equals(Color.MEDIUMPURPLE)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set2_pokemon04.png");
            } else if (stripesColor.equals(Color.DEEPSKYBLUE)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set2_pokemon05.png");
            }
        } else {
            if (stripesColor.equals(Color.ORANGERED)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set3_man01.png");
            } else if (stripesColor.equals(Color.DEEPPINK)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set3_man02.png");
            } else if (stripesColor.equals(Color.GREENYELLOW)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set3_man03.png");
            } else if (stripesColor.equals(Color.MEDIUMPURPLE)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set3_man04.png");
            } else if (stripesColor.equals(Color.DEEPSKYBLUE)) {
                imgBackground = ResourceFile.getInstance().getImagePath("set3_man05.png");
            }
        }
        return imgBackground;
    }

    // Draw a car method
    public static void drawCar(Group pane, double centerX, double centerY, double scale, double angle, Color stripesColor, List<String> finishOrder, String rocketNum, boolean runing, String skinCar) {
        String imgBackground = changeSkinCar(stripesColor, skinCar);

        Image galaxy = new Image(imgBackground, 215 * scale, 183 * scale, false, true, false);

        // Painting the image
        ImageView imageView = new ImageView();

        imageView.setImage(galaxy);
        imageView.setX(centerX);
        imageView.setY(centerY-55);

        Pane aux = new Pane();

        aux.getChildren().add(imageView);
        aux.setRotate(angle); // Rotate aux

        // Path Transition, only if it is going to race
        if (runing) {
            // Create a path
            Path path = new Path();
            Path path2 = new Path();
            /*path.getElements().add(new MoveTo(50, 80 + centerY * 0.5));// where the problem may be
            path.getElements().add(new LineTo(bounds.getWidth() - 100, 80 + centerY * 0.5));*/
            path.getElements().add(new MoveTo(50, 25 + centerY * 0.5));// where the problem may be
            path.getElements().add(new LineTo((bounds.getWidth() / 2), 25 + centerY * 0.5));
            path.setVisible(false);

            // Create a PathTransition
            PathTransition pathTransition = new PathTransition();
            // speed
            pathTransition.setDuration(Duration.millis(Math.random() * 5000 + 2000));
            pathTransition.setPath(path); // Set path to follow
            pathTransition.setNode(aux);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.play(); // Start Animation

            pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    path2.getElements().add(new MoveTo((bounds.getWidth() / 2) + 2, 25 + centerY * 0.5));// where the problem may be
                    path2.getElements().add(new LineTo((bounds.getWidth()) - 50, 25 + centerY * 0.5));
                    path2.setVisible(false);

                    // Create a PathTransition
                    PathTransition pathTransition2 = new PathTransition();
                    // speed
                    pathTransition2.setDuration(Duration.millis(Math.random() * 5000 + 500));
                    pathTransition2.setPath(path2); // Set path to follow
                    pathTransition2.setNode(aux);
                    pathTransition2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition2.play();
                    // Create a EventHandler to know who finished the race and in what position.
                    pathTransition2.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            finishOrder.add(rocketNum);
                        }
                    });
                }
            });
            pane.getChildren().addAll(path, path2);
        }
        pane.getChildren().add(aux);
    }

    public void drawAllCar(boolean isRun, String skinChoice) {
        if (!paneRace.getChildren().isEmpty()) {
            System.out.println("reset button");
            paneRace.getChildren().clear();
            paneRacing.getChildren().clear();
            mainPane.getChildren().remove(paneRacing);
        }
        drawCar(paneRace, -5, 180, 0.6, 360, Color.ORANGERED, finishedOrder, "0", isRun, skinChoice);
        drawCar(paneRace, -5, 305, 0.6, 360, Color.DEEPPINK, finishedOrder, "1", isRun, skinChoice);
        drawCar(paneRace, -5, 420, 0.6, 360, Color.GREENYELLOW, finishedOrder, "2", isRun, skinChoice);
        drawCar(paneRace, -5, 545, 0.6, 360, Color.MEDIUMPURPLE, finishedOrder, "3", isRun, skinChoice);
        drawCar(paneRace, -5, 660, 0.6, 360, Color.DEEPSKYBLUE, finishedOrder, "4", isRun, skinChoice);
        paneRacing.getChildren().add(paneRace);
        mainPane.getChildren().add(paneRacing);
    }


    // The task that prints the results of the race
    class PostResults implements Runnable {
        List<String> finishedOrder; // Race Positions
        Label changingLabel; // ChangingLabel
        Pane pane; // Our pane
        String userChoice; // User ComboBox selection
        int bet; // User Bet amount
        Button resetButton; // Reset Button

        interface CompleteCallBack {
            void runSuccess();

            void runFailed();
        }

        CompleteCallBack callBack;

        // Construct a task with specific values
        public PostResults(Pane pane, List<String> finishedOrder, Label changingLabel, String userChoice, int bet, Button resetButton) {
            this.finishedOrder = finishedOrder;
            this.pane = pane;
            this.changingLabel = changingLabel;
            this.userChoice = userChoice;
            this.bet = bet;
            this.resetButton = resetButton;
        }

        // Tell Thread how to run
        public void run() {
            try {
                while (finishedOrder.size() < 5) Thread.sleep(500);
            } catch (InterruptedException e) {
                return; // If sleep is interrupted it was due to reset,
            }

            String temp = ""; // Text to Display
            int tempBet = 0; // Text to Display
            System.out.println("Select is: " + userChoice);
            for (String rank : finishedOrder) {
                System.out.println(rank);
            }
            if (finishedOrder.get(0).equals(userChoice)) { // If userChoice finished 1rst...
                temp = ("Wow, You Just Won First!! You Earned $" + bet * 3 + "!!!");
                tempBet = bet * 3;
            } else if (finishedOrder.get(1).equals(userChoice)) { // If userChoice finished 2nd...
                temp = ("Nice, You Got Second. You Earned $" + bet * 2 + "!!");
                tempBet = bet * 2;
            } else if (finishedOrder.get(2).equals(userChoice)) { // If userChoice finished 3rd...
                temp = ("Well, 3rd is better than none. You Earned $" + bet + "!");
                tempBet = bet;
            } else { // If userChoice finished 4th or last...
                temp = ("Nope, Better Luck Next Time!!");
            }

            final String result = temp;
            final int resultBet = tempBet;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // Set text giving results
                    changingLabel.setText(result);
                    user.setGold(user.getGold() + resultBet);
                    goldObs.setValue(user.getGold());
                    CommonController.getInstance().writeObjectToFile(user, userName);
                    // Can't Click reset until race is finish
                    resetButton.setDisable(false);
                    //callBack.runSuccess();
                    showDialog();
                }
            });
        }
    }

    class MakeRockets implements Runnable {
        List<String> finishedOrder; // Race Positions

        // Construct a task with specific values
        public MakeRockets(List<String> finishedOrder) {
            this.finishedOrder = finishedOrder;
            hideDialog();
            mainPane.getChildren().addAll(timerLabel);
        }

        // Tell Thread how to run
        public void run() {
            try {
                // Put task to sleep for the specific time in milliseconds
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                return; // if sleep is interrupted it was due to reset,
            }           // and in that case the thread should just stop

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    drawAllCar(true, skinChoice);
                    mainPane.getChildren().remove(timerLabel);
                }
            });
        }
    }
}
