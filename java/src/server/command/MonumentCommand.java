/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.receiver.AllOfOurInformation;
import shared.model.Monument;

/**
 *
 * @author Samuel
 */
public class MonumentCommand implements Command {

    Monument monument;

    public MonumentCommand(Monument monument) {
        this.monument = monument;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(monument.getGameId()).useMonument();
            AllOfOurInformation.getSingleton().getGames().get(monument.getGameId()).getGame().getPlayers().get(monument.getPlayerIndex()).setPlayedDevCard(true);
            AllOfOurInformation.getSingleton().getGames().get(monument.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(monument.getGameId()).getGame().getVersion()+1);
            AllOfOurInformation.getSingleton().getGames().get(monument.getGameId()).log("Player can taste victory");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
