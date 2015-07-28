/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.model.BuildRoad;
import client.managers.GameManager;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

/**
 *
 * @author Samuel
 */
public class BuildRoadCommand implements Command {

    private BuildRoad buildRoad;

    public BuildRoadCommand(BuildRoad buildRoad) {
        this.buildRoad = buildRoad;
    }

    @Override
    public boolean execute() {
        try {
            HexLocation hexSpot = new HexLocation(buildRoad.getRoadLocation().getX(), buildRoad.getRoadLocation().getY());
            EdgeDirection edgeDirection = buildRoad.getRoadLocation().getDirection();
            EdgeLocation edgeLocation = new EdgeLocation(hexSpot, edgeDirection);
            if (AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).getGame().getTurnTracker().getStatus().equals("FirstRound")
                    || AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).getGame().getTurnTracker().getStatus().equals("SecondRound")) {
                AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).placeFreeRoad(edgeLocation);
                AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).getGame().getVersion() + 1);

                return true;
            } else {

                if (AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).buildRoad(edgeLocation)) {
                    AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).getGame().getVersion() + 1);
                    return true;
                } else {
                    return false;
                }

            }
        } catch (Exception e) {
            return false;
        }
    }

}
