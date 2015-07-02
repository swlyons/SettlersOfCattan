package client.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Location {

    private int ownerID;
    private Set<Integer> whoCanBuild;
    private boolean canBeSettled;
    private boolean isCity;
    private VertexLocation normalizedLocation;

    public Location(VertexLocation vertexLocationStart) {
        whoCanBuild = new HashSet<Integer>();        
        normalizedLocation = vertexLocationStart.getNormalizedLocation();
        canBeSettled = true;
        isCity = false;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
    
    public Set<Integer> getWhoCanBuild() {
        return whoCanBuild;
    }

    public void setWhoCanBuild(Set<Integer> whoCanBuild) {
        this.whoCanBuild = whoCanBuild;
    }

    public boolean getCanBeSettled() {
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
