package shared.model;

import shared.locations.EdgeLocation;

/**
 * Contains the information involved in playing a road building card
 *
 * @author Aaron
 *
 */
public class Road_Building extends Command {

    private EdgeLocation spot1;
    private EdgeLocation spot2;
    private Integer gameId;

    public Road_Building(int playerIndex) {
        super("Road_Building", playerIndex);
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public EdgeLocation getSpot1() {
        return spot1;
    }

    public void setSpot1(EdgeLocation spot1) {
        this.spot1 = spot1;
    }

    public EdgeLocation getSpot2() {
        return spot2;
    }

    public void setSpot2(EdgeLocation spot2) {
        this.spot2 = spot2;
    }
}
