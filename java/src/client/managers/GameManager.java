package client.managers;

import client.data.Card;
import client.data.Edge;
import client.data.Game;
import client.data.Hex;
import client.data.Location;
import client.data.Player;
import client.data.Port;
import client.data.ResourceList;
import com.google.gson.Gson;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.locations.VertexLocation;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

import com.google.gson.GsonBuilder;
import shared.locations.EdgeDirection;
import shared.locations.VertexDirection;

public class GameManager {

    private LocationManager locationManager;
    private MapManager mapManager;
    private ResourceManager resourceManager;
    private Random randomness;
    private Game game;

    public GameManager() {
        randomness = new Random();
        randomness.setSeed(new Date().getTime());
    }

    /**
     * @author ddennis
     * @param jsonData = JSON string of information to initialize the client
     * model
     * @pre model is accurate
     * @post Will initialize the game model
     */
    public void initializeGame(String jsonData) {
        //create a json object
        Gson model = new GsonBuilder().create();

        //initialize the client object model
        game = model.fromJson(jsonData, Game.class);

        /* instantiate all the managers */
        //Location Manager
        locationManager.setPorts(game.getMap().getPorts());

        //Map Manager
        mapManager.setHexList(game.getMap().getHexes());

        //Resource Manager
        List<ResourceList> gameBanks = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            gameBanks.add(player.getPlayerIndex(), player.getResources());
        }
        gameBanks.add(4, game.getBank());
        resourceManager.setGameBanks(gameBanks);

    }
    /* canDo Methods */

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @pre gameID matches an existing game
     * @post Game will be terminated. Players will be evicted from game if still
     * inside.
     */
    public void endGame(int gameID) {

    }

    /**
     * @author Curt
     * @pre The server is running. Method triggered by a request from a
     * successfully logged in user.
     * @post Game is added to the list of games on server
     */
    public void createGame(List<Player> players, boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
        locationManager = new LocationManager();
        mapManager = new MapManager();
        resourceManager = new ResourceManager();
        game = new Game(players, name);

        ArrayList<Hex> hexes = new ArrayList<Hex>();

        Hex robberHex = new Hex(new HexLocation(0, -2), 0, null);
        robberHex.setHasRobber(true);

        hexes.add(new Hex(new HexLocation(2, 0), 11, ResourceType.WOOD));
        hexes.add(new Hex(new HexLocation(2, 1), 12, ResourceType.SHEEP));
        hexes.add(new Hex(new HexLocation(2, 2), 9, ResourceType.WHEAT));

        hexes.add(new Hex(new HexLocation(1, -2), 4, ResourceType.BRICK));
        hexes.add(new Hex(new HexLocation(1, -1), 6, ResourceType.ORE));
        hexes.add(new Hex(new HexLocation(1, 0), 5, ResourceType.BRICK));
        hexes.add(new Hex(new HexLocation(1, 1), 10, ResourceType.SHEEP));

        hexes.add(robberHex);
        hexes.add(new Hex(new HexLocation(0, -1), 3, ResourceType.WOOD));
        hexes.add(new Hex(new HexLocation(0, 0), 11, ResourceType.WHEAT));
        hexes.add(new Hex(new HexLocation(0, 1), 4, ResourceType.WOOD));
        hexes.add(new Hex(new HexLocation(0, 2), 8, ResourceType.WHEAT));

        hexes.add(new Hex(new HexLocation(-1, -1), 8, ResourceType.BRICK));
        hexes.add(new Hex(new HexLocation(-1, 0), 10, ResourceType.SHEEP));
        hexes.add(new Hex(new HexLocation(-1, 1), 9, ResourceType.SHEEP));
        hexes.add(new Hex(new HexLocation(-1, 2), 3, ResourceType.ORE));

        hexes.add(new Hex(new HexLocation(-2, -2), 5, ResourceType.ORE));
        hexes.add(new Hex(new HexLocation(-2, -1), 2, ResourceType.WHEAT));
        hexes.add(new Hex(new HexLocation(-2, 0), 6, ResourceType.WOOD));

        List<Port> ports = new ArrayList<Port>();
        
        ports.add( new Port(2,ResourceType.BRICK,new HexLocation(-2,3)));
        ports.add( new Port(2,ResourceType.ORE,new HexLocation(1,-3)));
        ports.add( new Port(2,ResourceType.SHEEP,new HexLocation(3,-1)));
        ports.add( new Port(2,ResourceType.WHEAT,new HexLocation(-1,-2)));
        ports.add( new Port(2,ResourceType.WOOD,new HexLocation(-3,2)));
        
        ports.add( new Port(3,null,new HexLocation(-3,0)));
        ports.add( new Port(3,null,new HexLocation(0,3)));
        ports.add( new Port(3,null,new HexLocation(2,1)));
        ports.add( new Port(3,null,new HexLocation(3,-3)));
        
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
                if(hexes.get(i).getHasRobber()){
                    continue;
                }
                
                int getRandomHex = randomness.nextInt(19);
                
                while(hexes.get(getRandomHex).getHasRobber()){
                    getRandomHex = randomness.nextInt(19);
                }
                
                int originalNumber = hexes.get(i).getRollValue();
                hexes.get(i).setRollValue(hexes.get(getRandomHex).getRollValue());
                hexes.get(getRandomHex).setRollValue(originalNumber);
                
            }
            
        }
        
        if(randomPorts){
            for (int i = 0; i < ports.size(); i++) {
                int getRandomHex = randomness.nextInt(9);
                HexLocation originalHexLocation = ports.get(i).getLocation();
                ports.get(i).setLocation(ports.get(getRandomHex).getLocation());
                ports.get(getRandomHex).setLocation(originalHexLocation);                
            }
        }
            

        List<Location> unsettledLocations = new ArrayList<Location>();
        List<Edge> unsettledEdges = new ArrayList<Edge>();
        for(Hex hex: hexes){
            unsettledLocations.add(new Location(new VertexLocation(hex.getLocation(),VertexDirection.NorthEast)));
            unsettledLocations.add(new Location(new VertexLocation(hex.getLocation(),VertexDirection.NorthWest)));
            unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(),EdgeDirection.NorthWest)));
            unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(),EdgeDirection.North)));
            unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(),EdgeDirection.NorthEast)));
        }
        
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(2,1),VertexDirection.NorthEast)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(2,1),VertexDirection.NorthWest)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(1,2),VertexDirection.NorthEast)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(1,2),VertexDirection.NorthWest)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(0,3),VertexDirection.NorthEast)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(0,3),VertexDirection.NorthWest)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-1,3),VertexDirection.NorthEast)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-1,3),VertexDirection.NorthWest)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-2,3),VertexDirection.NorthEast)));
        unsettledLocations.add(new Location(new VertexLocation(new HexLocation(-2,3),VertexDirection.NorthWest)));
        
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(2,1),EdgeDirection.North)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(2,1),EdgeDirection.NorthWest)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(1,2),EdgeDirection.North)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(1,2),EdgeDirection.NorthWest)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(0,3),EdgeDirection.North)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-1,3),EdgeDirection.North)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-1,3),EdgeDirection.NorthEast)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-2,3),EdgeDirection.North)));
        unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-2,3),EdgeDirection.NorthEast)));
        
        mapManager.setHexList(hexes);
        locationManager.setUnsettledLocations(unsettledLocations);
        locationManager.setUnsettledEdges(unsettledEdges);
        locationManager.setPorts(ports);

    }
