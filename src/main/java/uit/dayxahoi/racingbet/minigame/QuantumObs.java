package uit.dayxahoi.racingbet.minigame;

import uit.dayxahoi.racingbet.util.ResourceFile;

public class QuantumObs extends Obstacle {

    private double initX, initY;
    private double timeSinceLastMove; // Delta time since last spawn
    private int tpRadius; // Spawn radius from obstacle's center

    /**
     * Constructor for the QuantumObs class.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param speed The obstacle's speed.
     */
    public QuantumObs(double x, double y, double speed) {

        super(x, y, speed);
        this.initX = this.getX();
        this.initY = this.getY();
        this.tpRadius = 30;
        setImg(ResourceFile.getInstance().getImagePath("quantumObs.png"));
    }

    /**
     * Updates the obstacle's x and y coordinate based on time. Gives it a random x and y in a specific radius to give
     * it the illusion of teleportation.
     * @param dt The amount of time it took to render the last frame.
     */
    @Override
    public void update(double dt) {

        timeSinceLastMove += dt;

        if (timeSinceLastMove >= 0.2) { // Move every 0.2 seconds
            initX = getX();
            // Random x and y value in a radius of tpRadius from initial x and y based on time
            setX(initX - (Obstacle.random(-tpRadius, tpRadius) + this.getSpeed() * dt));
            setY(initY - Obstacle.random(-tpRadius, tpRadius));
            timeSinceLastMove = 0;
        } else {
            setX(getX() - this.getSpeed() * dt);
        }
    }

    // GETTERS & SETTERS
    public double getInitX() {
        return initX;
    }

    public void setInitX(double initX) {
        this.initX = initX;
    }

    public double getInitY() {
        return initY;
    }

    public void setInitY(double initY) {
        this.initY = initY;
    }

    public double getTimeSinceLastMove() {
        return timeSinceLastMove;
    }

    public void setTimeSinceLastMove(double timeSinceLastMove) {
        this.timeSinceLastMove = timeSinceLastMove;
    }

    public int getTpRadius() {
        return tpRadius;
    }

    public void setTpRadius(int tpRadius) {
        this.tpRadius = tpRadius;
    }

}
