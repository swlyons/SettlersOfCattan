package server.command;

import server.receiver.AllOfOurInformation;
import shared.data.ResourceList;
import shared.model.MaritimeTrade;

public class MaritimeTradeCommand implements Command {

	private MaritimeTrade maritimeTrade;

    public MaritimeTradeCommand(MaritimeTrade maritimeTrade) {
        this.maritimeTrade = maritimeTrade;
    }

    @Override
    public boolean execute() {
        try {
        	ResourceList input = new ResourceList();
        	ResourceList output = new ResourceList();
       		input.add(maritimeTrade.getInputResource(), maritimeTrade.getRatio());
       		output.add(maritimeTrade.getOutputResource(), 1);
            AllOfOurInformation.getSingleton().getGames().get(maritimeTrade.getGameId()).tradeAccepted(maritimeTrade.getPlayerIndex(), 4, input, output);
            AllOfOurInformation.getSingleton().getGames().get(maritimeTrade.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(maritimeTrade.getGameId()).getGame().getVersion()+1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
