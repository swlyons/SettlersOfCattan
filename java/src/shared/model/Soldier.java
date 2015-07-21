package shared.model;

import shared.locations.HexLocation;

/**
 * contains information on playing a soldier card
 *
 * @author Aaron
 *
 */
public class Soldier extends Command {

    private int victimIndex;
    private HexLocation location;

    public Soldier(int playerIndex) {
        super("Soldier", playerIndex);
    }

    public int getVicitmIndex() {
        return victimIndex;
    }

    public void setVicitmIndex(int victimIndex) {
        this.victimIndex = victimIndex;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }
}
