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
import uit.dayxahoi.racingbet.model.DXHButton;
import uit.dayxahoi.racingbet.util.ResourceFile;

import java.util.LinkedList;
import java.util.Queue;

public class GameViewManager {

    private static final Screen screen = Screen.getPrimary();
    private static final Rectangle2D bounds = screen.getVisualBounds();
    private static final double GAME_WIDTH = bounds.getWidth();
    private static final double GAME_HEIGHT = bounds.getHeight();
    private final ObservableList<String> options = FXCollections.observableArrayList(
            "RED", "PINK", "GREEN", "PURPLE", "BLUE");

    //Parent
    private final Pane mainPane = new Pane();

    //Child
    private final Pane paneDialog = new Pane();
    private final Pane paneRacing = new Pane();
    private final Group paneRace = new Group();

    //View
    private ImageView menuPanelBackground = new ImageView();
    private ComboBox<String> comboBox = new ComboBox<>(options);
    private Label instructionLabel = new Label("Enter a Betting Amount: ");
    private Label changingLabel = new Label("");
    private TextField textField = new TextField();
    private static Label timerLabel = new Label();
    private DXHButton startButton = new DXHButton("Play");
    private DXHButton resetButton = new DXHButton("Reset");

    static final Integer STARTTIME = 3;
    static IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
    static Timeline timeline;

    // Threads
    Thread threadRace;
    Thread threadResult;

    // Rest of Variables
    boolean racing = false;
    Queue<String> finishedOrder = new LinkedList<String>();
    double bet;
    String userChoice;

    public GameViewManager() {

        loadBackground(mainPane);
        initView();
        showDialog();
        drawAllCar(false, paneRace);
        //mainPane.getChildren().add(paneRace);
        mainPane.getChildren().addAll(paneRacing, timerLabel);

    }

