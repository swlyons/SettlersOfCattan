package client.proxy;

/**
 * Contains information for a finish turn request
 * @author Aaron
 *
 */
public class FinishMove extends Request{
	private String type = "finishTurn";
	private int playerIndex;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}
}
