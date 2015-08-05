package server.command;

import java.io.Serializable;
import server.receiver.AllOfOurInformation;
import shared.model.Year_Of_Plenty;

public class YearOfPlentyCommand implements Command, Serializable {

    Year_Of_Plenty yearOfPlenty;

    public YearOfPlentyCommand(Year_Of_Plenty yearOfPlenty) {
        this.yearOfPlenty = yearOfPlenty;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(yearOfPlenty.getGameId()).useYearOfPlenty(yearOfPlenty.getResource1(), yearOfPlenty.getResource2());
            AllOfOurInformation.getSingleton().getGames().get(yearOfPlenty.getGameId()).getGame().getPlayers().get(yearOfPlenty.getPlayerIndex()).setPlayedDevCard(true);
            AllOfOurInformation.getSingleton().getGames().get(yearOfPlenty.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(yearOfPlenty.getGameId()).getGame().getVersion() + 1);
            AllOfOurInformation.getSingleton().getGames().get(yearOfPlenty.getGameId()).log("Player cast a spell for some free resources.");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
