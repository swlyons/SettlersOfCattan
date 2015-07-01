package client.proxy;

import client.data.ResourceList;

/**
 * contains information on offering a trade
 * @author Aaron
 *
 */
public class OfferTrade extends Request{
	private String type = "offerTrade";
	private int playerIndex;
	private ResourceList offer;
	private int reciever;
	
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
	public ResourceList getOffer() {
		return offer;
	}
	public void setOffer(ResourceList offer) {
		this.offer = offer;
	}
	public int getReciever() {
		return reciever;
	}
	public void setReciever(int reciever) {
		this.reciever = reciever;
	}
}
