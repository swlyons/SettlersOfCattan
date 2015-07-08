package client.data;

public class TradeOffer {

    private int sender;
    private int recevier;
    private ResourceList offer;

    public TradeOffer(int sender, int recevier, ResourceList offer) {
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

    public ResourceList getOffer() {
        return offer;
    }

    public void setOffer(ResourceList offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        return "{" + "sender=" + sender + ", recevier=" + recevier + ", offer=" + offer + '}';
    }
    
}
