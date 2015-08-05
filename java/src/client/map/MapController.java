package client.map;

import shared.data.Edge;
import shared.data.GameInfo;
import shared.data.SettlementLocation;
import shared.data.Hex;
import shared.data.RobPlayerInfo;
import shared.data.XYEdgeLocation;
import shared.data.Port;
import shared.data.Location;
import java.util.*;

import shared.definitions.*;
import shared.locations.*;
import client.base.*;
import client.managers.GameManager;
import shared.model.BuildCity;
import shared.model.BuildRoad;
import shared.model.Road_Building;
import shared.model.BuildSettlement;
import shared.model.RobPlayer;
import shared.model.Soldier;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {

    private IRobView robView;
    private boolean isFree = false;
    private boolean endTurn = false;
    private boolean playedRoadBuilding = false;
    private boolean placedFirstRoadBuildingRoad = false;
    private boolean playedSoldier = false;
    private boolean doneOnce = false;
    private EdgeLocation firstRoad;
    private HexLocation newRobberLocation = null;
    private HexLocation newRobberLocation2 = null;
    private MapPoller mapPoller = new MapPoller();
    private ArrayList<Location> settledCities;
    private ArrayList<Location> settledLocations;
    private ArrayList<Edge> settledEdges;

    public MapController(IMapView view, IRobView robView) {

        super(view);

        setRobView(robView);

        settledCities = new ArrayList<Location>();
        settledLocations = new ArrayList<Location>();
        settledEdges = new ArrayList<Edge>();
    }

    public IMapView getView() {

        return (IMapView) super.getView();
    }

    public IRobView getRobView() {
        return robView;
    }

    public void setRobView(IRobView robView) {
        this.robView = robView;
    }

    public MapPoller getMapPoller() {
        return mapPoller;
    }

    public void setMapPoller(MapPoller mapPoller) {
        this.mapPoller = mapPoller;
    }

    @Override
    public void initFromModel() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        if (!doneOnce) {
            //should only happen once
            for (int i = 0; i < gm.getMapManager().getHexList().size(); i++) {
                Hex h = gm.getMapManager().getHexList().get(i);
                if (h.getResource() == ResourceType.wood) {
                    getView().addHex(h.getLocation(), HexType.wood);
                } else if (h.getResource() == ResourceType.brick) {
                    getView().addHex(h.getLocation(), HexType.brick);
                } else if (h.getResource() == ResourceType.sheep) {
                    getView().addHex(h.getLocation(), HexType.sheep);
                } else if (h.getResource() == ResourceType.ore) {
                    getView().addHex(h.getLocation(), HexType.ore);
                } else if (h.getResource() == ResourceType.wheat) {
                    getView().addHex(h.getLocation(), HexType.wheat);
                } else {
                    getView().addHex(h.getLocation(), HexType.desert);
                }

                if ((h.getNumber() >= 2 && h.getNumber() <= 6) || (h.getNumber() >= 8 && h.getNumber() <= 12)) {
                    getView().addNumber(h.getLocation(), h.getNumber());
                }
            }
        }

        for (Edge e : gm.getLocationManager().getSettledEdges()) {
            if (!settledEdges.contains(e)) {
                settledEdges.add(e);
                getView().placeRoad(e.getEdgeLocation(),
                        CatanColor.valueOf(gm.getGame().getPlayers().get(e.getOwnerId()).getColor().toUpperCase()));
            }
        }
        for (Location l : gm.getLocationManager().getSettledLocations()) {
            if (l.getIsCity()) {
                if (!settledCities.contains(l)) {
                    settledCities.add(l);
                    getView().placeCity(l.getNormalizedLocation(),
                            CatanColor.valueOf(gm.getGame().getPlayers().get(l.getOwnerID()).getColor().toUpperCase()));
                }
            } else {
                if (!settledLocations.contains(l)) {
                    settledLocations.add(l);
                    getView().placeSettlement(l.getNormalizedLocation(),
                            CatanColor.valueOf(gm.getGame().getPlayers().get(l.getOwnerID()).getColor().toUpperCase()));
                }
            }
        }

        if ((newRobberLocation2 != null && !newRobberLocation2.equals(gm.getGame().getMap().getRobber())) || !doneOnce) {
            getView().placeRobber(gm.getGame().getMap().getRobber());
            newRobberLocation2 = gm.getGame().getMap().getRobber();
        }
        //update the robber location
        gm.getGame().getMap().setRobber(newRobberLocation2);

        if (!doneOnce) {
            // Water tiles are hard coded, sine they never change, ever.
            getView().addHex(new HexLocation(0, 3), HexType.water);
            getView().addHex(new HexLocation(1, 2), HexType.water);
            getView().addHex(new HexLocation(2, 1), HexType.water);
            getView().addHex(new HexLocation(3, 0), HexType.water);
            getView().addHex(new HexLocation(3, -1), HexType.water);
            getView().addHex(new HexLocation(3, -2), HexType.water);
            getView().addHex(new HexLocation(3, -3), HexType.water);
            getView().addHex(new HexLocation(2, -3), HexType.water);
            getView().addHex(new HexLocation(1, -3), HexType.water);
            getView().addHex(new HexLocation(0, -3), HexType.water);
            getView().addHex(new HexLocation(-1, -2), HexType.water);
            getView().addHex(new HexLocation(-2, -1), HexType.water);
            getView().addHex(new HexLocation(-3, 0), HexType.water);
            getView().addHex(new HexLocation(-3, 1), HexType.water);
            getView().addHex(new HexLocation(-3, 2), HexType.water);
            getView().addHex(new HexLocation(-3, 3), HexType.water);
            getView().addHex(new HexLocation(-2, 3), HexType.water);
            getView().addHex(new HexLocation(-1, 3), HexType.water);

            for (Port p : gm.getGame().getMap().getPorts()) {
                HexLocation location = p.getLocation();
                PortType type = PortType.THREE;
                if (p.getResource() != null) {
                    type = PortType.valueOf(p.getResource().toString().toUpperCase());
                }

                // Fancy logic determines edge direction by location
                EdgeDirection direction = EdgeDirection.N;
                if (location.getX() > 1) {
                    if (location.getY() > -2) {
                        direction = EdgeDirection.NW;
                    } else {
                        direction = EdgeDirection.SW;
                    }
                } else if (location.getX() < -1) {
                    if (location.getY() > 1) {
                        direction = EdgeDirection.NE;
                    } else {
                        direction = EdgeDirection.SE;
                    }
                } else if (location.getY() < 0) {
                    direction = EdgeDirection.S;
                }

                getView().addPort(new EdgeLocation(location, direction), type);
            }
        }
        doneOnce = true;
    }

    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        return gm.canPlaceRoad(edgeLoc);
    }

    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        return gm.canPlaceSettlement(vertLoc);
    }

    public boolean canPlaceCity(VertexLocation vertLoc) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        return gm.canPlaceCity(vertLoc);
    }

    public boolean canPlaceRobber(HexLocation hexLoc) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        return gm.canPlaceRobber(hexLoc);
    }

    public void placeRoad(EdgeLocation edgeLoc) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
        if (isFree || gm.getGame().getTurnTracker().getStatus().equals("FirstRound") || gm.getGame().getTurnTracker().getStatus().equals("SecondRound")) {
            isFree = false;
            if (gm.placeFreeRoad(edgeLoc)) {
                CatanColor color = CatanColor
                        .valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
                getView().placeRoad(edgeLoc, color);
                XYEdgeLocation edgeSpot = new XYEdgeLocation();
                edgeSpot.setDirection(edgeLoc.getDir());
                edgeSpot.setX(edgeLoc.getHexLoc().getX());
                edgeSpot.setY(edgeLoc.getHexLoc().getY());
                if (!playedRoadBuilding) {
                    BuildRoad br = new BuildRoad();
                    br.setFree(true);
                    br.setPlayerIndex(currentPlayer);
                    br.setRoadLocation(edgeSpot);
                    br.setType("buildRoad");
                    try {
                        ClientFascade.getSingleton().buildRoad(br);
                        if (gm.getGame().getTurnTracker().getStatus().equals("FirstRound") || gm.getGame().getTurnTracker().getStatus().equals("SecondRound")) {
                            setEndTurn(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (playedRoadBuilding) {
                if (!placedFirstRoadBuildingRoad) {
                    firstRoad = edgeLoc;
                    placedFirstRoadBuildingRoad = true;
                    startMove(PieceType.ROAD, true, false);
                } else {
                    Road_Building roadBuilding = new Road_Building(currentPlayer);
                    roadBuilding.setSpot1(firstRoad);
                    roadBuilding.setSpot2(edgeLoc);
                    placedFirstRoadBuildingRoad = false;
                    firstRoad = null;
                    try {
                        ClientFascade.getSingleton().roadBuilding(roadBuilding);
                    } catch (Exception e) {
                    }

                }
            }
        } else {
            if (gm.buildRoad(edgeLoc)) {
                CatanColor color = CatanColor
                        .valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
                getView().placeRoad(edgeLoc, color);
                BuildRoad br = new BuildRoad();
                br.setFree(false);
                br.setPlayerIndex(currentPlayer);
                XYEdgeLocation edgeSpot = new XYEdgeLocation();
                edgeSpot.setDirection(edgeLoc.getDir());
                edgeSpot.setX(edgeLoc.getHexLoc().getX());
                edgeSpot.setY(edgeLoc.getHexLoc().getY());
                br.setRoadLocation(edgeSpot);
                br.setType("buildRoad");
                try {
                    ClientFascade.getSingleton().buildRoad(br);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void placeSettlement(VertexLocation vertLoc) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        SettlementLocation settle = new SettlementLocation();
        settle.setDirection(vertLoc.getDir());
        settle.setX(vertLoc.getHexLoc().getX());
        settle.setY(vertLoc.getHexLoc().getY());
        Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
        BuildSettlement build = new BuildSettlement();

        if (isFree || gm.getLocationManager().getSettledLocations().size() < 8) {
            isFree = false;
            if (gm.getLocationManager().getSettledLocations().size() < 4) {
                gm.placeFirstSettlement(vertLoc);
            } else {
                gm.placeSecondSettlement(vertLoc);
            }
            CatanColor color = CatanColor
                    .valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());

            build.setFree(true);
            build.setType("buildSettlement");
            build.setPlayerIndex(currentPlayer);
            build.setVertexLocation(settle);
            try {
                ClientFascade.getSingleton().buildSettlement(build);
                getView().placeSettlement(vertLoc, color);
                startMove(PieceType.ROAD, true, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (gm.buildStructure(PieceType.SETTLEMENT, vertLoc)) {
                CatanColor color = CatanColor
                        .valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
                build.setType("buildSettlement");
                build.setFree(false);
                build.setPlayerIndex(currentPlayer);
                build.setVertexLocation(settle);
                try {
                    ClientFascade.getSingleton().buildSettlement(build);
                    getView().placeSettlement(vertLoc, color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void placeCity(VertexLocation vertLoc) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
        BuildCity build = new BuildCity(currentPlayer);
        SettlementLocation settle = new SettlementLocation();
        settle.setDirection(vertLoc.getDir());
        settle.setX(vertLoc.getHexLoc().getX());
        settle.setY(vertLoc.getHexLoc().getY());

        if (gm.buildStructure(PieceType.CITY, vertLoc)) {
            CatanColor color = CatanColor
                    .valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
            getView().placeCity(vertLoc, color);
            build.setType("buildCity");
            build.setPlayerIndex(currentPlayer);
            build.setVertexLocation(settle);
            try {
                GameInfo updatedModel = ClientFascade.getSingleton().buildCity(build);
                gm.initializeGame(updatedModel);
                getView().placeCity(vertLoc, color);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void placeRobber(HexLocation hexLoc) {
        newRobberLocation = hexLoc;
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        if (gm.getMapManager().moveRobber(hexLoc)) {
            getView().placeRobber(hexLoc);
        }
        List<Location> settlements = gm.getLocationManager().getSettledLocations();
        Set<Integer> playerIndexes = new HashSet<>();
        for (Location settlement : settlements) {
            List<HexLocation> neighbors = gm.getLocationManager().getHexLocationsAroundVertexLocation(settlement.getNormalizedLocation());
            for (HexLocation neighbor : neighbors) {
                if (neighbor.equals(hexLoc)) {
                    playerIndexes.add(settlement.getOwnerID());
                }
            }
        }

        Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();

        List<RobPlayerInfo> victims = new ArrayList<RobPlayerInfo>();
        for (Integer player : playerIndexes) {
            if (currentPlayer != player) {
                victims.add(new RobPlayerInfo(gm.getGame().getPlayers().get(player), gm.getResourceManager().getGameBanks().get(player).getResourcesCards().getTotalResources()));
            }
        }
        RobPlayerInfo[] victimsAreUs = new RobPlayerInfo[victims.size()];
        victims.toArray(victimsAreUs);
        getRobView().showModal();
        getRobView().setPlayers(victimsAreUs);
    }

    /*
     * This method is called when the user requests to place a piece on the map
     * (road, city, or settlement)
     * 
     * @param pieceType The type of piece to be placed
     * 
     * @param isFree true if the piece should not cost the player resources,
     * false otherwise. Set to true during initial setup and when a road
     * building card is played.
     * 
     * @param allowDisconnected true if the piece can be disconnected, false
     * otherwise. Set to true only during initial setup.
     * 
     * void startMove(PieceType pieceType, boolean isFree, boolean
     * allowDisconnected);
     */
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        playedSoldier = false;
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        this.isFree = isFree;
        Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
        CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
        getView().startDrop(pieceType, color, allowDisconnected);
    }

    public void cancelMove() {
        // This shouldn't need to do anything. The view closes the map overlay
        // modal, and the way we built this controller, no resources have been
        // taken yet.
    }

    public void playSoldierCard() {
        startMove(PieceType.ROBBER, true, true);
        playedSoldier = true;
    }

    public void playRoadBuildingCard() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        this.isFree = true;
        int currentPlayerIndex = gm.getGame().getTurnTracker().getCurrentTurn();
        playedRoadBuilding = true;
        if (gm.getResourceManager().getGameBanks().get(currentPlayerIndex).getRoads() > 0) {
            getView().startDrop(PieceType.ROAD,
                    CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayerIndex).getColor().toUpperCase()), false);
        }

    }

    public void robPlayer(RobPlayerInfo victim) {
        // the two players.
        // The RobPlayer class doesn't request a resource, so the resource
        // deciding must happen server side, or on the next poll request
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = gm.getPlayerIndex(playerId);

        Integer victimIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == victim.getPlayerID()) {
                victimIndex = i;
                break;
            }
        }

        if (playerIndex == victimIndex) {
            victimIndex = -1;
        }

        try {
            if (playedSoldier) {
                gm.useSoldier();
                Soldier s = new Soldier(playerIndex);
                s.setType("Soldier");
                s.setVictimIndex(victimIndex);
                s.setLocation(newRobberLocation);
                ClientFascade.getSingleton().soldier(s);
            } else {
                RobPlayer rp = new RobPlayer();
                rp.setPlayerIndex(playerIndex);
                rp.setType("robPlayer");
                rp.setLocation(newRobberLocation);
                rp.setVictimIndex(victimIndex);
                ClientFascade.getSingleton().robPlayer(rp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }

}
