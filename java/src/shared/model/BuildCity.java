package shared.model;

import java.io.Serializable;
import shared.data.SettlementLocation;

/**
 * contains information about the action of building a city
 *
 * @author Aaron
 *
 */
public class BuildCity extends Command implements Serializable {

    private SettlementLocation vertexLocation;
    Integer gameId;

    public BuildCity(int playerIndex) {
        super("buildCity", playerIndex);
    }

    public SettlementLocation getVertexLocation() {
        return vertexLocation;
    }

    public void setVertexLocation(SettlementLocation vertexLocation) {
        this.vertexLocation = vertexLocation;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

}
