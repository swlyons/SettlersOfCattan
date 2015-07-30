package server.command;

import server.receiver.AllOfOurInformation;
import shared.data.ResourceList;
import shared.data.TradeOffer;
import shared.model.OfferTrade;

public class OfferTradeCommand implements Command{

	private OfferTrade offerTrade;

    public OfferTradeCommand(OfferTrade offerTrade) {
        this.offerTrade = offerTrade;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(offerTrade.getGameId()).tradeOffer(offerTrade.getPlayerIndex(), offerTrade.getReceiver(), offerTrade);
            AllOfOurInformation.getSingleton().getGames().get(offerTrade.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(offerTrade.getGameId()).getGame().getVersion()+1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
