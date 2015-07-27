package shared.model;

import shared.locations.HexLocation;

/**
 * contains information on playing a soldier card
 *
 * @author Aaron
 *
 */
public class Soldier extends Command {

    private Integer victimIndex;
    private HexLocation location;
    private Integer gameId;
    
    public Soldier(int playerIndex) {
        super("Soldier", playerIndex);
    }

    public Integer getGameId(){
        return gameId;
    }
    
    public void setGameId(Integer gameId){
        this.gameId = gameId;
    }
    
    public Integer getVicitmIndex() {
        return victimIndex;
    }

    public void setVicitmIndex(Integer victimIndex) {
        this.victimIndex = victimIndex;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }
}
