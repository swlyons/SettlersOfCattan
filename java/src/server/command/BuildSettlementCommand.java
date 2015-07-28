/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.receiver.AllOfOurInformation;
import shared.model.BuildSettlement;
import shared.definitions.PieceType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 *
 * @author Samuel
 */
public class BuildSettlementCommand implements Command {

    private BuildSettlement buildSettlement;

    public BuildSettlementCommand(BuildSettlement buildSettlement) {
        this.buildSettlement = buildSettlement;
    }

    @Override
    public boolean execute() {
        try {
            HexLocation hexSpot = new HexLocation(buildSettlement.getVertexLocation().getX(), buildSettlement.getVertexLocation().getY());
            VertexDirection vertexDirection = buildSettlement.getVertexLocation().getDirection();
            VertexLocation vertexLocation = new VertexLocation(hexSpot, vertexDirection);
            if (AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame().getTurnTracker().getStatus().equals("FirstRound")) {
                AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).placeFirstSettlement(vertexLocation);
                AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame().getVersion() + 1);
                return true;
            } else {
                if (AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame().getTurnTracker().getStatus().equals("SecondRound")) {
                    AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).placeSecondSettlement(vertexLocation);
                    AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame().getVersion() + 1);
                    return true;
                } else {
                    if (AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).buildStructure(PieceType.SETTLEMENT, vertexLocation)) {
                        AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame().getVersion() + 1);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

}
