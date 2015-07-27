package shared.model;

import shared.locations.EdgeLocation;
import shared.data.XYEdgeLocation;

/**
 * contains information about building a road
 *
 * @author Aaron
 *
 */
public class BuildRoad extends Command {

    private XYEdgeLocation roadLocation;
    private Boolean free;
    private Integer gameId;

    public XYEdgeLocation getRoadLocation() {
        return roadLocation;
    }

    public void setRoadLocation(XYEdgeLocation roadLocation) {
        this.roadLocation = roadLocation;
    }

    public Boolean isFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }
    
    public Integer getGameId(){
        return gameId;
    }
    
    public void setGameId(Integer gameId){
        this.gameId=gameId;
    }
}
