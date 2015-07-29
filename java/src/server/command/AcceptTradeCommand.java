package server.command;

import server.receiver.AllOfOurInformation;
import shared.model.AcceptTrade;

public class AcceptTradeCommand implements Command{

	private AcceptTrade acceptTrade;

    public AcceptTradeCommand(AcceptTrade acceptTrade) {
        this.acceptTrade = acceptTrade;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).buyDevCard();
            AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(acceptTrade.getGameId()).getGame().getVersion()+1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
