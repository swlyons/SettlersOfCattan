package client.managers;

import client.data.Hex;
import java.util.List;
import java.util.ArrayList;
import shared.locations.HexLocation;

public class MapManager {

    private List<Hex> hexList;

    public MapManager() {
        hexList = new ArrayList<>();
    }

    /**
     * @param rollValue = The value rolled
     * @pre The triggeredHex's rollValue had just been rolled
     * @post Cycles through all neighboring vertexLocations, seeing if any are
     * settled, and award the ownerID of those that are with the appropriate
     * resources
     */
    public List<Hex> getTerrainResourceHexes(int rollValue) {
        List<Hex> hexesProducingResources = new ArrayList<>();

        for (int i = 0; i < hexList.size(); i++) {
            if (hexList.get(i).getRollValue() == i && !hexList.get(i).getHasRobber()) {
                hexesProducingResources.add(hexList.get(i));
            }
        }

        return hexesProducingResources;
    }

    /**
     * @pre A 7 was rolled
     * @post The each player discarded half of their cards if they had 7 or
     * more, the current player robbed 1 player that settled on the hex (if any)
     * returns the success of moving the Robber. If true the robber was moved
     * successfully.
     */
    public boolean moveRobber(HexLocation newLocationForRobber) {

        for (Hex hex : hexList) {
            if (hex.getHasRobber() && hex.getLocation().equals(newLocationForRobber)) {
                return false;
            }
        }

        return true;
    }

    public HexLocation getRobberLocation() {
        for (Hex hex : hexList) {
            if (hex.getHasRobber()) {
                return hex.getLocation();
            }
        }
        return null;
    }

    public List<Hex> getHexList() {
        return hexList;
    }

    public void setHexList(List<Hex> hexListNew) {
        hexList = hexListNew;
    }

    //generateMap(int mapRadius, ArrayList<Hex> Hexes, ArrayList<Port>, ArrayList<Road> Roads, ArrayList<VertexObject> Buildings)
}
