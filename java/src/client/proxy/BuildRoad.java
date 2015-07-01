package client.proxy;

import client.data.EdgeLocation;

/**
 * contains information about building a road
 * @author Aaron
 *
 */
public class BuildRoad extends Request {
	private String type = "buildRoad";
	private int playerIndex;
	private EdgeLocation roadLocation;
	private boolean free;
	
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
	public EdgeLocation getRoadLocation() {
		return roadLocation;
	}
	public void setRoadLocation(EdgeLocation roadLocation) {
		this.roadLocation = roadLocation;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
}
