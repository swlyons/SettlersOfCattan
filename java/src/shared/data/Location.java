package shared.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import shared.locations.VertexLocation;

public class Location implements Serializable {

    private int ownerID;
    private boolean canBeSettled;
    private boolean isCity;
    private Set<Integer> whoCanBuild;
    private VertexLocation normalizedLocation;

    public Location(VertexLocation vertexLocationStart) {
        ownerID = 4;
        canBeSettled = true;
        isCity = false;
        whoCanBuild = new HashSet<>();
        normalizedLocation = vertexLocationStart.getNormalizedLocation();
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
