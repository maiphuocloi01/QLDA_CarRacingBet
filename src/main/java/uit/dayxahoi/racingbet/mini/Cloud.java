
package uit.dayxahoi.racingbet.mini;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uit.dayxahoi.racingbet.util.ResourceFile;

public class Cloud extends ImageView {

    public Cloud() {
        String img = ResourceFile.getInstance().getImagePath("cloud.png");
        setImage(new Image(img));
        setScaleX(Math.random() / 2.0 + 0.5);
        setScaleY(Math.random() / 2.0 + 0.5);
        setOpacity(0.5);
    }

}
