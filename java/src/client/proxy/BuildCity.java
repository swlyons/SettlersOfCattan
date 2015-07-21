package client.proxy;

import client.data.SettlementLocation;

/**
 * contains information about the action of building a city
 *
 * @author Aaron
 *
 */
public class BuildCity extends Command {

    private SettlementLocation vertexLocation;

    public BuildCity(int playerIndex) {
        super("buildCity", playerIndex);
    }

    public SettlementLocation getVertexLocation() {
        return vertexLocation;
    }

    public void setVertexLocation(SettlementLocation vertexLocation) {
        this.vertexLocation = vertexLocation;
    }

}
