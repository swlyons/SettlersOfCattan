/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.model.DiscardCards;

/**
 *
 * @author Samuel
 */
public class DiscardCardsCommand implements Command {

    private DiscardCards discardCards;

    public DiscardCardsCommand(DiscardCards discardCards) {
        this.discardCards = discardCards;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame().getPlayers().get(discardCards.getPlayerIndex()).setDiscarded(true);
            AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getResourceManager().transferResourceCard(discardCards.getPlayerIndex(), 4, discardCards.getDiscardedCards());
            AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).saveResourcesIntoGame();
            AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).logWithPlayerId("Player has surrendered resources.", discardCards.getPlayerIndex());
            boolean everyoneDiscarded = true;
            for (int i = 0; i < 4; i++) {
                if (!(AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame().getPlayers().get(i).isDiscarded() ||
                        AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame().getPlayers().get(i).getResources().getTotalResources() < 8)) {
                    if(AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame().getPlayers().get(i).getId()<-1){
                        continue;
                    }
                    everyoneDiscarded = false;
                }
            }

            if (everyoneDiscarded) {
                for (int i = 0; i < 4; i++) {
                    AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame().getPlayers().get(i).setDiscarded(false);
                }
                AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).log("Players have finished surrendering.");
                AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame().getTurnTracker().setStatus("Robbing");

            }

            AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame().getVersion() + 1);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
