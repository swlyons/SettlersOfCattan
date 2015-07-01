package client.data;

import client.shared.definitions.ResourceType;

public class TradeOffer {

    private int sender;
    private int recevier;
    private ResourceList offer;

    public TradeOffer(int sender, int recevier, ResourceType offer) {
        this.sender = sender;
        this.recevier = recevier;
        this.offer = offer;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getRecevier() {
        return recevier;
    }

    public void setRecevier(int recevier) {
        this.recevier = recevier;
    }

    public ResourceType getOffer() {
        return offer;
    }

    public void setOffer(ResourceType offer) {
        this.offer = offer;
    }
    
}
