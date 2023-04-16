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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
import java.util.Random;

public class GameViewManager {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private static final Screen screen = Screen.getPrimary();
    private static final Rectangle2D bounds = screen.getVisualBounds();
    private static final double GAME_WIDTH = bounds.getWidth();
    private static final double GAME_HEIGHT = bounds.getHeight();

    private Stage menuStage;
    Random randomPositionGenerator;


    private ImageView star;
    //private SmallInfoLabel pointsLabel;
    private ImageView[] playerLifes;
    private int playerLife;
    private int points;
    private final static String GOLD_STAR_IMAGE = "/resources/star_gold.png";

    private final static int STAR_RADIUS = 12;
    private final static int SHIP_RADIUS = 27;
    private final static int METEOR_RADIUS = 20;

    static final Integer STARTTIME = 3;
    static Label timerLabel = new Label();
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
        //initializeStage();
        //createKeyListeners();
        randomPositionGenerator = new Random();

    }


    private void initializeStage() {
//        gamePane = new AnchorPane();
//        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
//        gameStage = new Stage();
//        gameStage.setScene(gameScene);
    }


    public void createNewGame(Stage menuStage) {

        gameStage = menuStage;
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);

        Pane pane = new Pane();
        Group paneRace = new Group();

        loadBackground(pane);
        //drawTrack(pane);

        Stop[] paintGradientMenu = new Stop[]{new Stop(0, Color.ORANGE),
                new Stop(4, Color.ORANGE)};

        // Fills with a linear color gradient pattern
        LinearGradient gray = new LinearGradient(0, 0, 0, 1, true,
                CycleMethod.NO_CYCLE, paintGradientMenu);

        // Create our Menu back with rounded corners
        String pathPanel = ResourceFile.getInstance().getImagePath("red_panel.png");
        Image imgPanel = new Image(pathPanel, 800.0, 800.0, false, true, false);

        ImageView menu = new ImageView();
        menu.setImage(imgPanel);
        menu.setY(bounds.getMaxY() / 6);
        menu.setX(bounds.getMaxX() / 4);
        menu.setFitWidth(bounds.getWidth() / 2);
        menu.setFitHeight(2*bounds.getHeight() / 3);

        /*Rectangle menu = new Rectangle(0, 0, 0, 0);
        menu.setY(bounds.getMaxY() / 6);
        menu.setX(bounds.getMaxX() / 4);
        menu.setWidth(bounds.getWidth() / 2);
        menu.setHeight(2*bounds.getHeight() / 3);

        menu.setArcWidth(30);
        menu.setArcHeight(30);
        menu.setFill(imageView); // Fill with Gradient*/

        // Creating the options for our ComboBox with Observable Array List
        ObservableList<String> options = FXCollections.observableArrayList(
                "RED", "PINK", "GREEN", "PURPLE", "BLUE");
        // Create our ComboBox and add the options
        ComboBox<String> comboBox = new ComboBox<String>(options);
        comboBox.setPromptText("Select Spaceship"); // Give instructions to
        comboBox.setLayoutX((bounds.getMaxX() / 4) + 150);                   // the user
        comboBox.setLayoutY(bounds.getMaxY() / 6);
        // Create a Cell Factory to customize our ComboBox
        comboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            public ListCell<String> call(ListView<String> p) {
                final ListCell<String> cell = new ListCell<String>() {
                    {
                        // Change text Font and size
                        super.setPrefWidth(20);
                        super.setFont(Font.font("Impact", FontWeight.BOLD, 14));
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        // Customize options
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            // If ComboBox contains word, add Color
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
        // Get userChoice from the ComboBox
        // You need to create a Changelistener of Objects to get value whenever
        // is change.
        comboBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Object>() {
                    public void changed(
                            @SuppressWarnings("rawtypes") ObservableValue observable,
                            Object oldValue, Object newValue) {
                        userChoice = newValue.toString();
                    }
                });

        // Create label showing a Instruction to the user
        Label instructionLabel = new Label("Enter a Betting Amount: ");
        instructionLabel.setLayoutX(bounds.getMaxX() / 2);
        instructionLabel.setLayoutY((bounds.getMaxY() / 2) - 30);
        // Change font and size
        instructionLabel.setFont(Font.font("Impact", FontWeight.BOLD, 20));

        // This label change depending on the situation
        Label changingLabel = new Label("");
        changingLabel.setLayoutX(bounds.getMaxX() / 2);
        changingLabel.setLayoutY((bounds.getMaxY() / 2) - 90);
        changingLabel.setFont(Font.font("Impact", FontWeight.BOLD, 18));

        // Create a text field where user can enter the amount to bet
        TextField textField = new TextField();
        textField.setLayoutX(bounds.getMaxX() / 2);
        textField.setLayoutY((bounds.getMaxY() / 2));
        textField.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        textField.setPromptText("$"); // Display the Dollar Sign($) PromptText

        // TIMER
        // Bind the timerLabel text property to the timeSeconds property
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.ORANGERED);
        timerLabel.setLayoutX((5*bounds.getMaxX() / 6));
        timerLabel.setLayoutY((bounds.getMaxY() / 10));
        timerLabel.setFont(Font.font("Impact", FontWeight.BOLD, 150));

        // Create our StartButton
        DXHButton startButton = new DXHButton("Start Race");
        // Create our Reset Button
        DXHButton resetButton = new DXHButton("Reset Race");

        // StartButton Properties
        //startButton.setFont(Font.font("Impact", FontWeight.BOLD, 31.5));
        startButton.setLayoutX((bounds.getMaxX() / 4) + 50);
        startButton.setLayoutY((4*bounds.getMaxY() / 6) + 10);
        // Create a EventHanler so when user Click the Button followings actions
        // going to happen
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
                                        .setText("Choose one Spaceship First!!");
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
                                                        + bet + " on the RED Spaceship.");
                                        break;
                                    case "1":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the PINK Spaceship.");
                                        break;
                                    case "2":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the GREEN Spaceship.");
                                        break;
                                    case "3":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the PURPLE Spaceship.");
                                        break;
                                    case "4":
                                        changingLabel
                                                .setText("Thanks for Betting!! \n You bet $"
                                                        + bet + " on the BLUE Spaceship.");
                                        break;
                                }
                                // Remove old pane
                                pane.getChildren().remove(paneRace);

                                // Create a task to run the Thread that make
                                // the rockets
                                Runnable race = new MakeRockets(paneRace, finishedOrder);
                                threadRace = new Thread(race);
                                threadRace.start(); // Start Thread

                                // Add a new pane
                                pane.getChildren().add(paneRace);

                                // Create a task to run the Thread that post
                                // the results of the race
                                Runnable results = new PostResults(pane, finishedOrder,
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

        // ResetButton Properties
        //resetButton.setFont(Font.font("Impact", FontWeight.BOLD, 30));
        resetButton.setLayoutX((2*bounds.getMaxX() / 4) + 100);
        resetButton.setLayoutY((4*bounds.getMaxY() / 6) + 10);
        // Create a EventHanler so when user Click the Button followings actions
        // going to happen
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (racing == false) // If not racing, do nothing
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
                        paneRace.getChildren().clear();
                        pane.getChildren().remove(paneRace);

                        // Reset Rockets
                        /*drawRocket(paneRace, -5, -50, 0.6, 360,
                                Color.ORANGERED, finishedOrder, "1", false);
                        drawRocket(paneRace, -5, 55, 0.6, 360, Color.DEEPPINK,
                                finishedOrder, "2", false);
                        drawRocket(paneRace, -5, 160, 0.6, 360,
                                Color.GREENYELLOW, finishedOrder, "3", false);
                        drawRocket(paneRace, -5, 265, 0.6, 360,
                                Color.MEDIUMPURPLE, finishedOrder, "4", false);
                        drawRocket(paneRace, -5, 375, 0.6, 360,
                                Color.DEEPSKYBLUE, finishedOrder, "5", false);*/

                        drawCar(paneRace, -5, 50, 0.6, 360,
                                Color.ORANGERED, finishedOrder, "1", false);
                        drawCar(paneRace, -5, 155, 0.6, 360, Color.DEEPPINK,
                                finishedOrder, "2", false);
                        drawCar(paneRace, -5, 260, 0.6, 360,
                                Color.GREENYELLOW, finishedOrder, "3", false);
                        drawCar(paneRace, -5, 365, 0.6, 360,
                                Color.MEDIUMPURPLE, finishedOrder, "4", false);
                        drawCar(paneRace, -5, 475, 0.6, 360,
                                Color.DEEPSKYBLUE, finishedOrder, "5", false);

                        // Add a new Pane
                        pane.getChildren().addAll(paneRace);
                    }
                });

        // Add everything to the pane
        pane.getChildren().addAll(menu, comboBox, startButton, resetButton,
                textField, instructionLabel, changingLabel, timerLabel);

        //drawAllCar(paneRace);
        // And then create the scene
        Scene scene2 = new Scene(pane, GAME_WIDTH, GAME_HEIGHT);
        //Scene scene1;
        gameStage.setTitle("Racing Bet");
        gameStage.setScene(scene2);
        gameStage.setMaximized(true);
        menuStage.hide();
        gameStage.show();
    }

    public static void loadBackground(Pane pane) {
        // Load the image from a specific file
        String imgBackground = ResourceFile.getInstance().getImagePath("Map3.png");
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

    // Draw a rocket method
    public static void drawRocket(Group pane, double centerX, double centerY,
                                  double scale, double angle, Color stripesColor,
                                  Queue<String> finishOrder, String rocketNum, boolean runing) {
        // Filling our Rocket with Gradient
        // Choose two Colors
        Stop[] paintGradientRocket = new Stop[]{new Stop(0, Color.GRAY),
                new Stop(1, Color.SILVER)};
        Stop[] paintGradientFire = new Stop[]{new Stop(0, Color.RED),
                new Stop(1, Color.YELLOW)};
        Stop[] paintGradientWings = new Stop[]{new Stop(0, Color.MEDIUMBLUE),
                new Stop(1, Color.DEEPSKYBLUE)};

        // Fills with a linear color gradient pattern
        LinearGradient gray = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, paintGradientRocket);
        LinearGradient red = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, paintGradientFire);
        LinearGradient blue = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, paintGradientWings);

        // Draw the wings. They are just one arc made to be almost a full
        // ellipse
        Arc wings = new Arc(centerX + 160 * scale, centerY + 190 * scale,
                70 * scale, 60 * scale, 220, 280);
        wings.setType(ArcType.ROUND);
        wings.setFill(blue); // Add Gradient Color to the wings
        wings.setStroke(Color.DARKBLUE);
        wings.setStrokeWidth(10 * scale);
        wings.setStrokeType(StrokeType.INSIDE);

        // Rocket body is made out of 3 pieces
        // First one elliptic arc up
        Arc bodySideUp = new Arc(centerX + 200 * scale, centerY + 200 * scale,
                100 * scale, 50 * scale, 10, 130);
        bodySideUp.setStroke(null);
        bodySideUp.setType(ArcType.ROUND);

        // Then one elliptic arc down
        Arc bodySideDown = new Arc(centerX + 200 * scale,
                centerY + 182 * scale, 100 * scale, 50 * scale, 220, 130);
        bodySideDown.setStroke(null);
        bodySideDown.setType(ArcType.ROUND);

        // Then a rectangle for the back and to fill body
        Rectangle fillRocket = new Rectangle(centerX + 123 * scale, centerY
                + 168 * scale, 60 * scale, 46 * scale);

        // For commodity, we used union to merge all 3 into one single shape
        Shape rocketBody = Shape.union(bodySideDown,
                Shape.union(bodySideUp, fillRocket));
        rocketBody.setFill(gray); // Add Gradient Color to our Rocket

        // This draw a nice red rocket head... It wouldn't be a rocket otherwise
        Arc rocketHead = new Arc(centerX + 310 * scale, centerY + 190 * scale,
                20 * scale, 10 * scale, 150, 60);
        rocketHead.setType(ArcType.ROUND);
        rocketHead.setFill(stripesColor);
        rocketHead.setStroke(stripesColor);
        rocketHead.setStrokeWidth(10);

        // Stripes...stripes are cool too. We made it with half an arc
        Arc rocketStripe = new Arc(centerX + 310 * scale,
                centerY + 190 * scale, 40 * scale, 35 * scale, 120, 120);
        rocketStripe.setType(ArcType.OPEN);
        rocketStripe.setFill(null);
        rocketStripe.setStrokeWidth(5 * scale);
        rocketStripe.setStroke(stripesColor);

        // We use Shape.intersect for better results, making the body a single
        // shape
        Shape finalRocketStripe = Shape.intersect(rocketStripe, rocketBody);
        finalRocketStripe.setFill(stripesColor);

        // It can have many stripes
        Arc rocketStripe2 = new Arc(centerX + 310 * scale, centerY + 190
                * scale, 150 * scale, 100 * scale, 120, 120);
        rocketStripe2.setType(ArcType.OPEN);
        rocketStripe2.setFill(null);
        rocketStripe2.setStrokeWidth(12 * scale);
        rocketStripe2.setStroke(stripesColor);

        // We used Shape.intersect again
        Shape finalRocketStripe2 = Shape.intersect(rocketStripe2, rocketBody);
        finalRocketStripe2.setFill(stripesColor);

        // Draw the back of the rocket with a line
        Line rocketBack = new Line(centerX + 124 * scale,
                centerY + 168 * scale, centerX + 124 * scale, centerY + 214
                * scale);
        rocketBack.setStrokeWidth(6 * scale);

        // Then we added it to to the final shape
        Shape finalRocketBody = Shape.intersect(rocketBack, rocketBody);
        finalRocketBody.setFill(Color.SILVER);

        // The fire that pushes the rocket, made with a Polyline Shape
        Polyline fire = new Polyline();
        fire.getPoints().addAll(
                centerX + 123.5 * scale,
                centerY + 168.0 * scale, centerX + 90.0 * scale,
                centerY + 150.0 * scale, centerX + 100.0 * scale,
                centerY + 168.0 * scale, centerX + 70.0 * scale,
                centerY + 160.0 * scale, centerX + 80.0 * scale,
                centerY + 180.0 * scale, centerX + 60.0 * scale,
                centerY + 190.0 * scale, centerX + 80.0 * scale,
                centerY + 200.0 * scale, centerX + 70.0 * scale,
                centerY + 220.0 * scale, centerX + 100.0 * scale,
                centerY + 213.0 * scale, centerX + 90.0 * scale,
                centerY + 231.0 * scale, centerX + 123.5 * scale,
                centerY + 213.0 * scale);

        fire.setStroke(Color.RED);
        fire.setFill(red);
        fire.setStrokeWidth(6 * scale);
        fire.setStrokeType(StrokeType.INSIDE);

        // Small window, if you are up there, might as well look at the stars
        Circle window = new Circle(centerX + 220 * scale,
                centerY + 190 * scale, 25 * scale, Color.AQUA);

        // Windows edge, something got to hold it
        Circle windowEdge = new Circle(centerX + 220 * scale, centerY + 190
                * scale, 25 * scale, null);
        windowEdge.setStroke(Color.BLACK);
        windowEdge.setStrokeWidth(5 * scale);
        windowEdge.setStrokeType(StrokeType.OUTSIDE);

        // The screws that hold it together, made the "screw"
        // sense by making the stroke a dash line.
        Circle windowScrews = new Circle(centerX + 220 * scale, centerY + 190
                * scale, 28 * scale, null);
        windowScrews.setStrokeWidth(2 * scale);
        // Made the dash intervals depending on the circle radius, this way it
        // can be scaled, and still have the same number of screws...also makes
        // them equidistant
        windowScrews.getStrokeDashArray().addAll(0d, (28 * scale * 3.14) / 10);
        windowScrews.setStrokeLineCap(StrokeLineCap.ROUND);
        windowScrews.setStroke(Color.SILVER); // Silver is flashy

        // Again an auxiliary Pane for rotation
        Pane aux = new Pane();

        // Order is important, add all the rocket body up to window, then the
        // Stitch, then the window edge and holder. This helps covering stitch
        // without need to go intersecting and subtracting shapes
        aux.getChildren().addAll(wings, fire, rocketBody, rocketHead, window,
                finalRocketStripe, finalRocketStripe2, finalRocketBody);
        // Call Stitch method
        drawStitch(aux, centerX + 190 * scale, centerY + 160 * scale,
                1.5 * scale, Color.CORNFLOWERBLUE, Color.DARKBLUE,
                Color.LIGHTBLUE);
        // Add shapes to cover Stitch
        aux.getChildren().addAll(windowEdge, windowScrews);
        aux.setRotate(angle); // Rotate aux

        Screen screen = Screen.getPrimary();

        Rectangle2D bounds = screen.getVisualBounds();


        // Path Transition, only if it is going to race
        if (runing) {
            // Create a path
            Path path = new Path();
            path.getElements().add(new MoveTo(50, 80 + centerY * 0.5));// where the problem may be
            path.getElements().add(new LineTo(bounds.getWidth() - 100, 80 + centerY * 0.5));
            path.setVisible(false);

            // Create a PathTransition
            PathTransition pathTransition = new PathTransition();
            // Duration of the race is Random, so the rockets go in difference
            // speed
            pathTransition.setDuration(Duration.millis(Math.random() * 2000 + 5000));
            pathTransition.setPath(path); // Set path to follow
            pathTransition.setNode(aux); // Set Rocket as the Node
            pathTransition
                    .setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.play(); // Start Animation
            // Create a EventHandler to know who finished the race and in what position.
            pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // When Rocket finish race get position
                    finishOrder.add(rocketNum);
                }
            });
            pane.getChildren().add(path);
        }
        pane.getChildren().add(aux);
    }

    // Draw a rocket method
    public static void drawCar(Group pane, double centerX, double centerY,
                               double scale, double angle, Color stripesColor,
                               Queue<String> finishOrder, String rocketNum, boolean runing) {
        String imgBackground = ResourceFile.getInstance().getImagePath("carBlack.png");
        if (stripesColor.equals(Color.ORANGERED)){
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

        Image galaxy = new Image(imgBackground, 175*scale, 93*scale, false, true, false);

        // Painting the image
        ImageView imageView = new ImageView();
        //Screen screen = Screen.getPrimary();
        //Rectangle2D bounds = screen.getVisualBounds();

        imageView.setImage(galaxy);
        imageView.setX(centerX);
        imageView.setY(centerY);
        //imageView.setFitWidth(bounds.getWidth());
        //imageView.setFitHeight(bounds.getHeight());

        Pane aux = new Pane();

        aux.getChildren().add(imageView);
        // Call Stitch method
//        drawStitch(aux, centerX + 190 * scale, centerY + 160 * scale,
//                1.5 * scale, Color.CORNFLOWERBLUE, Color.DARKBLUE,
//                Color.LIGHTBLUE);
        // Add shapes to cover Stitch
        //aux.getChildren().addAll(windowEdge, windowScrews);
        aux.setRotate(angle); // Rotate aux

        Screen screen = Screen.getPrimary();

        Rectangle2D bounds = screen.getVisualBounds();

        // Path Transition, only if it is going to race
        if (runing) {
            // Create a path
            Path path = new Path();
            path.getElements().add(new MoveTo(50, 80 + centerY * 0.5));// where the problem may be
            path.getElements().add(new LineTo(bounds.getWidth() - 100, 80 + centerY * 0.5));
            path.setVisible(false);

            // Create a PathTransition
            PathTransition pathTransition = new PathTransition();
            // Duration of the race is Random, so the rockets go in difference
            // speed
            pathTransition.setDuration(Duration.millis(Math.random() * 2000 + 5000));
            pathTransition.setPath(path); // Set path to follow
            pathTransition.setNode(aux); // Set Rocket as the Node
            pathTransition
                    .setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.play(); // Start Animation
            // Create a EventHandler to know who finished the race and in what position.
            pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // When Rocket finish race get position
                    finishOrder.add(rocketNum);
                }
            });
            pane.getChildren().add(path);
        }
        pane.getChildren().add(aux);
    }

    // Draw the Stitch method, only to mid body
    public static void drawStitch(Pane pane, double centerX, double centerY,
                                  double scale, Color bodyColor, Color darkBody, Color fill) {
        // Draw Stitch's head
        Circle head = new Circle(centerX + 20 * scale, centerY + 20 * scale,
                10 * scale);
        head.setFill(bodyColor); // Pass color in case you want to change it

        // Made Stitch's hair with a polygon
        Polygon hair = new Polygon();
        hair.setFill(bodyColor);
        hair.getPoints().addAll(
                centerX + 18.0 * scale, centerY + 12.0 * scale,
                centerX + 22.0 * scale, centerY + 12.0 * scale,
                centerX + 22.0 * scale, centerY + 9.0 * scale,
                centerX + 21.0 * scale, centerY + 10.0 * scale,
                centerX + 20.0 * scale, centerY + 9.0 * scale,
                centerX + 19.0 * scale, centerY + 10.0 * scale,
                centerX + 18.0 * scale, centerY + 9.0 * scale);

        // Both ears, a polyline
        Polyline leftEar = new Polyline();
        leftEar.getPoints().addAll(
                centerX + 14.0 * scale, centerY + 19.0 * scale,
                centerX + 10.0 * scale, centerY + 14.0 * scale,
                centerX + 8.0 * scale, centerY + 9.0 * scale,
                centerX + 5.0 * scale, centerY + 8.0 * scale,
                centerX + 4.0 * scale, centerY + 10.0 * scale,
                centerX + 5.0 * scale, centerY + 14.0 * scale,
                centerX + 6.0 * scale, centerY + 16.5 * scale,
                centerX + 12.0 * scale, centerY + 24.0 * scale);
        leftEar.setStrokeLineCap(StrokeLineCap.ROUND); // Round corners
        leftEar.setStroke(Color.LIGHTPINK); // Pass earColors in case you want
        leftEar.setFill(Color.LIGHTPINK); // to change it
        leftEar.setStrokeWidth(1);

        Polyline rightEar = new Polyline();
        rightEar.getPoints().addAll(
                centerX + 26.0 * scale, centerY + 19.0 * scale,
                centerX + 30.0 * scale, centerY + 14.0 * scale,
                centerX + 32.0 * scale, centerY + 9.0 * scale,
                centerX + 35.0 * scale, centerY + 8.0 * scale,
                centerX + 36.0 * scale, centerY + 10.0 * scale,
                centerX + 35.0 * scale, centerY + 14.0 * scale,
                centerX + 34.0 * scale, centerY + 16.5 * scale,
                centerX + 28.0 * scale, centerY + 24.0 * scale);
        rightEar.setStrokeLineCap(StrokeLineCap.ROUND); // Round corners
        rightEar.setStroke(Color.LIGHTPINK); // Pass earColors in case you want
        rightEar.setFill(Color.LIGHTPINK); // to change it
        rightEar.setStrokeWidth(1);

        // EYES
        // Helpers radiuses
        double xRadius = 3.0 * scale;
        double yRadius = 2.0 * scale;

        // Eyes are ellipses rotated. First the back of the eye, then the eye,
        // and then a small white circle for pupil.
        Circle pupilLeft = new Circle(centerX + 15 * scale, centerY + 20
                * scale, 0.8 * scale);
        pupilLeft.setFill(Color.WHITE);

        Ellipse eyeLeft = new Ellipse(centerX + 16 * scale, centerY + 21.5
                * scale, xRadius, yRadius);
        eyeLeft.setFill(Color.BLACK);
        eyeLeft.setRotate(135 / 2.0);

        Ellipse eyeLeftBack = new Ellipse(centerX + 15 * scale, centerY + 20
                * scale, xRadius * 1.5, yRadius * 1.5);
        eyeLeftBack.setFill(fill);
        eyeLeftBack.setRotate(135 / 2.0);

        Circle pupilRight = new Circle(centerX + 25 * scale, centerY + 20
                * scale, 0.8 * scale);
        pupilRight.setFill(Color.WHITE);

        Ellipse eyeRight = new Ellipse(centerX + 24 * scale, centerY + 21.5
                * scale, xRadius, yRadius);
        eyeRight.setFill(Color.BLACK);
        eyeRight.setRotate(225 / 2.0);

        Ellipse eyeRightBack = new Ellipse(centerX + 25 * scale, centerY + 20
                * scale, xRadius * 1.5, yRadius * 1.5);
        eyeRightBack.setFill(fill);
        eyeRightBack.setRotate(225 / 2.0);

        // NOSE, made with two ellipses
        double xNoseRadius = 2 * scale;
        double yNoseRadius = 1 * scale;

        Ellipse noseLength = new Ellipse(centerX + 20 * scale, centerY + 26
                * scale, xNoseRadius, yNoseRadius * 1.5);
        noseLength.setFill(darkBody);
        noseLength.setRotate(180 / 2.0);

        Ellipse noseWidth = new Ellipse(centerX + 20 * scale, centerY + 25
                * scale, xNoseRadius * 1.5, yNoseRadius * 1.5);
        noseWidth.setFill(darkBody);
        noseWidth.setRotate(360 / 2.0);

        // Stitch's hands are ellipses rotated
        Ellipse leftHand = new Ellipse(centerX + 15 * scale, centerY + 32
                * scale, xRadius * 1.5, yRadius * 1.5);
        leftHand.setFill(bodyColor);
        leftHand.setStroke(Color.BLACK);
        leftHand.setStrokeWidth(1);
        leftHand.setRotate(325 / 2.0);

        Ellipse rightHand = new Ellipse(centerX + 25 * scale, centerY + 32
                * scale, xRadius * 1.5, yRadius * 1.5);
        rightHand.setFill(bodyColor);
        rightHand.setStroke(Color.BLACK);
        rightHand.setStrokeWidth(1);
        rightHand.setRotate(45 / 2.0);

        // Body is made with rectangles with rounded edges
        Rectangle body = new Rectangle(centerX + 12 * scale, centerY + 28
                * scale, 16 * scale, 8 * scale);
        body.setArcWidth(8 * scale);
        body.setArcHeight(8 * scale);
        body.setFill(bodyColor);

        Rectangle insideBody = new Rectangle(centerX + 15 * scale, centerY + 28
                * scale, 10 * scale, 9 * scale);
        insideBody.setArcWidth(8 * scale);
        insideBody.setArcHeight(8 * scale);
        insideBody.setFill(fill); // Option to change color

        // Finally, add it all in the proper order
        pane.getChildren().addAll(body, insideBody, leftEar, rightEar, head,
                eyeRightBack, eyeRight, eyeLeftBack, eyeLeft, pupilLeft,
                pupilRight, noseLength, noseWidth, hair, rightHand, leftHand);
    }

    public static void drawTrack(Pane pane) {
        // Nested Loops to make our track
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 6; j++) {
                // i is the number of stars
                // j is the number of lines
                // Create a star with Polygon
                Polygon star = new Polygon();
                star.getPoints()
                        .addAll(15.00 + i * 21, 12.0 + j * 105,
                                13.05 + i * 21, 16.5 + j * 105, 9.00 + i * 21,
                                16.5 + j * 105, 12.0 + i * 21, 19.5 + j * 105,
                                10.5 + i * 21, 24.0 + j * 105, 15.0 + i * 21,
                                21.75 + j * 105, 19.5 + i * 21, 24.0 + j * 105,
                                18.0 + i * 21, 19.5 + j * 105, 21.0 + i * 21,
                                16.5 + j * 105, 17.1 + i * 21, 16.5 + j * 105);
                star.setRotate(i * 10); // Rotate start every loop
                star.setFill(Color.GHOSTWHITE); // Add Color
                pane.getChildren().add(star); // Add it to the pane

                // Create a FadeTransition
                // It looks like the stars are moving
                FadeTransition ft = new FadeTransition(Duration.millis(1000),
                        star); // Fade Duration
                ft.setFromValue(1.0);
                ft.setToValue(0.1);
                ft.setCycleCount(Timeline.INDEFINITE); // Infinite Fade
                ft.setAutoReverse(false); // Start from Dark to light Color
                ft.play(); // Play FadeTransition
            }
        }
    }

    // The task that prints the results of the race
    class PostResults implements Runnable {
        Queue<String> finishedOrder; // Race Positions
        Label changingLabel; // ChangingLabel
        Pane pane; // Our pane
        String userChoice; // User ComboBox selection
        double bet; // User Bet amount
        Button resetButton; // Reset Button

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
                    // Put task to sleep until all 5 rockets finish the race
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

            // Run after rockets finished the race
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // Set text giving results
                    changingLabel.setText(result);
                    // Can't Click reset until race is finish
                    resetButton.setDisable(false);
                }
            });
        }
    }

    // The task that draw the rockets
    class MakeRockets implements Runnable {
        Queue<String> finishedOrder; // Race Positions
        Group pane; // Our Pane

        // Construct a task with specific values
        public MakeRockets(Group pane, Queue<String> finishedOrder) {
            this.finishedOrder = finishedOrder;
            this.pane = pane;
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
                    // Draw rockets
                    /*drawRocket(pane, -5, -50, 0.6, 360, Color.ORANGERED,
                            finishedOrder, "0", true);
                    drawRocket(pane, -5, 55, 0.6, 360, Color.DEEPPINK,
                            finishedOrder, "1", true);
                    drawRocket(pane, -5, 160, 0.6, 360, Color.GREENYELLOW,
                            finishedOrder, "2", true);
                    drawRocket(pane, -5, 265, 0.6, 360, Color.MEDIUMPURPLE,
                            finishedOrder, "3", true);
                    drawRocket(pane, -5, 375, 0.6, 360, Color.DEEPSKYBLUE,
                            finishedOrder, "4", true);*/

                    drawCar(pane, -5, 50, 0.6, 360, Color.ORANGERED,
                            finishedOrder, "0", true);
                    drawCar(pane, -5, 155, 0.6, 360, Color.DEEPPINK,
                            finishedOrder, "1", true);
                    drawCar(pane, -5, 260, 0.6, 360, Color.GREENYELLOW,
                            finishedOrder, "2", true);
                    drawCar(pane, -5, 365, 0.6, 360, Color.MEDIUMPURPLE,
                            finishedOrder, "3", true);
                    drawCar(pane, -5, 475, 0.6, 360, Color.DEEPSKYBLUE,
                            finishedOrder, "4", true);
                }
            });
        }
    }
}
