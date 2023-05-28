package uit.dayxahoi.racingbet.minigame;

import uit.dayxahoi.racingbet.util.ResourceFile;

import java.util.ArrayList;

public class GameModel {

    private Player player;
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>(); // Contains all game obstacles in width of canvas
    private ArrayList<Coin> coins = new ArrayList<Coin>();             // Contains all game coins in width of canvas
    private final double OBS_MAX_RADIUS;                 // Maximum radius an obstacle can have
    private double spawnTimer, coinTimer, plusCoinTimer; // Somme of dela time since last obstacle or coin spawned
    private double gameWidth, gameHeight;
    private double initSpawnX, ySpawnRange;
    private boolean debugModeUsed, debugMode; // Was debug mode used during a round. Is debug mode on
    private boolean firstStart, isPaused;     // First round since launch. Is game on pause
    private double obsSpeed;
    private double numObsPassed;
    private boolean coinTaken;

    /**
     * Constructor for the GameModel class.
     * @param canvasWidth The width of the game's canvas.
     * @param canvasHeight The height of the game's canvas.
     */
    public GameModel(double canvasWidth, double canvasHeight) {

        this.player = new Player(canvasWidth / 2, canvasHeight / 2, ResourceFile.getInstance().getImagePath("player.png"));
        this.gameWidth = canvasWidth;
        this.gameHeight = canvasHeight;
        this.OBS_MAX_RADIUS = 45;
        this.obsSpeed = player.getAx();
        this.initSpawnX = gameWidth + OBS_MAX_RADIUS;
        this.ySpawnRange = gameHeight;
        this.spawnTimer = 0;
        this.coinTimer = 0;
        this.debugModeUsed = false;
        this.debugMode = false;
        this.firstStart = true;
        this.isPaused = false;
        this.numObsPassed = 0;
        this.coinTaken = false;
    }

    /**
     * Updates every logical component of the game.
     * @param dt The amount of time it took to render the last frame.
     */
    public void update(double dt) {

        updateCoins(dt);
        updateObstacles(dt);
        updateScore();
        updateHighScore();
        checkObsCollision();
        checkCoinCollision();
        player.update(dt);
    }

    /**
     * Creates a random obstacle and gives it a random initial y coordinate.
     */
    public void obsCreator() {

        double initSpawnY = Obstacle.random(0, (int) ySpawnRange);

        switch (Obstacle.random(1, 3)) {

            case 1:
                obstacles.add(new CircularObs(initSpawnX, initSpawnY, obsSpeed));
                break;
            case 2:
                obstacles.add(new SinObs(initSpawnX, initSpawnY, obsSpeed));
                break;
            case 3:
                obstacles.add(new QuantumObs(initSpawnX, initSpawnY, obsSpeed));
                break;
        }
    }

    /**
     * Updates the game's coins and removes the coins collected from the canvas.
     * @param dt The amount of time it took to render the last frame.
     */
    public void updateCoins(double dt) {

        coinTimer += dt;

        double initSpawnY = Obstacle.random(0, (int) ySpawnRange);

        if (coinTimer >= 7) { // Spawns coin every 7 seconds
            Coin coin = new Coin(initSpawnX, initSpawnY, obsSpeed);
            coins.add(coin);

            for (int i = 0; i < coins.size(); i++) { // Clears every coin in list if out of bounds
                if (coins.get(i).getX() <= coins.get(i).getRadius() * 2) {
                    coins.remove(i);
                }
            }
            coinTimer = 0;
        }

        for (int i = 0; i < coins.size(); i++) { // Draws all coins
            coins.get(i).update(dt);
        }
    }

    /**
     * Updates the game's obstacles and removes the obstacles out of the scene.
     * @param dt The amount of time it took to render the last frame.
     */
    public void updateObstacles(double dt) {

        spawnTimer += dt;

        if (spawnTimer >= 3) { // Spawns obstacle every 3 seconds
            obsCreator();

            for (int i = 0; i < obstacles.size(); i++) { // Clears every obstacle in list if out of bounds
                if (obstacles.get(i).getX() <= -obstacles.get(i).getRadius() * 2) {
                    obstacles.remove(i);
                }
            }
            spawnTimer = 0;
        }

        for (int i = 0; i < obstacles.size(); i++) { // Draws all obstacles
            obstacles.get(i).update(dt);
        }
    }

