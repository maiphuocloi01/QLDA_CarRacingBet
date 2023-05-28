package uit.dayxahoi.racingbet.minigame;

import uit.dayxahoi.racingbet.util.ResourceFile;

public class Coin extends GameObject {

    private int value;  // Coin's value in score upon taking
    private double speed;
    private boolean taken;

    /**
     * Constructor for the Coin class.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param speed The coin's speed.
     */
    public Coin(double x, double y, double speed) {

        super(x, y, 20, ResourceFile.getInstance().getImagePath("coin.png"));
        this.value = 10;
        this.speed = speed;
        this.taken = false;
    }

    /**
     * Updates the coin x coordinate based on it's speed.
     */
    @Override
    public void update(double dt) {
        setX(getX() - (speed * dt)); // Decrements coin's x coordinate by it's speed
    }

    // GETTERS & SETTERS
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

}
