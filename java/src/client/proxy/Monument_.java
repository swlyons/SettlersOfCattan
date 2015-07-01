package client.proxy;

/**
 * Contains information on playing a monument card
 * @author Aaron
 *
 */
public class Monument_ extends Request {
	private String type = "Monument";
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
