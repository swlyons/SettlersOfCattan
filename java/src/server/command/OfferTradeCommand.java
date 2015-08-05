package server.command;

import java.io.Serializable;
import server.receiver.AllOfOurInformation;
import shared.model.OfferTrade;

public class OfferTradeCommand implements Command, Serializable {

    private OfferTrade offerTrade;

    public OfferTradeCommand(OfferTrade offerTrade) {
        this.offerTrade = offerTrade;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(offerTrade.getGameId()).tradeOffer(offerTrade.getPlayerIndex(), offerTrade.getReceiver(), offerTrade);
            AllOfOurInformation.getSingleton().getGames().get(offerTrade.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(offerTrade.getGameId()).getGame().getVersion() + 1);
            AllOfOurInformation.getSingleton().getGames().get(offerTrade.getGameId()).log("Player sent an envoy.");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
