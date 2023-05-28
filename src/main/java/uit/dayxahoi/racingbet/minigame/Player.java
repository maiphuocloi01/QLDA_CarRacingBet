package uit.dayxahoi.racingbet.minigame;

public class Player extends GameObject {

    private double vy; // Player's rebound
    private double ax, ay; // Player's speed and gravity
    private int score, highScore;
    private double jumpSpeed; // Value of change in gravity after user input

    /**
     * Constructor for the Player class.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param img The player's image path.
     */
    public Player(double x, double y, String img) {

        super(x, y, 30, img);
        this.ax = 120;
        this.ay = 500;
        this.score = 0;
        this.highScore = 0;
        this.jumpSpeed = -300;
    }

    /**
     * Updates the player's y coordinate based on time and gravity. Also restricts the player movement to the canvas's
     * height.
     * @param dt The amount of time it took to render the last frame.
     */
    @Override
    public void update(double dt) {

        setVy(getVy() + dt * ay);
        setY(getY() + dt * vy);

        if (getY() + getRadius() > 400 || getY() - getRadius() < 0) { // Keeps player in canvas borders
            setVy(getVy() * -0.9); // Slowly brings player to static state
        }

        setY(Math.min(getY(), 400 - getRadius()));
        setY(Math.max(getY(), getRadius()));
    }

    // GETTERS & SETTERS
    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {

        if (vy > 300) { // Keeps vy to a range of 300 to -300
            vy = 300;
        } else if (vy < -300) {
            vy = -300;
        } else {
            this.vy = vy;
        }
    }

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public double getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(double jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

}
