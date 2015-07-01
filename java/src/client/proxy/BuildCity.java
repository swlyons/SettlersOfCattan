package client.proxy;

import client.data.VertexLocation;

/**
 * contains information about the action of building a city
 * @author Aaron
 *
 */
public class BuildCity extends Request {
	private String type = "buildCity";
	private int playerIndex;
	private VertexLocation vertexLocation;
	
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
}
