package uit.dayxahoi.racingbet.util;

import uit.dayxahoi.racingbet.MyApplication;

import java.net.URL;
import java.util.Objects;

public class ResourceFile {
    private static ResourceFile instance;

    private ResourceFile() {

    }

    public static ResourceFile getInstance() {
        if (instance == null)
            instance = new ResourceFile();
        return instance;
    }

    public String getResPath(String filePath) {
        return Objects.requireNonNull(MyApplication.class.getResource(filePath)).toString();
    }

    public URL getResURL(String filePath) {
        return MyApplication.class.getResource(filePath);
    }

    public String getImagePath(String imageName) {
        String path = "drawable/" + imageName;
        return Objects.requireNonNull(MyApplication.class.getResource(path)).toString();
    }

    public URL getImageURL(String imageName) {
        String path = "drawable/" + imageName;
        return MyApplication.class.getResource(path);
    }

    public String getFontPath(String fontName) {
        String path = "font/" + fontName;
        return Objects.requireNonNull(MyApplication.class.getResource(path)).toString();
    }

    public URL getFontURL(String fontName) {
        String path = "font/" + fontName;
        return MyApplication.class.getResource(path);
    }

    public String getSoundPath(String imageName) {
        String path = "sound/" + imageName;
        return Objects.requireNonNull(MyApplication.class.getResource(path)).toString();
    }

    public URL getSoundURL(String imageName) {
        String path = "sound/" + imageName;
        return MyApplication.class.getResource(path);
    }
}
