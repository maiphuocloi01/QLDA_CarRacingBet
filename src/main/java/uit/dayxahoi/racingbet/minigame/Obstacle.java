package uit.dayxahoi.racingbet.minigame;

import java.util.Random;

public abstract class Obstacle extends GameObject {

    private String type;
    private double speed;
    private boolean isPassed;

    /**
     * Constructor for the Obstacle class.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param speed The obstacle's speed.
     */
    public Obstacle(double x, double y, double speed) {

        super(x, y, random(25, 45), "img"); // Randomly generates a radius
        this.speed = speed;
        this.isPassed = false;
    }

    /**
     * Generates a random number between min and max.
     * @param min The minimum number.
     * @param max The maximum number.
     * @return A number between min and max.
     */
    public static int random(int min, int max) {

        Random random = new Random();
        return random.nextInt(max + 1 - min) + min;
    }

    // GETTERS & SETTERS
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }

}
