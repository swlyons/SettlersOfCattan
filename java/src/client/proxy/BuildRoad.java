package client.proxy;

import shared.locations.EdgeLocation;

/**
 * contains information about building a road
 *
 * @author Aaron
 *
 */
public class BuildRoad extends Command {

    private EdgeLocation roadLocation;
    private boolean free;

    public EdgeLocation getRoadLocation() {
        return roadLocation;
    }

    public void setRoadLocation(EdgeLocation roadLocation) {
        this.roadLocation = roadLocation;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
