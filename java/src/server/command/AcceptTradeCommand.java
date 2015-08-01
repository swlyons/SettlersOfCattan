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
        	if(acceptTrade.isWillAccept()){
                    TradeOffer trade = AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().getTradeOffer();
                    AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).tradeAccepted(trade.getSender(), trade.getReceiver(), trade.getOffer());
                    AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).logWithPlayerId("Player accepted the trade.", acceptTrade.getPlayerIndex());
                }
        	else {
                    AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().setTradeOffer(null);
                    AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).logWithPlayerId("This is Sparta!", acceptTrade.getPlayerIndex());
        	}
        	AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().getVersion()+1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
