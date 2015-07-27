package shared.model;

/**
 * Contains information for a buy dev card request
 *
 * @author Aaron
 *
 */
public class BuyDevCard extends Command {

    private Integer gameId;
    
    public BuyDevCard(int playerIndex) {
        super("buyDevCard", playerIndex);
    }
    
    public Integer getGameId(){
        return gameId;
    }
    
    public void setGameId(Integer gameId){
        this.gameId = gameId;
    }

}
