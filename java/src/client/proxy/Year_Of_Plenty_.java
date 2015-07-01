package client.proxy;

import shared.definitions.ResourceType;

/**
 * Contains the information for playing a year of plenty
 * @author Aaron
 *
 */
public class Year_Of_Plenty_ extends Request {
	private String type = "Year_of_Plenty";
	private int playerIndex;
	private ResourceType resource1;
	private ResourceType resource2;
	
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
	public ResourceType getResource1() {
		return resource1;
	}
	public void setResource1(ResourceType resource1) {
		this.resource1 = resource1;
	}
	public ResourceType getResource2() {
		return resource2;
	}
	public void setResource2(ResourceType resource2) {
		this.resource2 = resource2;
	}
}
