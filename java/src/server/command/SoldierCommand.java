/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import client.managers.GameManager;
import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.data.Bank;
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
                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId());
                Bank victimBank = gm.getResourceManager().getGameBanks().get(soldier.getVictimIndex());
                if (victimBank.getResourcesCards().getTotalResources() > 0) {
                    gm.getResourceManager().transferResourceCard(soldier.getVictimIndex(), soldier.getPlayerIndex(),
                            AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame().getPlayers().get(soldier.getVictimIndex()).getResources().getRandomResourceAvailable());
                    AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).logWithPlayerId("I was attacked!", soldier.getVictimIndex());
                } else {
                    AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).logWithPlayerId("Thief hid among the poor.", soldier.getVictimIndex());
                }
            }
            AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame().getPlayers().get(soldier.getPlayerIndex()).setPlayedDevCard(true);
            AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame().getVersion() + 1);
            AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).log("Player moved the theif.");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
