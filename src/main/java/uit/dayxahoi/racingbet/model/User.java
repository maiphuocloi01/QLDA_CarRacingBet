package uit.dayxahoi.racingbet.model;

import javafx.beans.property.IntegerProperty;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private IntegerProperty gold;

    public User(String username, String password, IntegerProperty gold) {
        this.username = username;
        this.password = password;
        this.gold = gold;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGold() {
        return gold.get();
    }

    public IntegerProperty goldProperty() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold.set(gold);
    }
}
