package client.proxy;

import client.data.ResourceList;

/**
 * contains information about discarding cards
 *
 * @author Aaron
 *
 */
public class DiscardCards extends Command {

    private ResourceList discardedCards;

    public ResourceList getDiscardedCards() {
        return discardedCards;
    }

    public void setDiscardedCards(ResourceList discardedCards) {
        this.discardedCards = discardedCards;
    }

}
