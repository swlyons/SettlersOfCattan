package client.proxy;

/**
 * Contains information for a buy dev card request
 * @author Aaron
 *
 */
public class BuyDevCard extends Request{
	private String type = "buyDevCard";
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
