package uit.dayxahoi.racingbet.minigame;

import uit.dayxahoi.racingbet.util.ResourceFile;

public class CircularObs extends Obstacle {

    private final double MAX_X, MAX_Y;
    private double angle;

    /**
     * Constructor for the CircularObs class.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param speed The obstacle's speed.
     */
    public CircularObs(double x, double y, double speed) {

        super(x, y, speed);
        this.MAX_X = 3;
        this.MAX_Y = 3;
        this.angle = 0;
        setImg(ResourceFile.getInstance().getImagePath("circularObs.png"));
    }

    /**
     * Updates the obstacle's x and y coordinate based on time. Makes it advance towards the player in a circular
     * motion.
     * @param dt The amount of time it took to render the last frame.
     */
    @Override
    public void update(double dt) {

        if (dt != 0) { // Game is not paused
            angle += 0.05;
            double changeX = Math.cos(angle) * MAX_X;
            double changeY = Math.sin(angle) * MAX_Y;

            // Gets new coordinate on circle
            setX(getX() - (this.getSpeed() * dt)  - changeX);
            setY(getY() - changeY);
        }
    }

    // GETTERS & SETTERS
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getMAX_X() {
        return MAX_X;
    }

    public double getMAX_Y() {
        return MAX_Y;
    }

}
