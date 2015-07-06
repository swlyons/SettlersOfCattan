package client.proxy;

import shared.locations.HexLocation;

/**
 * Contains the information for a rob request to be made.
 * @author Aaron
 *
 */
public class RobPlayer extends Request {
	private String type = "robPlayer";
	private int playerIndex;
	private int victimIndex;
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
	public int getVictimIndex() {
		return victimIndex;
	}
	public void setVictimIndex(int victimIndex) {
		this.victimIndex = victimIndex;
	}
	public HexLocation getLocation() {
		return location;
	}
	public void setLocation(HexLocation location) {
		this.location = location;
	}
}
