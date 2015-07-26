package shared.model;

/**
 * represents a reqeust to force a particular roll.
 *
 * @author Aaron
 *
 */
public class RollNumber extends Command {

    private int number;
    private int gameId;
    
    
    public RollNumber() {
        gameId = -1;
    }

    public RollNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public void setGameId(int gameId){
        this.gameId = gameId;
    }
    
    public int getGameId(){
        return gameId;
    }
    
}
