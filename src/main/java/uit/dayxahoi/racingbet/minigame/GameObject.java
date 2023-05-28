package uit.dayxahoi.racingbet.minigame;

public abstract class GameObject {

    private double x, y;
    private double radius, diameter;
    private String img;

    /**
     * Constructor for the GameObject class.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param radius The object's collision radius.
     * @param img The object's image path.
     */
    public GameObject (double x, double y, double radius, String img) {

        this.x = x;
        this.y = y;
        this.radius = radius;
        this.img = img;
        this.diameter = this.radius * 2;
    }

    /**
     * Updates the object's x and y coordinate based on time.
     * @param dt The amount of time it took to render the last frame.
     */
    public abstract void update(double dt);

    /**
     * Checks for collision between two game objects.
     * @param gameObj The game object he checks with for collision.
     * @return If collision happened.
     */
    public boolean intersects(GameObject gameObj) {

        double distX = this.getX() - gameObj.getX();
        double distY = this.getY() - gameObj.getY();;
        double distanceBetween = distX * distX + distY * distY;

        // Objects or intersecting if distance between their centers in less than the somme of their radius
        return distanceBetween < (this.getRadius() + gameObj.getRadius()) * (this.getRadius() + gameObj.getRadius());
    }

    // GETTERS & SETTERS
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
