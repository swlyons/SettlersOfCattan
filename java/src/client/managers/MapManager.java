package client.managers;

import client.data.*;
import java.util.ArrayList;

public class MapManager {

    private ArrayList<Hex> hexList;

    public MapManager(){
        hexList = new ArrayList<Hex>();
    }
    
    /**
     * @pre The current player clicked the button at the beginning of their turn
     * @post Has the gameManager roll the dice, then either calls the moveRobber method on a 7 or calls awardTerrainResource on each Hex that has that roll value
     */
    public void findRollValues(){
        
    }
    
    /**
     * @param triggeredHex = the hex to reward the possible settlements and/or cities 
     * @pre The triggeredHex's rollValue had just been rolled 
     * @post Cycles through all neighboring vertexLocations, seeing if any are settled, and award the ownerID of those that are with the appropriate resources
     */
    public void awardTerrainResource(Hex triggeredHex){
    
    }
    
    /**
     * @pre A 7 was rolled
     * @post The each player discarded half of their cards if they had 7 or more, the current player robbed 1 player that settled on the hex (if any)
     * 
     */
    public void moveRobber(){
        
    }
        
    public ArrayList<Hex> getHexList(){
        return hexList;
    }
    public void setHexList(ArrayList<Hex> hexListNew){
        hexList = hexListNew;
    }

    //generateMap(int mapRadius, ArrayList<Hex> Hexes, ArrayList<Port>, ArrayList<Road> Roads, ArrayList<VertexObject> Buildings)

}
