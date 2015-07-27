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
import shared.definitions.PieceType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 *
 * @author Samuel
 */
public class RobPlayerCommand implements Command {

    RobPlayer robPlayer;

    public RobPlayerCommand(RobPlayer robPlayer) {
        this.robPlayer = robPlayer;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).placeRobber(robPlayer.getLocation());
            if (robPlayer.getVictimIndex() != -1) {
                AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).getResourceManager().transferResourceCard(robPlayer.getVictimIndex(), robPlayer.getPlayerIndex(), AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).getGame().getPlayers().get(robPlayer.getVictimIndex()).getResources().getRandomResourceAvailable());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
