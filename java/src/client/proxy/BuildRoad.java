package client.proxy;

import shared.locations.EdgeLocation;
import client.data.XYEdgeLocation;
/**
 * contains information about building a road
 *
 * @author Aaron
 *
 */
public class BuildRoad extends Command {

    private XYEdgeLocation roadLocation;
    private boolean free;

    public XYEdgeLocation getRoadLocation() {
        return roadLocation;
    }

    public void setRoadLocation(XYEdgeLocation roadLocation) {
        this.roadLocation = roadLocation;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
