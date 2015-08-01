/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.model.Road_Building;

/**
 *
 * @author Samuel
 */
public class RoadBuildingCommand implements Command {

    Road_Building roadBuilding;

    public RoadBuildingCommand(Road_Building roadBuilding) {
        this.roadBuilding = roadBuilding;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(roadBuilding.getGameId()).useRoadBuilding(roadBuilding.getSpot1(),roadBuilding.getSpot2());
            AllOfOurInformation.getSingleton().getGames().get(roadBuilding.getGameId()).getGame().getPlayers().get(roadBuilding.getPlayerIndex()).setPlayedDevCard(true);
            AllOfOurInformation.getSingleton().getGames().get(roadBuilding.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(roadBuilding.getGameId()).getGame().getVersion()+1);
            AllOfOurInformation.getSingleton().getGames().get(roadBuilding.getGameId()).log("Player found an underground channel");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
