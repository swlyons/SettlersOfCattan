package shared.model;

import java.io.Serializable;

/**
 * Contains information for a finish turn request
 *
 * @author Aaron
 *
 */
public class FinishMove extends Command implements Serializable {

    public FinishMove(int playerIndex) {
        super("finishTurn", playerIndex);
    }

    private Integer gameId;

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return gameId;
    }
}
