package client.data;

import java.util.ArrayList;

public class Hex {
    
    private HexLocation hexLocation;
    private ArrayList<EdgeLocation> edgeLocations;
    private int rollValue;
    private String resourceType;
    private boolean hasRobber;
    
    public Hex(HexLocation hexStart, int rollValueStart, String resourceTypeStart){
        hexLocation=hexStart;
        rollValue=rollValueStart;
        resourceType=resourceTypeStart;
        hasRobber=false;
    }
    
        public HexLocation getHexLocation() {
        return hexLocation;
    }

    public void setHexLocation(HexLocation hexLocation) {
        this.hexLocation = hexLocation;
    }

    public ArrayList<EdgeLocation> getEdgeLocations() {
        return edgeLocations;
    }

    public void setEdgeLocations(ArrayList<EdgeLocation> edgeLocations) {
        this.edgeLocations = edgeLocations;
    }

    public int getRollValue() {
        return rollValue;
    }

    public void setRollValue(int rollValue) {
        this.rollValue = rollValue;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public boolean getHasRobber() {
        return hasRobber;
    }

    public void setHasRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    
}
