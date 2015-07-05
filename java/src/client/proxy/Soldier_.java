package client.proxy;

import shared.locations.HexLocation;

/**
 * contains information on playing a soldier card
 * @author Aaron
 *
 */
public class Soldier_ extends Request {
	private String type = "Soldier";
	private int playerIndex;
	private int vicitmIndex;
	private HexLocation location;
	
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
	public int getVicitmIndex() {
		return vicitmIndex;
	}
	public void setVicitmIndex(int vicitmIndex) {
		this.vicitmIndex = vicitmIndex;
	}
	public HexLocation getLocation() {
		return location;
	}
	public void setLocation(HexLocation location) {
		this.location = location;
	}
}
