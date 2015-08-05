package shared.model;

/**
 * Contains information on playing a monument card
 *
 * @author Aaron
 *
 */
public class Monument extends Command {

    private Integer gameId;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

}
