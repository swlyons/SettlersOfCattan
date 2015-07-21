package shared.model;

import shared.data.ResourceList;

/**
 * contains information on offering a trade
 *
 * @author Aaron
 *
 */
public class OfferTrade extends Command {

    private ResourceList offer;
    private int receiver;

    public ResourceList getOffer() {
        return offer;
    }

    public void setOffer(ResourceList offer) {
        this.offer = offer;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }
}
