package client.proxy;

import shared.definitions.ResourceType;

/**
 * contains information on making a maritime trade
 * @author Aaron
 *
 */
public class MaritimeTrade extends Request {
	private String type = "maritimeTrade";
	private int playerIndex;
	private ResourceType inputResource;
	private ResourceType outputResource;
	
	
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
	public ResourceType getInputResource() {
		return inputResource;
	}
	public void setInputResource(ResourceType inputResource) {
		this.inputResource = inputResource;
	}
	public ResourceType getOutputResource() {
		return outputResource;
	}
	public void setOutputResource(ResourceType outputResource) {
		this.outputResource = outputResource;
	}
}
