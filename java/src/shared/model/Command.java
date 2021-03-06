package shared.model;

import java.io.Serializable;

public class Command implements Serializable {

    private String type;
    private Integer playerIndex;

    public Command() {

    }

    public Command(String type, Integer playerIndex) {
        this.type = type;
        this.playerIndex = playerIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(Integer playerIndex) {
        this.playerIndex = playerIndex;
    }

}
