package client.managers;

import client.data.Bank;
import client.data.DevCardList;
import client.data.Edge;
import client.data.Hex;
import client.data.Location;
import client.data.Port;
import client.data.ResourceList;
import client.data.Map;
import client.data.VertexObject;
import javafx.geometry.VerticalDirection;
import client.data.EdgeValue;
import client.data.GameInfo;
import client.data.PlayerInfo;


import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;
import java.util.HashSet;

import shared.definitions.DevCardType;
import shared.definitions.PieceType;
import shared.definitions.ResourceType;
import shared.locations.VertexLocation;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

import shared.locations.EdgeDirection;
import shared.locations.VertexDirection;

public class GameManager {

    private LocationManager locationManager;
    private MapManager mapManager;
    private ResourceManager resourceManager;
    private Random randomness;
    private GameInfo game;
    private final int mainBankIndex = 4;

    public GameManager() {
        randomness = new Random();
        randomness.setSeed(new Date().getTime());
    }

    /**
     * @author ddennis = JSON string of information to initialize the client
     * model
     * @param game
     * @pre model is accurate
     * @post Will initialize the game model
     */
    public void initializeGame(GameInfo game) {
        try {
            for(int i=0;i<game.getMap().getSettlements().size();i++){
                int x = game.getMap().getSettlements().get(i).getLocation().getX();
                int y = game.getMap().getSettlements().get(i).getLocation().getY();
                VertexDirection dir = game.getMap().getSettlements().get(i).getLocation().getDirection();
                game.getMap().getSettlements().get(i).setDirection(new VertexLocation(new HexLocation(x,y),dir));
            }
            for(int i=0;i<game.getMap().getCities().size();i++){
                int x = game.getMap().getCities().get(i).getLocation().getX();
                int y = game.getMap().getCities().get(i).getLocation().getY();
                VertexDirection dir = game.getMap().getCities().get(i).getLocation().getDirection();
                game.getMap().getCities().get(i).setDirection(new VertexLocation(new HexLocation(x,y),dir));
            }
            for(int i=0;i<game.getMap().getRoads().size();i++){
                int x = game.getMap().getRoads().get(i).getLocation().getX();
                int y = game.getMap().getRoads().get(i).getLocation().getY();
                EdgeDirection dir = game.getMap().getRoads().get(i).getLocation().getDirection();
                game.getMap().getRoads().get(i).setLocation2(new EdgeLocation(new HexLocation(x,y),dir));
            }
            
            
            //our game
            setGame(game);
            if (game.getTurnTracker().getLargestArmy() == -1) {
                game.getTurnTracker().setLargestArmy(4);
            }
            if (game.getTurnTracker().getLongestRoad() == -1) {
                game.getTurnTracker().setLongestRoad(4);
            }
    
            Map map = game.getMap();
            
            HexLocation robberLocation = map.getRobber();

            if (mapManager == null) {
                mapManager = new MapManager();
            }

           
            List<Hex> hexList = mapManager.getHexList();
            for (Hex hex : hexList) {
                if (hex.getLocation().equals(robberLocation)) {
                    hex.setHasRobber(true);
                } else {
                    hex.setHasRobber(false);
                }
            }
            mapManager.setHexList(map.getHexes());
            if (locationManager == null) {
                locationManager = new LocationManager();
                createUnsettledAreas(hexList);
                locationManager.setPorts(map.getPorts());
            }

            
            for (VertexObject settlement : map.getSettlements()) {
                Location settlementLocation = null;
                for (Location location : locationManager.getUnsettledLocations()) {
            
                    if (location.getNormalizedLocation().equals(settlement.getDirection())) {
                        location.setIsCity(false);
                        location.getWhoCanBuild().add(settlement.getOwner());
                        settlementLocation = location;
                        break;
                    }

                }

                if (settlementLocation != null) {
                    if (!locationManager.settleLocation(settlementLocation.getNormalizedLocation(),
                            settlement.getOwner(), false)) {
                        throw new Exception("Unable to settle location from initializeGame.");
                    }
                }

            }

            for (VertexObject city : map.getCities()) {
                for (Location location : locationManager.getSettledLocations()) {
                    if (location.getNormalizedLocation().equals(city.getDirection())) {
                        locationManager.upgradeToCity(city.getDirection());
                    }
                }
            }

            for (EdgeValue road : map.getRoads()) {
                Edge roadLocation = null;

                for (Edge edge : locationManager.getUnsettledEdges()) {
                    if (edge.getEdgeLocation().equals(road.getLocation2())) {
                        edge.getWhoCanBuild().add(road.getOwner());
                        roadLocation = edge;
                        break;
                    }
                }

                if (roadLocation != null) {
                    if (!locationManager.settleEdge(roadLocation.getEdgeLocation(), road.getOwner())) {
                        throw new Exception("Unable to settle edge from initializeGame.");
                    }
                }
            }

            // Resource Manager
            List<Bank> gameBanks = new ArrayList<>();
            for (int i = 0; i < game.getPlayers().size(); i++) {
                PlayerInfo p = game.getPlayers().get(i);
                boolean hasLargestArmy = (p.getPlayerID() == game.getTurnTracker().getLargestArmy());
                boolean hasLongestRoad = (p.getPlayerID() == game.getTurnTracker().getLongestRoad());
                gameBanks.add(new Bank(p, hasLargestArmy, hasLongestRoad));
            }

            DevCardList mainBankDevCards = new DevCardList(game.getPlayers());
            boolean hasLargestArmy = (mainBankIndex == game.getTurnTracker().getLargestArmy());
            boolean hasLongestRoad = (mainBankIndex == game.getTurnTracker().getLongestRoad());
            gameBanks.add(new Bank(game.getBank(), mainBankDevCards, hasLargestArmy, hasLongestRoad));
            if (resourceManager == null) {
                resourceManager = new ResourceManager();
            }
            resourceManager.setGameBanks(gameBanks);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /* canDo Methods */
    /**
     * @author Curt = unique ID of a game in the server's games list
     * @pre gameID matches an existing game
     * @post Game will be terminated. Players will be evicted from game if still
     * inside.
     */
    public void endGame() {
        // TODO: interact with the GUI to boot everyone from the game
        game = null;
        locationManager = null;
        resourceManager = null;
        mapManager = null;
    }

    /**
     * @author Curt
     * @pre The server is running. Method triggered by a request from a
     * successfully logged in user. There are exactly 4 players in the lobby
     * @post Game is added to the list of games on server
     */
    public void createGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
        locationManager = new LocationManager();
        mapManager = new MapManager();
        resourceManager = new ResourceManager();
        game = new GameInfo(name);

        List<Hex> hexes = new ArrayList<Hex>();

        Hex robberHex = new Hex(new HexLocation(0, -2), 0, null);
        robberHex.setHasRobber(true);

        hexes.add(new Hex(new HexLocation(2, 0), 11, ResourceType.wood));
        hexes.add(new Hex(new HexLocation(2, 1), 12, ResourceType.sheep));
        hexes.add(new Hex(new HexLocation(2, 2), 9, ResourceType.wheat));

        hexes.add(new Hex(new HexLocation(1, -2), 4, ResourceType.brick));
        hexes.add(new Hex(new HexLocation(1, -1), 6, ResourceType.ore));
        hexes.add(new Hex(new HexLocation(1, 0), 5, ResourceType.brick));
        hexes.add(new Hex(new HexLocation(1, 1), 10, ResourceType.sheep));

        hexes.add(robberHex);
        hexes.add(new Hex(new HexLocation(0, -1), 3, ResourceType.wood));
        hexes.add(new Hex(new HexLocation(0, 0), 11, ResourceType.wheat));
        hexes.add(new Hex(new HexLocation(0, 1), 4, ResourceType.wood));
        hexes.add(new Hex(new HexLocation(0, 2), 8, ResourceType.wheat));

        hexes.add(new Hex(new HexLocation(-1, -1), 8, ResourceType.brick));
        hexes.add(new Hex(new HexLocation(-1, 0), 10, ResourceType.sheep));
        hexes.add(new Hex(new HexLocation(-1, 1), 9, ResourceType.sheep));
        hexes.add(new Hex(new HexLocation(-1, 2), 3, ResourceType.ore));

        hexes.add(new Hex(new HexLocation(-2, -2), 5, ResourceType.ore));
        hexes.add(new Hex(new HexLocation(-2, -1), 2, ResourceType.wheat));
        hexes.add(new Hex(new HexLocation(-2, 0), 6, ResourceType.wood));

        List<Port> ports = new ArrayList<Port>();

        ports.add(new Port(2, ResourceType.brick, new HexLocation(-2, 3)));
        ports.add(new Port(2, ResourceType.ore, new HexLocation(1, -3)));
        ports.add(new Port(2, ResourceType.sheep, new HexLocation(3, -1)));
        ports.add(new Port(2, ResourceType.wheat, new HexLocation(-1, -2)));
        ports.add(new Port(2, ResourceType.wood, new HexLocation(-3, 2)));

        ports.add(new Port(3, null, new HexLocation(-3, 0)));
        ports.add(new Port(3, null, new HexLocation(0, 3)));
        ports.add(new Port(3, null, new HexLocation(2, 1)));
        ports.add(new Port(3, null, new HexLocation(3, -3)));

        if (randomTiles) {
            for (int i = 0; i < hexes.size(); i++) {
                int getRandomHex = randomness.nextInt(19);
                HexLocation originalHexLocation = hexes.get(i).getLocation();
                hexes.get(i).setLocation(hexes.get(getRandomHex).getLocation());
                hexes.get(getRandomHex).setLocation(originalHexLocation);
            }
        }

        if (randomNumbers) {
            for (int i = 0; i < hexes.size(); i++) {
                if (hexes.get(i).getHasRobber()) {
                    continue;
                }

                int getRandomHex = randomness.nextInt(19);

                while (hexes.get(getRandomHex).getHasRobber()) {
                    getRandomHex = randomness.nextInt(19);
                }

                int originalNumber = hexes.get(i).getRollValue();
                hexes.get(i).setRollValue(hexes.get(getRandomHex).getRollValue());
                hexes.get(getRandomHex).setRollValue(originalNumber);

            }

        }

        if (randomPorts) {
            for (int i = 0; i < ports.size(); i++) {
                int getRandomHex = randomness.nextInt(9);
                HexLocation originalHexLocation = ports.get(i).getLocation();
                ports.get(i).setLocation(ports.get(getRandomHex).getLocation());
                ports.get(getRandomHex).setLocation(originalHexLocation);
            }
        }

        createUnsettledAreas(hexes);

        mapManager.setHexList(hexes);
        locationManager.setPorts(ports);

    }

    public void createUnsettledAreas(List<Hex> hexes) {
        List<Location> unsettledLocations = new ArrayList<Location>();
        List<Edge> unsettledEdges = new ArrayList<Edge>();
        for (Hex hex : mapManager.getHexList()) {
            unsettledLocations.add(new Location(new VertexLocation(hex.getLocation(), VertexDirection.NE)));
            unsettledLocations.add(new Location(new VertexLocation(hex.getLocation(), VertexDirection.NW)));
            unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(), EdgeDirection.NW)));
            unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(), EdgeDirection.N)));
            unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(), EdgeDirection.NE)));
        }

        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-3, 1), VertexDirection.NE)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-3, 2), VertexDirection.NE)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-3, 3), VertexDirection.NE)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(3, 0), VertexDirection.NW)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(3, -1), VertexDirection.NW)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(3, -2), VertexDirection.NW)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(2, 1), VertexDirection.NE)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(2, 1), VertexDirection.NW)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(1, 2), VertexDirection.NE)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(1, 2), VertexDirection.NW)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(0, 3), VertexDirection.NE)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(0, 3), VertexDirection.NW)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-1, 3), VertexDirection.NE)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-1, 3), VertexDirection.NW)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-2, 3), VertexDirection.NE)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-2, 3), VertexDirection.NW)));

        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-3, -3), EdgeDirection.NW)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-3, -2), EdgeDirection.NW)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-3, -1), EdgeDirection.NW)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(3, -2), EdgeDirection.NW)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(3, -1), EdgeDirection.NW)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(3, 0), EdgeDirection.NW)));        
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(2, 1), EdgeDirection.N)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(2, 1), EdgeDirection.NW)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(1, 2), EdgeDirection.N)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(1, 2), EdgeDirection.NW)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(0, 3), EdgeDirection.N)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-1, 3), EdgeDirection.N)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-1, 3), EdgeDirection.NE)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-2, 3), EdgeDirection.N)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-2, 3), EdgeDirection.NE)));
        
        for(Location l : unsettledLocations) {
            HashSet<Integer> allPlayers = new HashSet<Integer>();
            allPlayers.add(0);
            allPlayers.add(1);
            allPlayers.add(2);
            allPlayers.add(3);
            l.setWhoCanBuild(allPlayers);
        }
        
        locationManager.setUnsettledLocations(unsettledLocations);
        locationManager.setUnsettledEdges(unsettledEdges);
    }

    /*
     * public void joinGame(int playerUserID, String name, String color) { //
     * TODO: interact better with the GUI to handle login if
     * (!gameHasFourPlayers()) { Player p = new Player(playerUserID, name,
     * color); p.setPlayerIndex(this.game.getPlayers().size());
     * this.game.getPlayers().add(p); } }
     */
    /*
     * public boolean gameHasFourPlayers() { return game.getPlayers().size() ==
     * 4; }
     */
    /**
     * @author Curt
     * @pre gameId matches an existing game. It's the start of a player's turn
     * @post diceRollEvents from the MapManager are triggered.
     */
    public void rollDice(int dieRoll) {

        // int dieRoll = randomness.nextInt(6) + randomness.nextInt(6) + 2;
        List<Hex> hexesProducingResources = mapManager.getTerrainResourceHexes(dieRoll);
        ResourceList gameBank = resourceManager.getGameBanks().get(mainBankIndex).getResourcesCards();

        for (ResourceType resourceType : ResourceType.values()) {
            List<Hex> hexesProducingAParticularResource = new ArrayList<>();

            for (Hex particularHex : hexesProducingResources) {
                if (resourceType == particularHex.getResource()) {
                    hexesProducingAParticularResource.add(particularHex);
                }
            }

            if (hexesProducingAParticularResource.isEmpty()) {
                continue;
            }

            // If a player shows up, add one resource to them
            List<Integer> playersEarningResources = new ArrayList<>();

            for (Hex hexProducingResources : hexesProducingAParticularResource) {
                playersEarningResources
                        .addAll(locationManager.awardTerrainResource(hexProducingResources.getLocation()));
            }

            int amountAvailable = 0;
            switch (resourceType) {
                case ore:
                    amountAvailable = gameBank.getOre();
                    break;
                case wood:
                    amountAvailable = gameBank.getWood();
                    break;
                case brick:
                    amountAvailable = gameBank.getBrick();
                    break;
                case sheep:
                    amountAvailable = gameBank.getSheep();
                    break;
                case wheat:
                    amountAvailable = gameBank.getWheat();
                    break;
                default:
                    break;
            }

            if (amountAvailable >= playersEarningResources.size()) {
                ResourceList resource = new ResourceList(resourceType);
                for (Integer earningPlayer : playersEarningResources) {
                    resourceManager.transferResourceCard(mainBankIndex, earningPlayer, resource);
                }
            }

        }

    }

    public void diceIsSevenMoveRobber(HexLocation newLocationForRobber) {
		// TODO: players needs to choose the cards they discard for the call to
        // "playerDiscardsHalfCards()"
        if (mapManager.moveRobber(newLocationForRobber)) {
            for (int i = 0; i < 4; i++) {
                int numberOfResourceCards = resourceManager.getGameBanks().get(i).getResourcesCards()
                        .getTotalResources();
                if (numberOfResourceCards >= 7) {
                    resourceManager.playerDiscardsHalfCards(i, new ResourceList());
                }
            }
        }
    }

    public boolean placeFreeRoad(EdgeLocation edge) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
        if (playerBank.getRoads() > 0) {
            playerBank.removeRoad();
            locationManager.settleEdge(edge, currentPlayer);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param hex = hex used to find the normalized vertex location where
     * settlement should be placed
     * @param vertex = vertex where the settlement should be placed
     * @pre gameID matches existing game. Hex and VertexLocations are valid
     * location in this game's map. VertexLocation is settle-able.
     * @post
     */
    public void placeFirstSettlement(VertexLocation v) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
        locationManager.settleLocation(v, currentPlayer, true);
        playerBank.removeSettlement();
    }

    public void placeSecondSettlement(VertexLocation v) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        locationManager.settleLocation(v, currentPlayer, true);
        List<HexLocation> neighbors = locationManager.getHexLocationsAroundVertexLocation(v);
        ResourceList resourceList = new ResourceList(0, 0, 0, 0, 0);
        for (Hex hex : mapManager.getHexList()) {
            if (hex.equals(neighbors.get(0)) || hex.equals(neighbors.get(1)) || hex.equals(neighbors.get(2))) {
                if (hex.getResource() != null) {
                    switch (hex.getResource()) {
                        case ore:
                            resourceList.setOre(resourceList.getOre() + 1);
                            break;
                        case wood:
                            resourceList.setWood(resourceList.getWood() + 1);
                            break;
                        case brick:
                            resourceList.setBrick(resourceList.getBrick() + 1);
                            break;
                        case sheep:
                            resourceList.setSheep(resourceList.getSheep() + 1);
                            break;
                        case wheat:
                            resourceList.setWheat(resourceList.getWheat() + 1);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        resourceManager.transferResourceCard(mainBankIndex, currentPlayer, resourceList);
    }

    public boolean canBuildRoad() {
        ResourceList resList = resourceManager.getGameBanks().get(currentPlayer()).getResourcesCards();
        Bank bank = resourceManager.getGameBanks().get(currentPlayer());
        return resList.getWood() > 0 && resList.getBrick() > 0 && bank.getRoads() > 0;
    }

    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        boolean canPlaceRoad = false;        
        if(locationManager.getSettledLocations().size() > 4 && locationManager.getSettledLocations().size() < 9) {
        	ArrayList<Location> mySettledLocations = new ArrayList<Location>();
        	for(Location l : locationManager.getSettledLocations()){
        		if(l.getOwnerID() == currentPlayer()){
        			mySettledLocations.add(l);
        		}
        	}
        	
        	boolean hasRoad = false;
            VertexLocation v = mySettledLocations.get(0).getNormalizedLocation();
            if(v.getDir() == VertexDirection.NW) {
            	Edge e1 = new Edge(new EdgeLocation(v.getHexLoc(), EdgeDirection.N));
            	Edge e2 = new Edge(new EdgeLocation(v.getHexLoc(), EdgeDirection.NW));
            	Edge e3 = new Edge(new EdgeLocation(new HexLocation(v.getHexLoc().getX() -1, v.getHexLoc().getY()), EdgeDirection.NE));
            	for(Edge e : locationManager.getSettledEdges()) {
            		if(e.getEdgeLocation().equals(e1.getEdgeLocation()) || e.getEdgeLocation().equals(e2.getEdgeLocation()) || e.getEdgeLocation().equals(e3.getEdgeLocation()))
            			hasRoad = true;
            	}
            	if(edgeLoc.equals(e1.getEdgeLocation()) || edgeLoc.equals(e2.getEdgeLocation()) || edgeLoc.equals(e3.getEdgeLocation())){
            		if(hasRoad)
            			return false;
            		else
            			return true;
            	}
            } else {
            	Edge e1 = new Edge(new EdgeLocation(v.getHexLoc(), EdgeDirection.N));
            	Edge e2 = new Edge(new EdgeLocation(v.getHexLoc(), EdgeDirection.NE));
            	Edge e3 = new Edge(new EdgeLocation(new HexLocation(v.getHexLoc().getX() + 1, v.getHexLoc().getY() + 1), EdgeDirection.NW));
            	for(Edge e : locationManager.getSettledEdges()) {
            		if(e.getEdgeLocation().equals(e1.getEdgeLocation()) || e.getEdgeLocation().equals(e2.getEdgeLocation()) || e.getEdgeLocation().equals(e3.getEdgeLocation()))
            			hasRoad = true;
            	}            	
            	if(edgeLoc.equals(e1.getEdgeLocation()) || edgeLoc.equals(e2.getEdgeLocation()) || edgeLoc.equals(e3.getEdgeLocation())){
            		if(hasRoad)
            			return false;
            		else
            			return true;
            	}
            }
            return false;     	
        } else {
            for (Edge edge : locationManager.getUnsettledEdges()) {
                if (edge.getEdgeLocation().equals(edgeLoc)) {
                    if (edge.getWhoCanBuild().contains(currentPlayer())) {
                        canPlaceRoad = true;
                    }
                }
            }
            return canPlaceRoad;        	
        }
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param object = structure type
     * @pre gameID matches an existing game. object matches a valid structure
     * @post Success = Structure built, player victory point incremented,
     * structure count decremented.
     */
    public boolean buildRoad(EdgeLocation edge) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
        ResourceList cost = new ResourceList(1, 0, 0, 0, 1);
        if (playerBank.getRoads() > 0 && playerBank.getResourcesCards().hasCardsAvailable(cost)) {
            resourceManager.transferResourceCard(currentPlayer, mainBankIndex, cost);
            resourceManager.placedRoad(currentPlayer);
            return locationManager.settleEdge(edge, currentPlayer);
        } else {
            return false;
        }
    }

    public boolean canBuildSettlement() {
        ResourceList resList = resourceManager.getGameBanks().get(currentPlayer()).getResourcesCards();
        Bank bank = resourceManager.getGameBanks().get(currentPlayer());
        return resList.getBrick() > 0 && resList.getSheep() > 0 && resList.getWheat() > 0 && resList.getWood() > 0 && bank.getSettlements() > 0;
    }

    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        boolean canPlaceSettlement = false;
        for (Location lc : locationManager.getUnsettledLocations()) {
            if (lc.getNormalizedLocation().equals(vertLoc)) {
            	if (lc.getWhoCanBuild().contains(currentPlayer())) {
                    canPlaceSettlement = true;
                }
            }
        }
        return canPlaceSettlement;

    }

    public boolean canBuildCity() {
        ResourceList resList = resourceManager.getGameBanks().get(currentPlayer()).getResourcesCards();
        Bank bank = resourceManager.getGameBanks().get(currentPlayer());
        return resList.getOre() > 2 && resList.getWheat() > 1 && bank.getCities() > 0 && bank.getSettlements() < 5;
    }

    public boolean canPlaceCity(VertexLocation vertLoc) {
        boolean canPlaceCity = false;
        for (Location lc : locationManager.getSettledLocations()) {
            if (lc.getNormalizedLocation().equals(vertLoc)) {
                if (!lc.getIsCity() && lc.getOwnerID() == currentPlayer()) {
                    canPlaceCity = true;
                }
            }
        }
        return canPlaceCity;

    }

    public boolean buildStructure(PieceType type, VertexLocation v) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
        ResourceList cost = new ResourceList();

        switch (type) {
            case SETTLEMENT:
                cost = new ResourceList(1, 0, 1, 1, 1);
                if (playerBank.getResourcesCards().hasCardsAvailable(cost)) {
                    resourceManager.transferResourceCard(currentPlayer, mainBankIndex, cost);
                    resourceManager.placedSettlement(currentPlayer);
                    return locationManager.settleLocation(v, currentPlayer, false);
                } else {
                    return false;
                }
            case CITY:
                cost = new ResourceList(0, 3, 0, 2, 0);
                if (playerBank.getResourcesCards().hasCardsAvailable(cost)) {
                    resourceManager.transferResourceCard(currentPlayer, mainBankIndex, cost);
                    resourceManager.placedCity(currentPlayer);
                    return locationManager.upgradeToCity(v);
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    public boolean canPlaceRobber(HexLocation hexLoc) {
        boolean canPlaceRobber = false;
        for (Hex hex : mapManager.getHexList()) {
            if (hex.getLocation().equals(hexLoc)) {
                if (!hex.getHasRobber()) {
                    canPlaceRobber = true;
                }
            }
        }
        return canPlaceRobber;
    }

    public boolean canBuyCard() {
        ResourceList resList = resourceManager.getGameBanks().get(currentPlayer()).getResourcesCards();
        return resList.getOre() > 0 && resList.getSheep() > 0 && resList.getWheat() > 0;
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param card = requested card type
     * @pre gameId matches an existing game. Card is a valid cardType
     * @post Success = Cards will be transferred between current player bank and
     * game bank.
     */
    public DevCardType buyDevCard() {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        ResourceList cost = new ResourceList(0, 1, 1, 1, 0);

        resourceManager.transferResourceCard(currentPlayer, mainBankIndex, cost);
        return resourceManager.devCardBought(currentPlayer);
    }

    public boolean tradeOffer(int senderID, int receiverID, ResourceList offer, ResourceList request) {
        ResourceList senderResources = resourceManager.getGameBanks().get(senderID).getResourcesCards();
        ResourceList receiverResources = resourceManager.getGameBanks().get(receiverID).getResourcesCards();
        if (senderResources.hasCardsAvailable(offer) && receiverResources.hasCardsAvailable(request)) {
            if (receiverID == mainBankIndex) {
                tradeAccepted(senderID, receiverID, offer, request);
                return true;
            } else {
                // TODO: prompt receiver for acceptance
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param senderID = unique ID of the current player
     * @param receiverID = unique ID of the trade recipient (game's bankID if
     * maritime trade)
     * @param offer = List of cards being offered by current player
     * @param request = List of cards requested from the trade recipient
     * @pre gameID is valid. Sender and receiver are valid. Sender has all cards
     * in the offer available
     * @post Failure = Receiver doesn't have the requested cards; Success =
     * cards are transferred between the two player's banks.
     */
    public boolean tradeAccepted(int senderID, int receiverID, ResourceList offer, ResourceList request) {
        ResourceList senderResources = resourceManager.getGameBanks().get(senderID).getResourcesCards();
        ResourceList receiverResources = resourceManager.getGameBanks().get(receiverID).getResourcesCards();
        if (senderResources.hasCardsAvailable(offer) && receiverResources.hasCardsAvailable(request)) {
            resourceManager.transferResourceCard(senderID, receiverID, offer);
            resourceManager.transferResourceCard(receiverID, senderID, request);
            return true;
        } else {
            return false;
        }
    }

    public boolean canPlayCard() {
        DevCardList devList = resourceManager.getGameBanks().get(currentPlayer()).getDevelopmentCards();
        return devList.totalCardsRemaining() > 0;
    }

    public boolean canUseMonopoly() {
        DevCardList devList = resourceManager.getGameBanks().get(currentPlayer()).getDevelopmentCards();
        return devList.getMonopoly() > 0;
    }

    public void useMonopoly(ResourceType r) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        // TODO: use GUI to prompt user for Resource Type
        for (int i = 0; i < 4; i++) {
            if (i != currentPlayer) {
                ResourceList playerResources = resourceManager.getGameBanks().get(i).getResourcesCards();
                ResourceList allResource = new ResourceList();
                switch (r) {
                    case brick:
                        allResource.setBrick(playerResources.getBrick());
                        break;
                    case ore:
                        allResource.setOre(playerResources.getOre());
                        break;
                    case wheat:
                        allResource.setWheat(playerResources.getWheat());
                        break;
                    case sheep:
                        allResource.setSheep(playerResources.getSheep());
                        break;
                    case wood:
                        allResource.setWood(playerResources.getWood());
                        break;
                    default:
                        return;
                }
                resourceManager.transferResourceCard(i, currentPlayer, allResource);
            }
        }
        resourceManager.monopolyUsed(currentPlayer);
    }

    public boolean canUseMonument() {
        DevCardList devList = resourceManager.getGameBanks().get(currentPlayer()).getDevelopmentCards();
        return devList.getMonument() > 0;
    }

    public void useMonument() {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        PlayerInfo p = game.getPlayers().get(currentPlayer);
        p.setVictoryPoints(p.getVictoryPoints() + 1);
        resourceManager.monumentUsed(currentPlayer);
    }

    public boolean canUseRoadBuilding() {
        DevCardList devList = resourceManager.getGameBanks().get(currentPlayer()).getDevelopmentCards();
        return devList.getRoadBuilding() > 0;
    }

    public void useRoadBuilding(EdgeLocation road1, EdgeLocation road2) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        Bank b = resourceManager.getGameBanks().get(currentPlayer);
        if (b.getRoads() > 0 && road1 != null) {
            placeFreeRoad(road1);
        }

        if (b.getRoads() > 0 && road2 != null) {
            placeFreeRoad(road2);
        }

        resourceManager.roadBuildingUsed(currentPlayer);
    }

    public boolean canUseYearOfPlenty() {
        DevCardList devList = resourceManager.getGameBanks().get(currentPlayer()).getDevelopmentCards();
        return devList.getYearOfPlenty() > 0;
    }

    public boolean useYearOfPlenty(ResourceType type1, ResourceType type2) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        ResourceList r = new ResourceList();
        r.add(type1, 1);
        r.add(type2, 1);
        ResourceList mainBankResources = resourceManager.getGameBanks().get(mainBankIndex).getResourcesCards();
        if (mainBankResources.hasCardsAvailable(r)) {
            resourceManager.transferResourceCard(mainBankIndex, currentPlayer, r);
            resourceManager.yearOfPlentyUsed(currentPlayer);
            return true;
        } else {
            return false;
        }
    }

    public boolean canUseSoldier() {
        DevCardList devList = resourceManager.getGameBanks().get(currentPlayer()).getDevelopmentCards();
        return devList.getSoldier() > 0;
    }

    public void useSoldier(HexLocation h) {
        int currentPlayer = game.getTurnTracker().getCurrentTurn();
        diceIsSevenMoveRobber(h);
        resourceManager.soldierUsed(currentPlayer);
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @pre gameID exists. Method only called after other turn-operations are
     * all completed (No trades ongoing, cards being played, etc)
     * @post The current player pointer will move to the next player. All cards
     * in all player's hands will be marked as usable.
     */
    public void endTurn() {
        resourceManager.makeCardsUsable();
        if(locationManager.getSettledLocations().size() == 8) {
        	for(Location l : locationManager.getUnsettledLocations()){
        		l.setWhoCanBuild(new HashSet<Integer>());
        	}
        }
        // TODO: interact with GUI to disable for non-current player
    }

    public GameInfo getGame() {
        return game;
    }

    public void setGame(GameInfo game) {
        this.game = game;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    private int currentPlayer() {
        return game.getTurnTracker().getCurrentTurn();
    }

    public int getPlayerIndex(int playerId) {
        Integer playerIndex = 4;
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (game.getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        return playerIndex;
    }
}
