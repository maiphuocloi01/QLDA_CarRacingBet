package uit.dayxahoi.racingbet.model;

import java.util.Random;

import javax.swing.JOptionPane;


import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Horse implements Runnable{
	
	private Image image;
	private ImageView imageView;
	private static int counter = 0;
	private static String tfBettingHorse;
	private static int firstHorse = 5;
	private static int secondHorse = 135;
	private static int thirdHorse = 265;
	private static int fourthHorse = 400;
	private static int fifthHorse = 530;
	private static String tfBettingAmount;
	
	public Horse(Image image, ImageView imageView){
		this.image = image;
		this.imageView = imageView;
	}

	public String getUserChoice() {
		return tfBettingHorse;
	}

	public static void setUserChoice(String userChoiceSTR) {
		Horse.tfBettingHorse = userChoiceSTR;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	
	public static int getCounter() {
		return counter;
	}
	
	//this checks which horse the user chose
	public int userHorseChoice(){
		int userChoiceINT = 0;
		if(tfBettingHorse.equals("1")){
			userChoiceINT = firstHorse;
			return userChoiceINT;
		}
		else if(tfBettingHorse.equals("2")){
			userChoiceINT = secondHorse;
			return userChoiceINT;
		}
		else if(tfBettingHorse.equals("3")){
			userChoiceINT = thirdHorse;
			return userChoiceINT;
		}
		else if(tfBettingHorse.equals("4")){
			userChoiceINT = fourthHorse;
			return userChoiceINT;
		}
		else if(tfBettingHorse.equals("5")){
			userChoiceINT = fifthHorse;
			return userChoiceINT;
		}
		return userChoiceINT;
	}
	
	//this method gets how much the user bet
	public static void setTfBettingAmount(String tfBettingAmount) {
		Horse.tfBettingAmount = tfBettingAmount;
	}

	/*
	 * this method checks which horse won the race
	 * and lets the user know how their horse did in the race
	 */
	private void bettingResults(String raceResults){
		int earnings = 0;
		if(raceResults.equals("first place")){
			earnings = Integer.parseInt(tfBettingAmount) * 1000;
			JOptionPane.showMessageDialog(null, "Your Horse Won\n"
					+ "You won $" + earnings);
		}
		else if(raceResults.equals("second place")){
			earnings = Integer.parseInt(tfBettingAmount) * 500;
			JOptionPane.showMessageDialog(null, "Your Horse Placed 2nd\n"
					+ "You won $" + earnings);
		}
		else if(raceResults.equals("third place")){
			earnings = Integer.parseInt(tfBettingAmount) * 250;
			JOptionPane.showMessageDialog(null, "Your Horse Placed 3rd\n"
					+ "You won $" + earnings);
		}
		else if(raceResults.equals("lost")){
			earnings = Integer.parseInt(tfBettingAmount) * 250;
			JOptionPane.showMessageDialog(null, "Your Horse Lost\n"
					+ "You won no money\n.Better luck next time.");
		}
	}
	
	//this method starts the thread
	public void run() {
		
		boolean isAlive = true;
		Random randGen = new Random();
		String raceResults = "";
		
		//this loops checks if a horse is still running
		while(isAlive){
			Platform.runLater(new Runnable(){
				public void run() {
						imageView.setX(imageView.getX() + randGen.nextInt(20));
				}
			});
			if(imageView.getX() < 640){
				try{
					Thread.sleep(100);
				} 
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			else{
				isAlive = false;
				//this checks the order in which the horses finished the race
				synchronized(imageView){
					counter++;
					if(counter == 1 && imageView.getY() == userHorseChoice()){
						raceResults = "first place";
						bettingResults(raceResults);
					}
					else if(counter == 2 && imageView.getY() == userHorseChoice()){
						raceResults = "second place";
						bettingResults(raceResults);
					}
					else if(counter == 3 && imageView.getY() == userHorseChoice()){
						raceResults = "third place";
						bettingResults(raceResults);
					}
					else if(counter == 4 && imageView.getY() == userHorseChoice()){
						raceResults = "lost";
						bettingResults(raceResults);
					}
					else if(counter == 5 && imageView.getY() == userHorseChoice()){
						raceResults = "lost";
						bettingResults(raceResults);
					}
				}
			}
		}
		if(counter == 5){
			counter = 0;
		}
	}
}