package shared.model;

import java.io.Serializable;

/**
 * contains information on the action of accepting/rejecting a trade
 *
 * @author Aaron
 *
 */
public class AcceptTrade extends Command implements Serializable {

    private Boolean willAccept;
    private Integer gameId;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Boolean isWillAccept() {
        return willAccept;
    }

    public void setWillAccept(Boolean willAccept) {
        this.willAccept = willAccept;
    }
}
