/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.model.BuildCity;
import client.managers.GameManager;
import shared.definitions.PieceType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 *
 * @author Samuel
 */
public class BuildCityCommand implements Command {

    private BuildCity buildCity;

    public BuildCityCommand(BuildCity buildCity) {
        this.buildCity = buildCity;
    }

    @Override
    public boolean execute() {
        try {
            GameManager gm = AllOfOurInformation.getSingleton().getGames().get(buildCity.getGameId());
            HexLocation hexSpot = new HexLocation(buildCity.getVertexLocation().getX(), buildCity.getVertexLocation().getY());
            VertexDirection vertexDirection = buildCity.getVertexLocation().getDirection();
            VertexLocation vertexLocation = new VertexLocation(hexSpot, vertexDirection);
            if (gm.buildStructure(PieceType.CITY, vertexLocation)) {
                AllOfOurInformation.getSingleton().getGames().get(buildCity.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(buildCity.getGameId()).getGame().getVersion() + 1);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
