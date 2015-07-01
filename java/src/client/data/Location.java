package client.data;

import java.util.ArrayList;

public class Location {

    private int ownerID;
    private ArrayList<Integer> whoCanBuild;
    private boolean canBeSettled;
    private boolean isCity;
    private VertexLocation normalizedLocation;

    public Location(VertexLocation vertexLocationStart) {
        whoCanBuild = new ArrayList<Integer>();        
        normalizedLocation = vertexLocationStart.getNormalizedLocation();
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
    
    public ArrayList<Integer> getWhoCanBuild() {
        return whoCanBuild;
    }

    public void setWhoCanBuild(ArrayList<Integer> whoCanBuild) {
        this.whoCanBuild = whoCanBuild;
    }

    public boolean isCanBeSettled() {
        return canBeSettled;
    }

    public void setCanBeSettled(boolean canBeSettled) {
        this.canBeSettled = canBeSettled;
    }

    public boolean getIsCity() {
        return isCity;
    }

    public void setIsCity(boolean isCity) {
        this.isCity = isCity;
    }

    public VertexLocation getNormalizedLocation() {
        return normalizedLocation;
    }

    public void setNormalizedLocation(VertexLocation normalizedLocation) {
        this.normalizedLocation = normalizedLocation;

    }
}
