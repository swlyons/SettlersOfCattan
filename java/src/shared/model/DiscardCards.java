package shared.model;

import shared.data.ResourceList;

/**
 * contains information about discarding cards
 *
 * @author Aaron
 *
 */
public class DiscardCards extends Command {

    private ResourceList discardedCards;
    private Integer gameId;

    public ResourceList getDiscardedCards() {
        return discardedCards;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public void setDiscardedCards(ResourceList discardedCards) {
        this.discardedCards = discardedCards;
    }

}
