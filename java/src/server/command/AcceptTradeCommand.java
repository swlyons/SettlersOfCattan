package server.command;

import server.receiver.AllOfOurInformation;
import shared.data.TradeOffer;
import shared.model.AcceptTrade;

public class AcceptTradeCommand implements Command{

	private AcceptTrade acceptTrade;

    public AcceptTradeCommand(AcceptTrade acceptTrade) {
        this.acceptTrade = acceptTrade;
    }

    @Override
    public boolean execute() {
        try {
        	TradeOffer trade = AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().getTradeOffer();
            AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).tradeAccepted(trade.getSender(), trade.getReceiver(), trade.getOffer());
            AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().getVersion()+1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
