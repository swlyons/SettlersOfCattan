package client.proxy;

import shared.locations.VertexLocation;

/**
 * contains information about the action of building a city
 *
 * @author Aaron
 *
 */
public class BuildCity extends Command {

    private VertexLocation vertexLocation;
    
    public BuildCity(int playerIndex){
        super("buildCity", playerIndex);
    }
    public VertexLocation getVertexLocation() {
        return vertexLocation;
    }

    public void setVertexLocation(VertexLocation vertexLocation) {
        this.vertexLocation = vertexLocation;
    }

}
