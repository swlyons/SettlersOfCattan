package server.command;

import server.receiver.AllOfOurInformation;
import shared.model.Monopoly;

public class MonopolyCommand implements Command {
    
	Monopoly monopoly;

    public MonopolyCommand(Monopoly monopoly) {
        this.monopoly = monopoly;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(monopoly.getGameId()).useMonopoly(monopoly.getResource());
            AllOfOurInformation.getSingleton().getGames().get(monopoly.getGameId()).getGame().getPlayers().get(monopoly.getPlayerIndex()).setPlayedDevCard(true);
            AllOfOurInformation.getSingleton().getGames().get(monopoly.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(monopoly.getGameId()).getGame().getVersion()+1);
            AllOfOurInformation.getSingleton().getGames().get(monopoly.getGameId()).log("They are all Player's now!");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
