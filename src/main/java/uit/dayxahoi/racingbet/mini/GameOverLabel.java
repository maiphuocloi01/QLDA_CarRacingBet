package uit.dayxahoi.racingbet.mini;

public class GameOverLabel extends ScoreLabel {

    public GameOverLabel(double x, double y) {
        super(x, y);
        setPrefWidth(400);
        setTranslateX(x - 150);
        status.setTranslateX(100);

    }

}
