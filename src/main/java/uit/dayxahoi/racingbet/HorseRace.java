package uit.dayxahoi.racingbet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HorseRace extends Application{
	
	TextField tfBettingAmount = new TextField();
	TextField tfBettingHorse = new TextField();
	
	@Override
	public void start(Stage primaryStage) {
		
		Pane pane = new Pane();
		
		//this creates the track
		//Image track 			  = new Image("/resources/redhorse.jpg");
		//ImageView trackView 	  = new ImageView(track);
		/*
		 * these are the images of the horses used to put into the
		 * image view to then create the horse objects
		 */

		Image redHorseImage 	  = new Image(String.valueOf(getClass().getResource("redhorse.png")));
		ImageView redHorseView    = new ImageView(redHorseImage);
		
		Image blueHorseImage 	  = new Image(String.valueOf(getClass().getResource("bluehorse.png")));
		ImageView blueHorseView   = new ImageView(blueHorseImage);
		
		Image greenHorseImage 	  = new Image(String.valueOf(getClass().getResource("greenHorse.png")));
		ImageView greenHorseView  = new ImageView(greenHorseImage);
		
		Image yellowHorseImage 	  = new Image(String.valueOf(getClass().getResource("yellowHorse.png")));
		ImageView yellowHorseView = new ImageView(yellowHorseImage);
		
		Image brownHorseImage 	  = new Image(String.valueOf(getClass().getResource("brownHorse.png")));
		ImageView brownHorseView  = new ImageView(brownHorseImage);
		
		Horse redHorse    = new Horse(redHorseImage, redHorseView);
		Horse blueHorse   = new Horse(blueHorseImage, blueHorseView);
		Horse greenHorse  = new Horse(greenHorseImage, greenHorseView);
		Horse yellowHorse = new Horse(yellowHorseImage, yellowHorseView);
		Horse brownHorse  = new Horse(brownHorseImage, brownHorseView);
		
		//this sets the horse objects to a specific y position
		redHorse.getImageView().setY(5);
		blueHorse.getImageView().setY(135);
		greenHorse.getImageView().setY(265);
		yellowHorse.getImageView().setY(400);
		brownHorse.getImageView().setY(530);
		
		//this adds the horses and the track to the pane
		//pane.getChildren().add(trackView);
		pane.getChildren().add(redHorse.getImageView());
		pane.getChildren().add(blueHorse.getImageView());
		pane.getChildren().add(greenHorse.getImageView());
		pane.getChildren().add(yellowHorse.getImageView());
		pane.getChildren().add(brownHorse.getImageView());
		
		//this adds the reset button to the pane
		pane.getChildren().add(resetButton(redHorse, blueHorse, greenHorse,
				yellowHorse, brownHorse));
		
		//this adds the start button to the pane
		pane.getChildren().add(startButton(redHorse, blueHorse, greenHorse,
				yellowHorse, brownHorse));
		
		
		Label lbBettingAmount = new Label("Enter betting amount:");
		lbBettingAmount.relocate(10, 650);
		pane.getChildren().add(lbBettingAmount);
		
		tfBettingAmount.relocate(150, 650);
		pane.getChildren().add(tfBettingAmount);
		
		Label lbWhichHorse = new Label("Enter horse number\n you wish to be on:");
		lbWhichHorse.relocate(10, 600);
		pane.getChildren().add(lbWhichHorse);
		
		tfBettingHorse.relocate(150, 600);
		pane.getChildren().add(tfBettingHorse);
		
		Scene scene = new Scene(pane);
	    primaryStage.setTitle("HorseRace"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	
	/*
	 * this method checks whether or not the threads can start
	 */
	public boolean isAble(){
		boolean start = false;
		
		//checks if the user typed a letter and gives an error message
		if(tfBettingAmount.getText().matches(".*[a-zA-Z]+.*")){
			tfBettingAmount.setText("Sorry. Please enter an amount between "
					+ "1 and 1000");
		}
		
		//checks if the user typed a letter and gives an error message
		if(tfBettingHorse.getText().matches(".*[a-zA-Z]+.*")){
			tfBettingHorse.setText("Sorry. Please enter a number between "
					+ "1 and 5");
		}
		
		//checks if a user typed a number out of bounds gives an error message
		for(int i = 0; i < tfBettingAmount.getText().length(); i++){
			if(tfBettingAmount.getText().charAt(i) == '.'){
				tfBettingAmount.setText("Sorry. Please enter whole number between "
						+ "1 and 1000");
			}
		}
		
		//checks if a user typed a number out of bounds gives an error message
		for(int i = 0; i < tfBettingHorse.getText().length(); i++){
			if(tfBettingHorse.getText().charAt(i) == '.'){
				tfBettingHorse.setText("Sorry. Please enter whole number between "
						+ "1 and 5");
			}
		}
		
		//checks if a user typed a number out of bounds gives an error message
		try{
			if(Integer.parseInt(tfBettingAmount.getText()) < 1 
					|| Integer.parseInt(tfBettingAmount.getText()) > 1000){
				tfBettingAmount.setText("Sorry. Please enter an amount between "
						+ "1 and 1000");
			}
			if(Integer.parseInt(tfBettingHorse.getText()) < 1 
					|| Integer.parseInt(tfBettingHorse.getText()) > 5){
				tfBettingHorse.setText("Sorry. Please enter a number between 1 and 5");
			}
			else{
				start = true;
			}
		}catch(NumberFormatException e){
			if(tfBettingAmount.getText().isEmpty()){
				tfBettingAmount.setText("Sorry. Please enter an amount between"
						+ "1 and 1000");
			}
			if(tfBettingHorse.getText().isEmpty()){
				tfBettingHorse.setText("Sorry. Please enter a number between "
						+ "1 and 5");
			}
		}
		return start;
	}
	
	/*
	 * this method is called by the start button event handler
	 * this method starts the threads
	 */
	public void startButtonClick(Horse redHorse, Horse blueHorse, Horse greenHorse,
			Horse yellowHorse, Horse brownHorse){
		
		if(isAble() == true){
			Thread redHorseRun = new Thread(redHorse);
			Thread blueHorseRun = new Thread(blueHorse);
			Thread greenHorseRun = new Thread(greenHorse);
			Thread yellowHorseRun = new Thread(yellowHorse);
			Thread brownHorseRun = new Thread(brownHorse);
			redHorseRun.start();
			blueHorseRun.start();
			greenHorseRun.start();
			yellowHorseRun.start();
			brownHorseRun.start();
		}
	}
	
	//this creates the button to start the race
	public Pane startButton(Horse redHorse, Horse blueHorse, Horse greenHorse,
							Horse yellowHorse, Horse brownHorse){
		Pane pane = new Pane();
		
		Button startRace = new Button("Start");
		startRace.relocate(10, 650);
		startRace.setOnMouseClicked(e -> {startButtonClick(redHorse, blueHorse, greenHorse,
													yellowHorse, brownHorse);
										  Horse.setTfBettingAmount(tfBettingAmount.getText());
										  Horse.setUserChoice(tfBettingHorse.getText());});
		
		pane.getChildren().add(startRace);
		return pane;
	}
	
	/*
	 * this method is called by the reset button event handle
	 * it sets the horses back to y position 0 and clears the text fields
	 */
	public void resetButtonClick(Horse redHorse, Horse blueHorse, Horse greenHorse,
			Horse yellowHorse, Horse brownHorse){
		
		redHorse.getImageView().setX(0);
		blueHorse.getImageView().setX(0);
		greenHorse.getImageView().setX(0);
		yellowHorse.getImageView().setX(0);
		brownHorse.getImageView().setX(0);
		tfBettingAmount.setText("");
		tfBettingHorse.setText("");
	}
	
	//this creates the button to reset the race
	public Pane resetButton(Horse redHorse, Horse blueHorse, Horse greenHorse,
			Horse yellowHorse, Horse brownHorse){
		Pane pane = new Pane();
		
		Button resetRace = new Button("Reset");
		
		resetRace.setOnMouseClicked(e -> {resetButtonClick(redHorse, blueHorse, greenHorse,
				yellowHorse, brownHorse);});
		resetRace.relocate(100, 680);
		
		pane.getChildren().add(resetRace);
		return pane;
	}
	
	public static void main(String[] args){
		launch(args);
	}
}