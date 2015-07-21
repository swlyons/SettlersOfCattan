package shared.data;

public class TradeOffer {

    private int sender;
    private int receiver;
    private ResourceList offer;

    public TradeOffer(int sender, int receiver, ResourceList offer) {
        this.sender = sender;
        this.receiver = receiver;
        this.offer = offer;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void getReceiver(int receiver) {
        this.receiver = receiver;
    }

    public ResourceList getOffer() {
        return offer;
    }

    public void setOffer(ResourceList offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        return "{" + "sender=" + sender + ", receiver=" + receiver + ", offer=" + offer + '}';
    }

}
