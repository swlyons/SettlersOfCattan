package shared.model;

import shared.locations.HexLocation;

/**
 * Contains the information for a rob request to be made.
 *
 * @author Aaron
 *
 */
public class RobPlayer extends Command {

    private Integer victimIndex;
    private HexLocation location;
    private Integer gameId;

    public RobPlayer() {
    }

    public Integer getVictimIndex() {
        return victimIndex;
    }

    public void setVictimIndex(Integer victimIndex) {
        this.victimIndex = victimIndex;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
}
