package shared.model;

/**
 * represents a reqeust to force a particular roll.
 *
 * @author Aaron
 *
 */
public class RollNumber extends Command {

    private Integer number;
    private Integer gameId;
    
    
    public RollNumber() {
        gameId = -1;
    }

    public RollNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
    
    public void setGameId(Integer gameId){
        this.gameId = gameId;
    }
    
    public Integer getGameId(){
        return gameId;
    }
    
}