    /**
     * Updates the player's score after every two obstacle passed.
     */
    public void updateScore() {

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obs = obstacles.get(i);

            // Update game difficulty every two obstacle passed and adds to score
            if (player.getX() > obs.getX() && !obs.isPassed()) {
                numObsPassed++;
                obs.setPassed(true);
                if (numObsPassed == 2) {
                    player.setScore(player.getScore() + 5);
                    player.setAx(player.getAx() + 15);
                    player.setAy(player.getAy() + 15);
                    setObsSpeed(obsSpeed + 15);
                    numObsPassed = 0;
                }
            }
        }
    }

    /**
     * Updates the player's high score when the debug mode wasn't used and the player score is greater than the old high
     * score.
     */
    public void updateHighScore() {

        // HighScore is not updated if debug mode used
        if (player.getScore() > player.getHighScore() && !debugModeUsed) {
            player.setHighScore(player.getScore());
        }
    }

    /**
     * Checks for collision between the player and obstacles. Restarts the game if collision happens.
     */
    public void checkObsCollision() {

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obs = obstacles.get(i);

            if (player.intersects(obs) && !debugMode && !isPaused) { // If player collides with obstacle game restarts
                restart();
            }
        }
    }

    /**
     * Checks for coin collision between the player and any coin. Adds the value of the coin to the player's score and
     * removes the coin from the canvas.
     */
    public void checkCoinCollision() {

        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);

            if (player.intersects(coin) && !debugMode && !isPaused) { // Adds to score when player collides with coin
                if (!coin.isTaken()) {
                    player.setScore(player.getScore() + coin.getValue());
                    coins.remove(i);
                    coinTaken = true;
                }
            }
        }
    }

    /**
     * Restarts the game to it's initial state.
     */
    public void restart() {

        // Sets game initial values and clears all obstacles and coins
        player.setAx(120);
        player.setAy(500);
        player.setX(gameWidth / 2);
        player.setY(gameHeight / 2);
        player.setScore(0);
        setObsSpeed(120);
        setSpawnTimer(0);
        setCoinTimer(0);
        setPlusCoinTimer(0);
        setCoinTaken(false);
        debugModeUsed = false;
        firstStart = false;
        obstacles.clear();
        coins.clear();
    }

    // GETTERS & SETTERS
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setObstacles(ArrayList<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public void setCoins(ArrayList<Coin> coins) {
        this.coins = coins;
    }

    public double getSpawnTimer() {
        return spawnTimer;
    }

    public void setSpawnTimer(double spawnTimer) {
        this.spawnTimer = spawnTimer;
    }

    public double getCoinTimer() {
        return coinTimer;
    }

    public void setCoinTimer(double coinTimer) {
        this.coinTimer = coinTimer;
    }

    public double getPlusCoinTimer() {
        return plusCoinTimer;
    }

    public void setPlusCoinTimer(double plusCoinTimer) {
        this.plusCoinTimer = plusCoinTimer;
    }

    public double getGameWidth() {
        return gameWidth;
    }

    public void setGameWidth(double gameWidth) {
        this.gameWidth = gameWidth;
    }

    public double getGameHeight() {
        return gameHeight;
    }

    public void setGameHeight(double gameHeight) {
        this.gameHeight = gameHeight;
    }

    public double getInitSpawnX() {
        return initSpawnX;
    }

    public void setInitSpawnX(double initSpawnX) {
        this.initSpawnX = initSpawnX;
    }

    public double getySpawnRange() {
        return ySpawnRange;
    }

    public void setySpawnRange(double ySpawnRange) {
        this.ySpawnRange = ySpawnRange;
    }

    public boolean isDebugModeUsed() {
        return debugModeUsed;
    }

    public void setDebugModeUsed(boolean debugModeUsed) {
        this.debugModeUsed = debugModeUsed;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean isFirstStart() {
        return firstStart;
    }

    public void setFirstStart(boolean firstStart) {
        this.firstStart = firstStart;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public double getObsSpeed() {
        return obsSpeed;
    }

    public void setObsSpeed(double obsSpeed) {
        this.obsSpeed = obsSpeed;
    }

    public double getNumObsPassed() {
        return numObsPassed;
    }

    public void setNumObsPassed(double numObsPassed) {
        this.numObsPassed = numObsPassed;
    }

    public boolean isCoinTaken() {
        return coinTaken;
    }

    public void setCoinTaken(boolean coinTaken) {
        this.coinTaken = coinTaken;
    }

    public double getOBS_MAX_RADIUS() {
        return OBS_MAX_RADIUS;
    }

}