/*
    
int ratio, ResourceType resource, HexLocation location
    
    */
    
    /**
     * @author Curt
     * @pre Server is running
     * @post If games exist, all existing games will be listed. Otherwise a
     * message will display "No games found"
     */
    public List<Game> listGames(int gameID) {
        return new ArrayList<>();
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @pre gameID matches an existing Game
     * @post Player will join a game
     */
    public void joinGame(int gameID) {

    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @pre gameID matches an existing Game
     * @post returns boolean value for whether game has 4 players or not
     */
    public boolean gameHasFourPlayers(int gameID) {

        return false;
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param edge = valid edge location where road should be placed
     * @pre gameID matches existing game. Edge Locations is a valid location in
     * this game's map
     * @post adds a road to the requested edge in the game
     */
    public void placeRoad(int gameID, EdgeLocation edge) {

    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param hex = hex used to find the normalized vertex location where
     * settlement should be placed
     * @param vertex = vertex where the settlement should be placed
     * @pre gameID matches existing game. Hex and VertexLocations are valid
     * location in this game's map.
     * @post
     */
    public void placeSettlement(int gameID, HexLocation h, VertexLocation v) {

    }

    /**
     * @author Curt
     * @pre gameId matches an existing game. It's the start of a player's turn
     * @post diceRollEvents from the MapManager are triggered.
     */
    public void rollDice(int dieRoll) {

//        int dieRoll = randomness.nextInt(6) + randomness.nextInt(6) + 2;
        List<Hex> hexesProducingResources = mapManager.getTerrainResourceHexes(dieRoll);
        ResourceList gameBank = resourceManager.getGameBanks().get(4);

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

            //If a player shows up, add one resource to them
            List<Integer> playersEarningResources = new ArrayList<>();

            for (Hex hexProducingResources : hexesProducingAParticularResource) {
                playersEarningResources.addAll(locationManager.awardTerrainResource(hexProducingResources.getLocation()));
            }

            int amountAvailable = 0;
            switch (resourceType) {
                case ORE:
                    amountAvailable = gameBank.getOre();
                    break;
                case WOOD:
                    amountAvailable = gameBank.getWood();
                    break;
                case BRICK:
                    amountAvailable = gameBank.getBrick();
                    break;
                case SHEEP:
                    amountAvailable = gameBank.getSheep();
                    break;
                case WHEAT:
                    amountAvailable = gameBank.getWheat();
                    break;
                default:
                    break;
            }

            if (amountAvailable >= playersEarningResources.size()) {
                Card transferringCard = new Card(true, resourceType, DevCardType.MONOPOLY, true, false);
                for (Integer earningPlayer : playersEarningResources) {
                    resourceManager.transferCard(4, earningPlayer, transferringCard);
                }
            }

        }

    }

    public void diceIsSevenMoveRober(HexLocation newLocationForRobber) {
        mapManager.moveRobber(newLocationForRobber);

        for (int i = 0; i < 4; i++) {
            int numberOfResourceCards = resourceManager.getGameBanks().get(i).getTotalResources();
            if (numberOfResourceCards >= 7) {
                resourceManager.playerDiscardsHalfCards(i);
            }
        }
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param card = requested card type
     * @pre gameId matches an existing game. Card is a valid cardType
     * @post Success = Cards will be transferred between current player bank and
     * game bank.
     */
    public boolean buy(int gameID, String card) {

        return false;
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param object = structure type
     * @pre gameID matches an existing game. object matches a valid structure
     * @post Success = Structure built, player victory point incremented,
     * structure count decremented.
     */
    public boolean build(int gameID, String object) {

        return false;
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
    public boolean trade(int gameID, int senderID, int receiverID, ArrayList<Card> offer, ArrayList<Card> request) {

        return false;
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @param card = card being used.
     * @pre Card exists in the player's hand
     * @post Card will be returned to game's bank (unless it's a development
     * card. Then it will be moved to another part of the player's bank) Card
     * effect will be carried out.
     */
    public boolean useCard(int gameID, Card c) {

        return false;
    }

    /**
     * @author Curt
     * @param gameID = unique ID of a game in the server's games list
     * @pre gameID exists. Method only called after other turn-operations are
     * all completed (No trades ongoing, cards being played, etc)
     * @post The current player pointer will move to the next player. All cards
     * in all player's hands will be marked as usable.
     */
    public void endTurn(int gameID) {

    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
