package shared.model;

import shared.data.SettlementLocation;

/**
 * contains information about a settlement building action
 *
 * @author Aaron
 *
 */
public class BuildSettlement extends Command {

    private SettlementLocation vertexLocation;
    private boolean free;

    public SettlementLocation getVertexLocation() {
        return vertexLocation;
    }

    public void setVertexLocation(SettlementLocation vertexLocation) {
        this.vertexLocation = vertexLocation;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
