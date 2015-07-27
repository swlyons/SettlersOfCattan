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
public class BuildRoadCommand implements Command{
    
    private BuildRoad buildRoad;
    
    public BuildRoadCommand(BuildRoad buildRoad){
        this.buildRoad=buildRoad;
    }
    
    @Override
    public boolean execute(){
        try{
            GameManager gm = AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId());
            HexLocation hexSpot = new HexLocation(buildRoad.getRoadLocation().getX(), buildRoad.getRoadLocation().getY());
            EdgeDirection edgeDirection = buildRoad.getRoadLocation().getDirection();
            EdgeLocation edgeLocation = new EdgeLocation(hexSpot,edgeDirection);
            gm.buildRoad(edgeLocation);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
}
