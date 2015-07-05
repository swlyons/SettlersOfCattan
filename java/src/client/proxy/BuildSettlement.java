package client.proxy;

import shared.locations.VertexLocation;

/**
 * contains information about a settlement building action
 * @author Aaron
 *
 */
public class BuildSettlement extends Request{
	private String type = "buildSettlement";
	private int playerIndex;
	private VertexLocation vertexLocation;
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
	public VertexLocation getVertexLocation() {
		return vertexLocation;
	}
	public void setVertexLocation(VertexLocation vertexLocation) {
		this.vertexLocation = vertexLocation;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
}
