package shared.model;

import shared.definitions.ResourceType;

/**
 * Contains information about playing a monopoly card
 *
 * @author Aaron
 *
 */
public class Monopoly extends Command {

    private ResourceType resource;
    private int gameId;

    public Monopoly(int playerIndex) {
        super("Monopoly", playerIndex);
    }

    public ResourceType getResource() {
        return resource;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }
    
    public int getGameId() {
    	return gameId;
    }

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

}
