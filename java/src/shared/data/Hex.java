package shared.data;

import java.util.ArrayList;

import shared.definitions.HexType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

public class Hex {

    private HexLocation location;
    private ArrayList<EdgeLocation> edgeLocations;
    private ResourceType resource;
    private HexType type;
    private boolean hasRobber;
    private int number;

    public Hex(HexLocation hexStart, int rollValueStart, ResourceType resourceTypeStart) {
        location = hexStart;
        number = rollValueStart;
        resource = resourceTypeStart;
        hasRobber = false;
    }

    public ArrayList<EdgeLocation> getEdgeLocations() {
        return edgeLocations;
    }

    public void setEdgeLocations(ArrayList<EdgeLocation> edgeLocations) {
        this.edgeLocations = edgeLocations;
    }


    public boolean getHasRobber() {
        return hasRobber;
    }

    public void setHasRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }

    public ResourceType getResource() {
        return resource;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

    public HexType getType() {
        return type;
    }

    public void setType(HexType type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        if(resource==null){
            return "{" + "\"location\" : " + location.toString() + ", \"resource\" : null, \"number\" : " + number + "}";
        }else{
            return "{" + "\"location\" : " + location.toString() + ", \"resource\" : \"" + resource.toString().toLowerCase() + "\", \"number\" : " + number + "}";
        
        }
    }

}
