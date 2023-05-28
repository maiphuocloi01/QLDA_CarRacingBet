package uit.dayxahoi.racingbet.minigame;

import uit.dayxahoi.racingbet.util.ResourceFile;

public class SinObs extends Obstacle {

    private double initY;
    private double initMotionValue; // Different starting point on function curve
    private double timeSinceStart;  // Since creation of obstacle
    private double motionRange;     // Range of the function

    /**
     * Constructor for the SinObs class.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param speed The obstacle's speed.
     */
    public SinObs(double x, double y, double speed) {

        super(x, y, speed);
        this.initY = this.getY();
        this.initMotionValue = Obstacle.random(0, 10);
        this.motionRange = 50;
        setImg(ResourceFile.getInstance().getImagePath("sinObs.png"));
    }

    /**
     * Updates the obstacle's x and y coordinate based on time. Makes it follow the sin function.
     * @param dt The amount of time it took to render the last frame.
     */
    @Override
    public void update(double dt) {

        timeSinceStart += dt;

        setX(getX() - (this.getSpeed() * dt));
        // Mimics sinus function movement based on time
        setY(initY + motionRange * Math.sin(initMotionValue + timeSinceStart * 5));
    }

    // GETTERS & SETTERS
    public double getInitY() {
        return initY;
    }

    public void setInitY(double initY) {
        this.initY = initY;
    }

    public double getInitMotionValue() {
        return initMotionValue;
    }

    public void setInitMotionValue(double initMotionValue) {
        this.initMotionValue = initMotionValue;
    }

    public double getTimeSinceStart() {
        return timeSinceStart;
    }

    public void setTimeSinceStart(double timeSinceStart) {
        this.timeSinceStart = timeSinceStart;
    }

    public double getMotionRange() {
        return motionRange;
    }

    public void setMotionRange(double motionRange) {
        this.motionRange = motionRange;
    }

}
