package client.proxy;

import shared.locations.HexLocation;

/**
 * contains information on playing a soldier card
 *
 * @author Aaron
 *
 */
public class Soldier extends Command {

    private int vicitmIndex;
    private HexLocation location;

    public int getVicitmIndex() {
        return vicitmIndex;
    }

    public void setVicitmIndex(int vicitmIndex) {
        this.vicitmIndex = vicitmIndex;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }
}
