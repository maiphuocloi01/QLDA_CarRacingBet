package uit.dayxahoi.racingbet.util;

import javafx.scene.image.Image;

public class Res {

    public Image birdImgs[] = new Image[3];

    public Res() {
        try {
            for (int i = 0; i < birdImgs.length; i++) {
                String img = ResourceFile.getInstance().getImagePath("birdFrame" + i + ".png");
                birdImgs[i] = new Image(img);
            }

        } catch (Exception e) {
            System.out.println("Problem in loading resourses");
        }
    }

}
