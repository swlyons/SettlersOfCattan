package client.proxy;

import client.data.ResourceList;

/**
 * contains information on offering a trade
 *
 * @author Aaron
 *
 */
public class OfferTrade extends Command {

    private ResourceList offer;
    private int reciever;

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
