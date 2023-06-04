package uit.dayxahoi.racingbet.view;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import uit.dayxahoi.racingbet.model.Item;
import javafx.geometry.Pos;

import java.io.File;
import java.net.URI;

public class StoredView extends Application {
    private ObservableList<Item> gameItems;
    private ListView<Item> gameListView;
    private int monNey = 100;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameItems = FXCollections.observableArrayList();

        gameListView = new ListView<>();
        gameListView.setItems(gameItems);

        TabPane tabPane = new TabPane();
        Tab skinXeTab = new Tab("SKIN XE");
        Tab mapTab = new Tab("MAP");

        skinXeTab.setStyle("-fx-font-weight: bold;");
        mapTab.setStyle("-fx-font-weight: bold;");

        setTabContentSkinXeTab(skinXeTab);
        setTabContentMapTab(mapTab);

        tabPane.getTabs().addAll(skinXeTab, mapTab);
        String tabContentStyle = "-fx-background-color: rgba(150, 111, 51, 0.5);"; // Mã màu nâu nhạt và độ trong suốt
        tabPane.setStyle(tabContentStyle);


        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tabPane);
        borderPane.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(borderPane);
        StackPane.setAlignment(borderPane, Pos.CENTER);

        String imagePath = "D:/IntelliJ/DoAnGame/QLDA_CarRacingBet/src/main/resources/uit/dayxahoi/racingbet/drawable/background.png";

        // Tạo ImageView từ hình ảnh
        Image backgroundImage = new Image(new File(imagePath).toURI().toString());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(stackPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(stackPane.heightProperty());

        // Đặt ImageView làm nền của StackPane
        stackPane.getChildren().add(0, backgroundImageView);



        Scene scene = new Scene(stackPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/StyleForStored.css").toExternalForm()); // Thêm CSS vào Scene
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Storage");
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Lấy kích thước của màn hình chính
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Tính toán kích thước của tabPane
        double tabPaneWidth = screenWidth * 4 / 5;
        double tabPaneHeight = screenHeight * 4 / 5;

        // Đặt kích thước của tabPane
        tabPane.setPrefSize(tabPaneWidth, tabPaneHeight);

        // Đặt khoảng cách xung quanh tabPane
        stackPane.setPadding(new Insets((screenHeight - tabPaneHeight) / 2, (screenWidth - tabPaneWidth) / 2,
                (screenHeight - tabPaneHeight) / 2, (screenWidth - tabPaneWidth) / 2));

    }


    private void setTabContentSkinXeTab(Tab tab) {
        String styleContent = "-fx-background-color: rgba(150, 111, 51, 0.5);"; // Mã màu nâu nhạt và độ trong suốt
        VBox leftContent = new VBox();
        leftContent.setStyle(styleContent);
        leftContent.setPadding(new Insets(15, 0, 0, 0));
        // Thêm các thành phần khác vào leftContent
        Label label = new Label("Vật phẩm đã mua");
        label.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px;");
        leftContent.setAlignment(Pos.TOP_CENTER);
        GridPane labelContainer = new GridPane(); // GridPane để chứa các label ô vuông
        labelContainer.setStyle("-fx-background-color: rgba(150, 111, 51, 0.5);");
        labelContainer.setHgap(5); // Khoảng cách giữa các label ngang
        labelContainer.setVgap(5); // Khoảng cách giữa các label dọc

        loadDSItemDaMua(labelContainer);

        ScrollPane scrollPane = new ScrollPane(labelContainer); // Sử dụng ScrollPane để cuộn khi nội dung quá dài
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(600, 300);
        //setClickedListenerScrollPane(scrollPane);
        leftContent.setSpacing(10);
        //rightContent.getChildren().addAll(label1,scrollPane);

        // Áp dụng CSS để ẩn thanh phân chia và vô hiệu hóa sự kiện kéo thả
        VBox labelContainer2 = new VBox(); // VBox để chứa label1 và scrollPane
        labelContainer2.setStyle("-fx-background-color: rgba(150, 111, 51, 0.5);");
        labelContainer2.setSpacing(15);
        labelContainer2.setAlignment(Pos.TOP_CENTER);
        labelContainer2.getChildren().addAll(label, scrollPane);

        leftContent.getChildren().addAll(labelContainer2);


        HBox rightContent = new HBox();
        rightContent.setStyle(styleContent);
        // Thêm các thành phần khác vào rightContent nếu cần
        setRightContent(rightContent, 13, labelContainer);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftContent, rightContent);
        splitPane.setDividerPositions(0.2); // Tỉ lệ chia 1/4 và 3/4

        tab.setContent(splitPane);
    }

    private void setTabContentMapTab(Tab tab) {
        String styleContent = "-fx-background-color: rgba(150, 111, 51, 0.5);"; // Mã màu nâu nhạt và độ trong suốt
        VBox leftContent = new VBox();
        leftContent.setStyle(styleContent);
        leftContent.setPadding(new Insets(15, 0, 0, 0));
        Label label = new Label("Vật phẩm đã mua");
        label.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px;");
        leftContent.setAlignment(Pos.TOP_CENTER);
        leftContent.getChildren().add(label);


        // Thêm các thành phần khác vào leftContent nếu cần


        HBox rightContent = new HBox();
        rightContent.setStyle(styleContent);
        // Thêm các thành phần khác vào rightContent nếu cần
        rightContent.setPadding(new Insets(10, 0, 0, 0));
        Label label1 = new Label("Cửa hàng");
        label1.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px;");
        rightContent.setAlignment(Pos.TOP_CENTER);
        rightContent.getChildren().add(label1);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftContent, rightContent);
        splitPane.setDividerPositions(0.2); // Tỉ lệ chia 1/4 và 3/4

        tab.setContent(splitPane);
    }

    private void loadDSItemDaMua(GridPane labelContainer) {
        int columns = 6; // Số cột trong lưới
        for (int i = 0; i < gameItems.size(); i++) {
            Item item = gameItems.get(i);
            Label labelItem = new Label();
            ImageView imageView = new ImageView();
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            String imagePath = item.getPath();
            System.out.println(item.getPath());
            Image image = new Image(new File(imagePath).toURI().toString());
            imageView.setImage(image);

            // Đặt ImageView làm nội dung của label
            labelItem.setGraphic(imageView);
            labelItem.setStyle("-fx-background-color: #efc5a8; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px;");
            labelItem.setAlignment(Pos.CENTER);
            labelItem.setMaxWidth(Double.MAX_VALUE);
            labelItem.setMaxHeight(Double.MAX_VALUE);

            // Tính toán vị trí hàng và cột của label trong lưới
            int row = i / columns;
            int col = i % columns;

            // Đặt label vào lưới tại vị trí hàng và cột tương ứng
            labelContainer.add(labelItem, col, row);
        }
    }

    private void setRightContent(Node node, int number, GridPane container) {
        if (node instanceof HBox rightContent) {
            rightContent.setPadding(new Insets(10, 0, 0, 0));
            Label label1 = new Label("Cửa hàng");
            label1.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px;");
            rightContent.setAlignment(Pos.TOP_CENTER);

            GridPane labelContainer = new GridPane(); // GridPane để chứa các label ô vuông
            labelContainer.setStyle("-fx-background-color: rgba(150, 111, 51, 0.5);");
            labelContainer.setHgap(5); // Khoảng cách giữa các label ngang
            labelContainer.setVgap(5); // Khoảng cách giữa các label dọc

            int columns = 5; // Số cột trong lưới
            String imagePath = "D:/IntelliJ/DoAnGame/QLDA_CarRacingBet/src/main/resources/uit/dayxahoi/racingbet/SkinXe/";
            for (int i = 0; i < number; i++) {
                Label labelItem = new Label();
                ImageView imageView = new ImageView();
                imageView.setFitWidth(92);
                imageView.setFitHeight(92);

                String imageFileName = "image" + (i + 1) + ".png";
                String absoluteImagePath = new File(imagePath, imageFileName).toURI().toString();

                Image image = new Image(absoluteImagePath);
                imageView.setImage(image);

                // Đặt ImageView làm nội dung của label
                labelItem.setGraphic(imageView);
                labelItem.setStyle("-fx-background-color: #efc5a8; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px;");
                labelItem.setAlignment(Pos.CENTER);
                labelItem.setMaxWidth(Double.MAX_VALUE);
                labelItem.setMaxHeight(Double.MAX_VALUE);

                // Tính toán vị trí hàng và cột của label trong lưới
                int row = i / columns;
                int col = i % columns;

                // Đặt label vào lưới tại vị trí hàng và cột tương ứng
                labelContainer.add(labelItem, col, row);
            }

            ScrollPane scrollPane = new ScrollPane(labelContainer); // Sử dụng ScrollPane để cuộn khi nội dung quá dài
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefSize(600, 300);
            setClickedListenerScrollPane(scrollPane, container);
            rightContent.setSpacing(10);
            //rightContent.getChildren().addAll(label1,scrollPane);

            // Áp dụng CSS để ẩn thanh phân chia và vô hiệu hóa sự kiện kéo thả
            VBox labelContainer2 = new VBox(); // VBox để chứa label1 và scrollPane
            labelContainer2.setStyle("-fx-background-color: rgba(150, 111, 51, 0.5);");
            labelContainer2.setSpacing(15);
            labelContainer2.setAlignment(Pos.TOP_CENTER);
            labelContainer2.getChildren().addAll(label1, scrollPane);

            rightContent.getChildren().addAll(labelContainer2);
        }
    }


    private void setClickedListenerScrollPane(ScrollPane scrollPane, GridPane labelContainer) {
        scrollPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Node target = (Node) event.getTarget();
            if (target instanceof Label) {
                // Xử lý sự kiện click cho label
                Label clickedLabel = (Label) target;
                ImageView imageView = (ImageView) clickedLabel.getGraphic();
                Image image = imageView.getImage();
                String imageUrl = image.getUrl();
                URI uri = URI.create(imageUrl);
                String imagePath = uri.getPath();
                gameItems.add(new Item(imagePath));


                // Thay đổi màu label khi clicked
                clickedLabel.setStyle("-fx-background-color: 966F337F;");

                // Tạo một đối tượng Timeline để trở lại màu như cũ sau một khoảng thời gian
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0.15), e -> {
                            // Trở lại màu như cũ
                            clickedLabel.setStyle("-fx-background-color: #efc5a8; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px;");
                        })
                );
                timeline.play();
                loadDSItemDaMua(labelContainer);
            }
            event.consume();
        });
    }


}


