package client.data;

import java.util.ArrayList;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;


public class Hex {
    
    private HexLocation location;
    private ArrayList<EdgeLocation> edgeLocations;
    private int rollValue;
    private String resource;
    private boolean hasRobber;
    private int number;
    
    public Hex(HexLocation hexStart, int rollValueStart, String resourceTypeStart){
        location=hexStart;
        rollValue=rollValueStart;
        resource=resourceTypeStart;
        hasRobber=false;
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "{" + "location : " + location + ", resource : " + resource + ", number : " + number + '}';
    }
    
    
}
