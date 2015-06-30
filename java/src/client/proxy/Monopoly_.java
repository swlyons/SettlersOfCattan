package client.proxy;

import shared.definitions.ResourceType;

/**
 * Contains information about playing a monopoly card
 * @author Aaron
 *
 */
public class Monopoly_ extends Request {
	private String type = "Monopoly";
	private ResourceType resource;
	private int playerIndex;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ResourceType getResource() {
		return resource;
	}
	public void setResource(ResourceType resource) {
		this.resource = resource;
	}
	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}
}
