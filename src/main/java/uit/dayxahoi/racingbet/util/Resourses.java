/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.dayxahoi.racingbet.util;

import javafx.scene.image.Image;

public class Resourses {

    public Image birdImgs[] = new Image[3];

    public Resourses() {
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
