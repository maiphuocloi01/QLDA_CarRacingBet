package uit.dayxahoi.racingbet.model;

public class Item {
    //private String name;
    private String path;

    public Item( String path) {
    //    this.name = name;
        this.path = path;
    }


    public void setPath(String path) {
        this.path = path;
    }



    public String getPath() {
        return path;
    }
}