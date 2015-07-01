package client.data;
import java.util.ArrayList;

public class Edge {
 
    private int ownerId;
    private ArrayList<Integer> whoCanBuild;
    private EdgeLocation edgeLocation;
            
    public Edge(EdgeLocation edgeLocationStart){
       ownerId=4;
       whoCanBuild = new ArrayList<Integer>();
       edgeLocation = edgeLocationStart;
    }
    
    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<Integer> getWhoCanBuild() {
        return whoCanBuild;
    }

    public void setWhoCanBuild(ArrayList<Integer> whoCanBuild) {
        this.whoCanBuild = whoCanBuild;
    }

    public EdgeLocation getEdgeLocation() {
        return edgeLocation;
    }

    public void setEdgeLocation(EdgeLocation edgeLocation) {
        this.edgeLocation = edgeLocation;
    }
    
}
