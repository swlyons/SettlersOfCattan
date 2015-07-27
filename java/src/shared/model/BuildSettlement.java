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
    private Boolean free;

    public SettlementLocation getVertexLocation() {
        return vertexLocation;
    }

    public void setVertexLocation(SettlementLocation vertexLocation) {
        this.vertexLocation = vertexLocation;
    }

    public Boolean isFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }
}
