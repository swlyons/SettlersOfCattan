package shared.model;

/**
 * Contains information for a finish turn request
 *
 * @author Aaron
 *
 */
public class FinishMove extends Command {

    public FinishMove(int playerIndex) {
        super("finishTurn", playerIndex);
    }

    private int gameId;
    
    public void setGameId(int gameId){
        this.gameId = gameId;
    }
    
    public int getGameId(){
        return gameId;
    }
}
