/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.model.Soldier;

/**
 *
 * @author Samuel
 */
public class SoldierCommand implements Command {

    Soldier soldier;

    public SoldierCommand(Soldier soldier) {
        this.soldier = soldier;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).placeRobber(soldier.getLocation());
            AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).useSoldier();
            if (soldier.getVictimIndex() != -1) {
                AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getResourceManager().transferResourceCard(soldier.getVictimIndex(), soldier.getPlayerIndex(), AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame().getPlayers().get(soldier.getVictimIndex()).getResources().getRandomResourceAvailable());
            }
            AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame().getPlayers().get(soldier.getPlayerIndex()).setPlayedDevCard(true);
            AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame().getVersion()+1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
