package client.proxy;

import shared.locations.HexLocation;

/**
 * Contains the information for a rob request to be made.
 *
 * @author Aaron
 *
 */
public class RobPlayer extends Command {

    private int victimIndex;
    private HexLocation location;

    public RobPlayer(int playerIndex) {
        super("robPlayer", playerIndex);
    }
    
    public int getVictimIndex() {
        return victimIndex;
    }

    public void setVictimIndex(int victimIndex) {
        this.victimIndex = victimIndex;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }
}
