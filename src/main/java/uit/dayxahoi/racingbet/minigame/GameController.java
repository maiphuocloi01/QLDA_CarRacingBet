package uit.dayxahoi.racingbet.minigame;

import uit.dayxahoi.racingbet.view.MiniGameViewManager;

public class GameController {

    private MiniGameViewManager view;
    private GameModel gameModel;

    /**
     * Constructor for the GameController class.
     * @param view The GUI.
     */
    public GameController(MiniGameViewManager view) {

        this.view = view;
        this.gameModel = new GameModel(view.getCANVAS_WIDTH(), view.getCANVAS_HEIGHT());
    }

    /**
     * Calls the model's update method to update every logic element of the game.
     * @param dt The amount of time it took to render the last frame.
     */
    public void update(double dt) {
        gameModel.update(dt);
    }

    // GETTERS & SETTERS
    public MiniGameViewManager getView() {
        return view;
    }

    public void setView(MiniGameViewManager view) {
        this.view = view;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

}