    public void startGame(Stage gameStage) {
        AnchorPane gamePane = new AnchorPane();
        Scene gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage.setScene(gameScene);
        Scene scene = new Scene(mainPane, GAME_WIDTH, GAME_HEIGHT);
        gameStage.setScene(scene);
        gameStage.setMaximized(true);
        gameStage.show();
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

        // Combox chọn xe
        comboBox.setPromptText("Select Car");
        comboBox.setLayoutX((bounds.getMaxX() / 2) - 50);
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
                            if (item.contains("RED"))
                                setTextFill(Color.ORANGERED);
                            else if (item.contains("PINK"))
                                setTextFill(Color.DEEPPINK);
                            else if (item.contains("GREEN"))
                                setTextFill(Color.GREENYELLOW);
                            else if (item.contains("PURPLE"))
                                setTextFill(Color.PURPLE);
                            else
                                setTextFill(Color.DEEPSKYBLUE);
                        } else
                            setText(null);
                    }
                };
                return cell;
            }
        });
        comboBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Object>() {
                    public void changed(
                            @SuppressWarnings("rawtypes") ObservableValue observable,
                            Object oldValue, Object newValue) {
                        userChoice = newValue.toString();
                    }
                });

        // Title Text Field
        instructionLabel.setLayoutX((bounds.getMaxX() / 2) - 100);
        instructionLabel.setLayoutY((bounds.getMaxY() / 2) - 30);
        instructionLabel.setFont(Font.font("Impact", FontWeight.BOLD, 20));

        // Text thông báo trạng thái cuộc đua
        changingLabel.setLayoutX((bounds.getMaxX() / 2) - 100);
        changingLabel.setLayoutY((bounds.getMaxY() / 2) - 90);
        changingLabel.setFont(Font.font("Impact", FontWeight.BOLD, 18));

        // Nhập tiền cược
        textField.setLayoutX((bounds.getMaxX() / 2) - 100);
        textField.setLayoutY((bounds.getMaxY() / 2));
        textField.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        textField.setPromptText("$");

        // Đếm thời gian bắt đầu
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.ORANGERED);
        timerLabel.setLayoutX((5 * bounds.getMaxX() / 6) + 30);
        timerLabel.setLayoutY((bounds.getMaxY() / 20) - 20);
        timerLabel.setFont(Font.font("Impact", FontWeight.BOLD, 80));

        // StartButton Properties
        startButton.setLayoutX((bounds.getMaxX() / 4) + 50);
        startButton.setLayoutY((4 * bounds.getMaxY() / 6) + 10);

        // ResetButton Properties
        resetButton.setLayoutX((2 * bounds.getMaxX() / 4) + 100);
        resetButton.setLayoutY((4 * bounds.getMaxY() / 6) + 10);

        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        String text = textField.getText(); // Get Text from
                        // TextField
                        try {
                            // If reset button hasn't been pressed, you can't
                            // start a new race
                            // Try to convert String to Double
                            bet = Double.parseDouble(text);

                            if (bet > 1000) // If value is not valid,
                                // changingLabel prints...
                                changingLabel
                                        .setText("C'mon! You don't have that much \nmoney. Try again!!");
                            else if (bet < 1)
                                changingLabel
                                        .setText("Wrong!! Try we positive values!!");
                            else if (userChoice == null) // If not userChoice,
                                // changingLabel
                                // prints...
                                changingLabel
                                        .setText("Choose one Car First!!");
                            else {
                                // Ok, race is good to go
                                // Can't press buttons until race is over
                                startButton.setDisable(true);
                                resetButton.setDisable(true);

                                racing = true;

                                timeSeconds.set(STARTTIME); // Countdown start
                                timeline = new Timeline();
                                timeline.getKeyFrames().add(
                                        new KeyFrame(Duration
                                                .millis(STARTTIME + 4000), // Timer Duration
                                                new KeyValue(timeSeconds, 0))); // Countdown
                                // finish at 0
                                timeline.playFromStart(); // Play from beginning

                                // Check userChoice and print his/her option.
                                switch (userChoice) {
                                    case "0":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the RED Car.");
                                        break;
                                    case "1":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the PINK Car.");
                                        break;
                                    case "2":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the GREEN Car.");
                                        break;
                                    case "3":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the PURPLE Car.");
                                        break;
                                    case "4":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the BLUE Car.");
                                        break;
                                }
                                // Remove old pane
                                paneRacing.getChildren().remove(paneRace);

                                // Create a task to run the Thread that make
                                Runnable race = new MakeRockets(mainPane, paneRace, finishedOrder);
                                threadRace = new Thread(race);
                                threadRace.start(); // Start Thread

                                // Add a new pane
                                paneRacing.getChildren().add(paneRace);

                                // Create a task to run the Thread that post
                                // the results of the race
                                Runnable results = new PostResults(paneDialog, finishedOrder,
                                        changingLabel, userChoice, bet, resetButton);
                                threadResult = new Thread(results);
                                threadResult.start(); // Start Thread
                            }
                        } catch (NumberFormatException e) {
                            // If there is no bet, changingLabel prints...
                            changingLabel
                                    .setText("C'mon! You need to bet something");
                        }
                    }
                });

        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
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
                        if (timeline != null)
                            timeline.stop();
                        timeSeconds.set(STARTTIME);
                        timeline = new Timeline();
                        timeline.getKeyFrames().add(
                                new KeyFrame(Duration.millis(STARTTIME + 4000),
                                        new KeyValue(timeSeconds, 0)));

                        // Reset Label and queue
                        changingLabel.setText("Ok...Make new bet");
                        finishedOrder = new LinkedList<String>();

                        // Stop the threads from finishing, in case you
                        // interrupted a race
                        threadRace.interrupt();
                        threadResult.interrupt();

                        // Clear and Remove old Pane
                        paneRacing.getChildren().clear();
                        mainPane.getChildren().remove(paneRace);

                        drawAllCar(false, paneRace);

                        // Add a new Pane
                        paneRacing.getChildren().addAll(paneRace);
                    }
                });
        // Add everything to the pane
    }


    private void showDialog() {
        if (paneDialog.getChildren().isEmpty()) {
            paneDialog.getChildren().addAll(menuPanelBackground, comboBox, startButton, resetButton,
                    textField, instructionLabel, changingLabel);
            mainPane.getChildren().add(paneDialog);
        }
    }

    private void hideDialog() {
        if (!paneDialog.getChildren().isEmpty()) {
            paneDialog.getChildren().clear();
            mainPane.getChildren().remove(paneDialog);
        }
    }

    public static void loadBackground(Pane pane) {
        // Load the image from a specific file
        String imgBackground = ResourceFile.getInstance().getImagePath("Map1.png");
        Image galaxy = new Image(imgBackground, 800.0, 800.0, false, true, false);

        // Painting the image
        ImageView imageView = new ImageView();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        imageView.setImage(galaxy);
        imageView.setX(bounds.getMinX());
        imageView.setY(bounds.getMinY());
        imageView.setFitWidth(bounds.getWidth());
        imageView.setFitHeight(bounds.getHeight());

        // Sets up picture background
        pane.getChildren().add(imageView);
    }

    // Draw a car method
    public static void drawCar(Group pane, double centerX, double centerY,
                               double scale, double angle, Color stripesColor,
                               Queue<String> finishOrder, String rocketNum, boolean runing) {
        String imgBackground = ResourceFile.getInstance().getImagePath("carBlack.png");
        if (stripesColor.equals(Color.ORANGERED)) {
            imgBackground = ResourceFile.getInstance().getImagePath("carRed.png");
        } else if (stripesColor.equals(Color.DEEPPINK)) {
            imgBackground = ResourceFile.getInstance().getImagePath("carPink.png");
        } else if (stripesColor.equals(Color.GREENYELLOW)) {
            imgBackground = ResourceFile.getInstance().getImagePath("carGreen.png");
        } else if (stripesColor.equals(Color.MEDIUMPURPLE)) {
            imgBackground = ResourceFile.getInstance().getImagePath("carViolet.png");
        } else if (stripesColor.equals(Color.DEEPSKYBLUE)) {
            imgBackground = ResourceFile.getInstance().getImagePath("carBlue.png");
        }

        Image galaxy = new Image(imgBackground, 175 * scale, 93 * scale, false, true, false);

        // Painting the image
        ImageView imageView = new ImageView();

        imageView.setImage(galaxy);
        imageView.setX(centerX);
        imageView.setY(centerY);

        Pane aux = new Pane();

        aux.getChildren().add(imageView);
        aux.setRotate(angle); // Rotate aux

        // Path Transition, only if it is going to race
        if (runing) {
            // Create a path
            Path path = new Path();
            /*path.getElements().add(new MoveTo(50, 80 + centerY * 0.5));// where the problem may be
            path.getElements().add(new LineTo(bounds.getWidth() - 100, 80 + centerY * 0.5));*/
            path.getElements().add(new MoveTo(50, 25 + centerY * 0.5));// where the problem may be
            path.getElements().add(new LineTo(bounds.getWidth() - 50, 25 + centerY * 0.5));
            path.setVisible(false);

            // Create a PathTransition
            PathTransition pathTransition = new PathTransition();
            // speed
            pathTransition.setDuration(Duration.millis(Math.random() * 2000 + 5000));
            pathTransition.setPath(path); // Set path to follow
            pathTransition.setNode(aux);
            pathTransition
                    .setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.play(); // Start Animation
            // Create a EventHandler to know who finished the race and in what position.
            pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    finishOrder.add(rocketNum);
                }
            });
            pane.getChildren().add(path);
        }
        pane.getChildren().add(aux);
    }

    public void drawAllCar(boolean isRun, Group paneRace) {
        drawCar(paneRace, -5, 150, 0.6, 360,
                Color.ORANGERED, finishedOrder, "1", isRun);
        drawCar(paneRace, -5, 255, 0.6, 360,
                Color.DEEPPINK, finishedOrder, "2", isRun);
        drawCar(paneRace, -5, 360, 0.6, 360,
                Color.GREENYELLOW, finishedOrder, "3", isRun);
        drawCar(paneRace, -5, 465, 0.6, 360,
                Color.MEDIUMPURPLE, finishedOrder, "4", isRun);
        drawCar(paneRace, -5, 575, 0.6, 360,
                Color.DEEPSKYBLUE, finishedOrder, "5", isRun);
    }



    // The task that prints the results of the race
    class PostResults implements Runnable {
        Queue<String> finishedOrder; // Race Positions
        Label changingLabel; // ChangingLabel
        Pane pane; // Our pane
        String userChoice; // User ComboBox selection
        double bet; // User Bet amount
        Button resetButton; // Reset Button
        interface CompleteCallBack {
            void runSuccess();
            void runFailed();
        }
        CompleteCallBack callBack;

        // Construct a task with specific values
        public PostResults(Pane pane, Queue<String> finishedOrder,
                           Label changingLabel, String userChoice, double bet, Button resetButton) {
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
                while (finishedOrder.size() < 5)
                    Thread.sleep(500);
            } catch (InterruptedException e) {
                return; // If sleep is interrupted it was due to reset,
            }

            String temp = ""; // Text to Display

            if (finishedOrder.poll().equals(userChoice)) // If userChoice finished 1rst...
                temp = ("Wow, You Just Won First!! You Earned $" + bet * 1000 + "!!!");
            else if (finishedOrder.poll().equals(userChoice)) // If userChoice finished 2nd...
                temp = ("Nice, You Got Second. You Earned $" + bet * 500 + "!!");
            else if (finishedOrder.poll().equals(userChoice)) // If userChoice finished 3rd...
                temp = ("Well, 3rd is better than none. You Earned $" + bet
                        * 250 + "!");
            else // If userChoice finished 4th or last...
                temp = ("Nope, Better Luck Next Time!!");

            final String result = temp;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // Set text giving results
                    changingLabel.setText(result);
                    // Can't Click reset until race is finish
                    resetButton.setDisable(false);
                    //callBack.runSuccess();
                    showDialog();
                }
            });
        }
    }

    class MakeRockets implements Runnable {
        Queue<String> finishedOrder; // Race Positions
        Group pane; // Our Pane

        // Construct a task with specific values
        public MakeRockets(Pane mainpane, Group pane, Queue<String> finishedOrder) {
            this.finishedOrder = finishedOrder;
            this.pane = pane;
            //mainpane.getChildren().remove(paneDialog);
            hideDialog();
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
                    pane.getChildren().clear(); // Clear old pane
                    drawCar(pane, -5, 150, 0.6, 360, Color.ORANGERED,
                            finishedOrder, "0", true);
                    drawCar(pane, -5, 255, 0.6, 360, Color.DEEPPINK,
                            finishedOrder, "1", true);
                    drawCar(pane, -5, 360, 0.6, 360, Color.GREENYELLOW,
                            finishedOrder, "2", true);
                    drawCar(pane, -5, 465, 0.6, 360, Color.MEDIUMPURPLE,
                            finishedOrder, "3", true);
                    drawCar(pane, -5, 575, 0.6, 360, Color.DEEPSKYBLUE,
                            finishedOrder, "4", true);
                }
            });
        }
    }
}
