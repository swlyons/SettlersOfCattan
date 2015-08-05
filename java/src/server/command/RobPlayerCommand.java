/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.model.RobPlayer;
import client.managers.GameManager;
import java.io.Serializable;
import shared.data.Bank;
import shared.definitions.PieceType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 *
 * @author Samuel
 */
public class RobPlayerCommand implements Command, Serializable {

    RobPlayer robPlayer;

    public RobPlayerCommand(RobPlayer robPlayer) {
        this.robPlayer = robPlayer;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).placeRobber(robPlayer.getLocation());
            if (robPlayer.getVictimIndex() != -1) {
                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId());
                Bank victimBank = gm.getResourceManager().getGameBanks().get(robPlayer.getVictimIndex());
                if (victimBank.getResourcesCards().getTotalResources() > 0) {
                    gm.getResourceManager().transferResourceCard(robPlayer.getVictimIndex(), robPlayer.getPlayerIndex(),
                            AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).getGame().getPlayers().get(robPlayer.getVictimIndex()).getResources().getRandomResourceAvailable());
                    AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).logWithPlayerId("I was robbed!", robPlayer.getVictimIndex());
                } else {
                    AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).logWithPlayerId("Player was spared.", robPlayer.getVictimIndex());
                }
            }
            AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).getGame().getTurnTracker().setStatus("Playing");
            AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).getGame().getVersion() + 1);
            AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).log("Player moved the theif.");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
