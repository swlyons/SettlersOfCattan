package client.proxy;

import shared.locations.VertexLocation;

/**
 * contains information about a settlement building action
 *
 * @author Aaron
 *
 */
public class BuildSettlement extends Command {

    private VertexLocation vertexLocation;
    private boolean free;

    public VertexLocation getVertexLocation() {
        return vertexLocation;
    }

    public void setVertexLocation(VertexLocation vertexLocation) {
        this.vertexLocation = vertexLocation;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
