package client.managers;

import client.data.Bank;
import client.data.DevCardList;
import client.data.Edge;
import client.data.Game;
import client.data.Hex;
import client.data.Location;
import client.data.Player;
import client.data.Port;
import client.data.ResourceList;
import client.data.Map;
import client.data.VertexObject;
import client.data.EdgeValue;

import com.google.gson.Gson;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

import shared.definitions.PieceType;
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
	private final int mainBankIndex = 4;

	public GameManager() {
		randomness = new Random();
		randomness.setSeed(new Date().getTime());
	}

	/**
	 * @author ddennis
	 * @param jsonData
	 *            = JSON string of information to initialize the client model
	 * @pre model is accurate
	 * @post Will initialize the game model
	 */
	public void initializeGame(String jsonData) {
		// create a json object
		Gson model = new GsonBuilder().create();

		// initialize the client object model
		game = model.fromJson(jsonData, Game.class);
		Map map = game.getMap();
		HexLocation robberLocation = map.getRobber();

		if (mapManager == null) {
			mapManager = new MapManager();
			mapManager.setHexList(map.getHexes());
		}

		for (Hex hex : mapManager.getHexList()) {
			if (hex.getLocation().equals(robberLocation)) {
				hex.setHasRobber(true);
			} else {
				hex.setHasRobber(false);
			}
		}

		if (locationManager == null) {
			locationManager = new LocationManager();
			createUnsettledAreas(mapManager.getHexList());
			locationManager.setPorts(map.getPorts());
		}

		for (VertexObject settlement : map.getSettlements()) {

			Location settlementLocation = null;

			for (Location location : locationManager.getUnsettledLocations()) {
				if (location.getNormalizedLocation().equals(
						settlement.getDirection())) {
					location.setIsCity(false);
					location.getWhoCanBuild().add(settlement.getOwner());
					settlementLocation = location;
					break;
				}

			}

			if (settlementLocation != null) {
				if (!locationManager.settleLocation(
						settlementLocation.getNormalizedLocation(),
						settlement.getOwner(), false)) {
					System.out
							.println("Unable to settle location from initializeGame");
				}
			}

		}

		for (EdgeValue road : map.getRoads()) {
			Edge roadLocation = null;

			for (Edge edge : locationManager.getUnsettledEdges()) {
				if (edge.getEdgeLocation().equals(road.getLocation())) {
					edge.getWhoCanBuild().add(road.getOwner());
					roadLocation = edge;
					break;
				}
			}

			if (roadLocation != null) {
				if (!locationManager.settleEdge(roadLocation.getEdgeLocation(),
						road.getOwner())) {
					System.out
							.println("Unable to settle edge from initializeGame");
				}
			}
		}
		
		// Resource Manager
		List<Bank> gameBanks = new ArrayList<>();
		for (int i = 0; i < game.getPlayers().size(); i++) {
			Player p = game.getPlayers().get(i);
			boolean hasLargestArmy = (p.getPlayerID() == game.getTurnTracker()
					.getLargestArmyHolder());
			boolean hasLongestRoad = (p.getPlayerID() == game.getTurnTracker()
					.getLongestRoadHolder());
			gameBanks.add(new Bank(p, hasLargestArmy, hasLongestRoad));
		}

		DevCardList mainBankDevCards = new DevCardList(game.getPlayers());
		boolean hasLargestArmy = (mainBankIndex == game.getTurnTracker()
				.getLargestArmyHolder());
		boolean hasLongestRoad = (mainBankIndex == game.getTurnTracker()
				.getLongestRoadHolder());
		gameBanks.add(new Bank(game.getBank(), mainBankDevCards,
				hasLargestArmy, hasLongestRoad));
		if (resourceManager == null) {
			resourceManager = new ResourceManager();
		}
		resourceManager.setGameBanks(gameBanks);

	}

	/**
	 * @author Curt
	 * @param gameID
	 *            = unique ID of a game in the server's games list
	 * @pre gameID matches an existing game
	 * @post Game will be terminated. Players will be evicted from game if still
	 *       inside.
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
	 *      successfully logged in user. There are exactly 4 players in the
	 *      lobby
	 * @post Game is added to the list of games on server
	 */
	public void createGame(List<Player> players, boolean randomTiles,
			boolean randomNumbers, boolean randomPorts, String name) {
		locationManager = new LocationManager();
		mapManager = new MapManager();
		resourceManager = new ResourceManager();
		game = new Game(players, name);

		List<Hex> hexes = new ArrayList<Hex>();

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

		ports.add(new Port(2, ResourceType.BRICK, new HexLocation(-2, 3)));
		ports.add(new Port(2, ResourceType.ORE, new HexLocation(1, -3)));
		ports.add(new Port(2, ResourceType.SHEEP, new HexLocation(3, -1)));
		ports.add(new Port(2, ResourceType.WHEAT, new HexLocation(-1, -2)));
		ports.add(new Port(2, ResourceType.WOOD, new HexLocation(-3, 2)));

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
				hexes.get(i).setRollValue(
						hexes.get(getRandomHex).getRollValue());
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
		for (Hex hex : hexes) {
			unsettledLocations.add(new Location(new VertexLocation(hex
					.getLocation(), VertexDirection.NorthEast)));
			unsettledLocations.add(new Location(new VertexLocation(hex
					.getLocation(), VertexDirection.NorthWest)));
			unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(),
					EdgeDirection.NorthWest)));
			unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(),
					EdgeDirection.North)));
			unsettledEdges.add(new Edge(new EdgeLocation(hex.getLocation(),
					EdgeDirection.NorthEast)));
		}

		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				2, 1), VertexDirection.NorthEast)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				2, 1), VertexDirection.NorthWest)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				1, 2), VertexDirection.NorthEast)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				1, 2), VertexDirection.NorthWest)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				0, 3), VertexDirection.NorthEast)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				0, 3), VertexDirection.NorthWest)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				-1, 3), VertexDirection.NorthEast)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				-1, 3), VertexDirection.NorthWest)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				-2, 3), VertexDirection.NorthEast)));
		unsettledLocations.add(new Location(new VertexLocation(new HexLocation(
				-2, 3), VertexDirection.NorthWest)));

		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(2, 1),
				EdgeDirection.North)));
		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(2, 1),
				EdgeDirection.NorthWest)));
		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(1, 2),
				EdgeDirection.North)));
		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(1, 2),
				EdgeDirection.NorthWest)));
		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(0, 3),
				EdgeDirection.North)));
		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-1, 3),
				EdgeDirection.North)));
		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-1, 3),
				EdgeDirection.NorthEast)));
		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-2, 3),
				EdgeDirection.North)));
		unsettledEdges.add(new Edge(new EdgeLocation(new HexLocation(-2, 3),
				EdgeDirection.NorthEast)));

		locationManager.setUnsettledLocations(unsettledLocations);
		locationManager.setUnsettledEdges(unsettledEdges);
	}

	public void joinGame(int playerUserID, String name, String color) {
		// TODO: interact better with the GUI to handle login
		if (!gameHasFourPlayers()) {
			Player p = new Player(playerUserID, name, color);
			p.setPlayerIndex(this.game.getPlayers().size());
			this.game.getPlayers().add(p);
		}
	}

	/**
	 * @author Curt
	 * @pre none
	 * @post returns boolean value for whether game has 4 players or not
	 */
	public boolean gameHasFourPlayers() {
		return game.getPlayers().size() == 4;
	}

	/**
	 * @author Curt
	 * @pre gameId matches an existing game. It's the start of a player's turn
	 * @post diceRollEvents from the MapManager are triggered.
	 */
	public void rollDice(int dieRoll) {

		// int dieRoll = randomness.nextInt(6) + randomness.nextInt(6) + 2;
		List<Hex> hexesProducingResources = mapManager
				.getTerrainResourceHexes(dieRoll);
		ResourceList gameBank = resourceManager.getGameBanks()
				.get(mainBankIndex).getResourcesCards();

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
				playersEarningResources.addAll(locationManager
						.awardTerrainResource(hexProducingResources
								.getLocation()));
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
				ResourceList resource = new ResourceList(resourceType);
				for (Integer earningPlayer : playersEarningResources) {
					resourceManager.transferResourceCard(mainBankIndex,
							earningPlayer, resource);
				}
			}

		}

	}

	public void diceIsSevenMoveRobber(HexLocation newLocationForRobber) {
		if (mapManager.moveRobber(newLocationForRobber)) {

			for (int i = 0; i < 4; i++) {
				int numberOfResourceCards = resourceManager.getGameBanks()
						.get(i).getResourcesCards().getTotalResources();
				if (numberOfResourceCards >= 7) {
					resourceManager.playerDiscardsHalfCards(i,
							new ResourceList());
				}
			}
		}
	}

	public boolean placeFreeRoad(EdgeLocation edge) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
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
	 * @param gameID
	 *            = unique ID of a game in the server's games list
	 * @param hex
	 *            = hex used to find the normalized vertex location where
	 *            settlement should be placed
	 * @param vertex
	 *            = vertex where the settlement should be placed
	 * @pre gameID matches existing game. Hex and VertexLocations are valid
	 *      location in this game's map. VertexLocation is settle-able.
	 * @post
	 */
	public void placeFirstSettlement(VertexLocation v) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
		locationManager.settleLocation(v, currentPlayer, true);
		playerBank.removeSettlement();
	}

	public void placeSecondSettlement(VertexLocation v) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		locationManager.settleLocation(v, currentPlayer, true);
		List<HexLocation> neighbors = locationManager
				.getHexLocationsAroundVertexLocation(v);
		ResourceList resourceList = new ResourceList(0, 0, 0, 0, 0);
		for (Hex hex : mapManager.getHexList()) {
			if (hex.equals(neighbors.get(0)) || hex.equals(neighbors.get(1))
					|| hex.equals(neighbors.get(2))) {
				if (hex.getResource() != null) {
					switch (hex.getResource()) {
					case ORE:
						resourceList.setOre(resourceList.getOre() + 1);
						break;
					case WOOD:
						resourceList.setWood(resourceList.getWood() + 1);
						break;
					case BRICK:
						resourceList.setBrick(resourceList.getBrick() + 1);
						break;
					case SHEEP:
						resourceList.setSheep(resourceList.getSheep() + 1);
						break;
					case WHEAT:
						resourceList.setWheat(resourceList.getWheat() + 1);
						break;
					default:
						break;
					}
				}
			}
		}
		resourceManager.transferResourceCard(mainBankIndex, currentPlayer,
				resourceList);
	}

	/**
	 * @author Curt
	 * @param gameID
	 *            = unique ID of a game in the server's games list
	 * @param object
	 *            = structure type
	 * @pre gameID matches an existing game. object matches a valid structure
	 * @post Success = Structure built, player victory point incremented,
	 *       structure count decremented.
	 */
	public boolean buildRoad(EdgeLocation edge) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
		ResourceList cost = new ResourceList(1, 0, 0, 0, 1);
		if (playerBank.getRoads() > 0
				&& playerBank.getResourcesCards().hasCardsAvailable(cost)) {
			resourceManager.transferResourceCard(currentPlayer, mainBankIndex,
					cost);
			resourceManager.placedRoad(currentPlayer);
			return locationManager.settleEdge(edge, currentPlayer);
		} else {
			return false;
		}
	}

	public boolean buildStructure(PieceType type, VertexLocation v) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
		ResourceList cost = new ResourceList();

		switch (type) {
		case SETTLEMENT:
			cost = new ResourceList(1, 0, 1, 1, 1);
			if (playerBank.getResourcesCards().hasCardsAvailable(cost)) {
				resourceManager.transferResourceCard(currentPlayer,
						mainBankIndex, cost);
				resourceManager.placedSettlement(currentPlayer);
				return locationManager.settleLocation(v, currentPlayer, false);
			} else {
				return false;
			}
		case CITY:
			cost = new ResourceList(0, 3, 0, 2, 0);
			if (playerBank.getResourcesCards().hasCardsAvailable(cost)) {
				resourceManager.transferResourceCard(currentPlayer,
						mainBankIndex, cost);
				resourceManager.placedCity(currentPlayer);
				return true;
			} else {
				return false;
			}
		default:
			return false;
		}
	}

	/**
	 * @author Curt
	 * @param gameID
	 *            = unique ID of a game in the server's games list
	 * @param card
	 *            = requested card type
	 * @pre gameId matches an existing game. Card is a valid cardType
	 * @post Success = Cards will be transferred between current player bank and
	 *       game bank.
	 */
	public boolean buyDevCard() {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
		ResourceList cost = new ResourceList(0, 1, 1, 1, 0);

		if (playerBank.getResourcesCards().hasCardsAvailable(cost)) {
			resourceManager.transferResourceCard(currentPlayer, mainBankIndex,
					cost);
			resourceManager.devCardBought(currentPlayer);
			return true;
		} else {
			return false;
		}
	}

	public boolean tradeOffer(int senderID, int receiverID, ResourceList offer,
			ResourceList request) {
		ResourceList senderResources = resourceManager.getGameBanks()
				.get(senderID).getResourcesCards();
		ResourceList receiverResources = resourceManager.getGameBanks()
				.get(receiverID).getResourcesCards();
		if (senderResources.hasCardsAvailable(offer)
				&& receiverResources.hasCardsAvailable(request)) {
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
	 * @param gameID
	 *            = unique ID of a game in the server's games list
	 * @param senderID
	 *            = unique ID of the current player
	 * @param receiverID
	 *            = unique ID of the trade recipient (game's bankID if maritime
	 *            trade)
	 * @param offer
	 *            = List of cards being offered by current player
	 * @param request
	 *            = List of cards requested from the trade recipient
	 * @pre gameID is valid. Sender and receiver are valid. Sender has all cards
	 *      in the offer available
	 * @post Failure = Receiver doesn't have the requested cards; Success =
	 *       cards are transferred between the two player's banks.
	 */
	public boolean tradeAccepted(int senderID, int receiverID,
			ResourceList offer, ResourceList request) {
		ResourceList senderResources = resourceManager.getGameBanks()
				.get(senderID).getResourcesCards();
		ResourceList receiverResources = resourceManager.getGameBanks()
				.get(receiverID).getResourcesCards();
		if (senderResources.hasCardsAvailable(offer)
				&& receiverResources.hasCardsAvailable(request)) {
			resourceManager.transferResourceCard(senderID, receiverID, offer);
			resourceManager.transferResourceCard(receiverID, senderID, request);
			return true;
		} else {
			return false;
		}
	}

	public void useMonopoly(ResourceType r) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		// TODO: use GUI to prompt user for Resource Type
		for (int i = 0; i < 4; i++) {
			if (i != currentPlayer) {
				ResourceList playerResources = resourceManager.getGameBanks()
						.get(i).getResourcesCards();
				ResourceList allResource = new ResourceList();
				switch (r) {
				case BRICK:
					allResource.setBrick(playerResources.getBrick());
					break;
				case ORE:
					allResource.setOre(playerResources.getOre());
					break;
				case WHEAT:
					allResource.setWheat(playerResources.getWheat());
					break;
				case SHEEP:
					allResource.setSheep(playerResources.getSheep());
					break;
				case WOOD:
					allResource.setWood(playerResources.getWood());
					break;
				default:
					return;
				}
				resourceManager.transferResourceCard(i, currentPlayer,
						allResource);
			}
		}
		resourceManager.monopolyUsed(currentPlayer);
	}

	public void useMonument() {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		Player p = game.getPlayers().get(currentPlayer);
		p.setVictoryPoints(p.getVictoryPoints() + 1);
		resourceManager.monumentUsed(currentPlayer);
	}

	public void useRoadBuilding(EdgeLocation road1, EdgeLocation road2) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		Bank b = resourceManager.getGameBanks().get(currentPlayer);
		if (b.getRoads() > 0 && road1 != null) {
			placeFreeRoad(road1);
		}

		if (b.getRoads() > 0 && road2 != null) {
			placeFreeRoad(road2);
		}

		resourceManager.roadBuildingUsed(currentPlayer);
	}

	public boolean useYearOfPlenty(ResourceType type1, ResourceType type2) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		ResourceList r = new ResourceList();
		r.add(type1, 1);
		r.add(type2, 1);
		ResourceList mainBankResources = resourceManager.getGameBanks()
				.get(mainBankIndex).getResourcesCards();
		if (mainBankResources.hasCardsAvailable(r)) {
			resourceManager.transferResourceCard(mainBankIndex, currentPlayer,
					r);
			resourceManager.yearOfPlentyUsed(currentPlayer);
			return true;
		} else {
			return false;
		}
	}

	public void useSoldier(HexLocation h) {
		int currentPlayer = game.getTurnTracker().getCurrentPlayerIndex();
		diceIsSevenMoveRobber(h);
		resourceManager.soldierUsed(currentPlayer);
	}

	/**
	 * @author Curt
	 * @param gameID
	 *            = unique ID of a game in the server's games list
	 * @pre gameID exists. Method only called after other turn-operations are
	 *      all completed (No trades ongoing, cards being played, etc)
	 * @post The current player pointer will move to the next player. All cards
	 *       in all player's hands will be marked as usable.
	 */
	public void endTurn() {
		game.getTurnTracker().nextTurn();
		// TODO: interact with GUI to disable for non-current player
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public ResourceManager getResourceManager(){
		return resourceManager;
	}
}
