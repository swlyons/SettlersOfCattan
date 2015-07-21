package shared.model;

public class Command {

    private String type;
    private int playerIndex;

    public Command() {

    }

    public Command(String type, int playerIndex) {
        this.type = type;
        this.playerIndex = playerIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

}
