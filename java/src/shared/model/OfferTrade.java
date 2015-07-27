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
    private Integer receiver;
    private Integer gameId;
        
    public Integer getGameId(){
        return gameId;
    }
    
    public void setGameId(Integer gameId){
        this.gameId = gameId;
    }
    
    public ResourceList getOffer() {
        return offer;
    }

    public void setOffer(ResourceList offer) {
        this.offer = offer;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }
}
