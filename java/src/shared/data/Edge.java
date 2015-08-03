package shared.data;

import java.util.HashSet;
import java.util.Set;

import shared.locations.EdgeLocation;

public class Edge {

    private int ownerId;
    private Set<Integer> whoCanBuild;
    private EdgeLocation edgeLocation;

    public Edge(EdgeLocation edgeLocationStart) {
        ownerId = 4;
        whoCanBuild = new HashSet<Integer>();
        edgeLocation = edgeLocationStart;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Integer> getWhoCanBuild() {
        return whoCanBuild;
    }

    public void setWhoCanBuild(Set<Integer> whoCanBuild) {
        this.whoCanBuild = whoCanBuild;
    }

    public EdgeLocation getEdgeLocation() {
        return edgeLocation;
    }

    public void setEdgeLocation(EdgeLocation edgeLocation) {
        this.edgeLocation = edgeLocation;
    }

    @Override
    public String toString() {
        return "Edge{" + "ownerId=" + ownerId + ", whoCanBuild=" + whoCanBuild + ", edgeLocation=" + edgeLocation + '}';
    }
    
}
