package client.managers;

import shared.data.Bank;
import shared.data.DevCardList;
import shared.data.Edge;
import shared.data.Hex;
import shared.data.Location;
import shared.data.Port;
import shared.data.ResourceList;
import shared.data.Map;
import shared.data.VertexObject;
import javafx.geometry.VerticalDirection;
import shared.data.EdgeValue;
import shared.data.GameInfo;
import shared.data.PlayerInfo;
import shared.data.MessageList;
import shared.data.TurnTracker;
import shared.data.XYEdgeLocation;
import shared.data.SettlementLocation;

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
	 *         model
	 * @param game
	 * @pre model is accurate
	 * @post Will initialize the game model
	 */
	public void initializeGame(GameInfo game) {
		try {
			for (int i = 0; i < game.getMap().getSettlements().size(); i++) {
				int x = game.getMap().getSettlements().get(i).getLocation().getX();
				int y = game.getMap().getSettlements().get(i).getLocation().getY();
				VertexDirection dir = game.getMap().getSettlements().get(i).getLocation().getDirection();
				game.getMap().getSettlements().get(i).setDirection(new VertexLocation(new HexLocation(x, y), dir));
			}
			for (int i = 0; i < game.getMap().getCities().size(); i++) {
				int x = game.getMap().getCities().get(i).getLocation().getX();
				int y = game.getMap().getCities().get(i).getLocation().getY();
				VertexDirection dir = game.getMap().getCities().get(i).getLocation().getDirection();
				game.getMap().getCities().get(i).setDirection(new VertexLocation(new HexLocation(x, y), dir));
			}
			for (int i = 0; i < game.getMap().getRoads().size(); i++) {
				int x = game.getMap().getRoads().get(i).getLocation().getX();
				int y = game.getMap().getRoads().get(i).getLocation().getY();
				EdgeDirection dir = game.getMap().getRoads().get(i).getLocation().getDirection();
				game.getMap().getRoads().get(i).setLocation2(new EdgeLocation(new HexLocation(x, y), dir));
			}

			// our game
			setGame(game);

			Map map = game.getMap();

			if (mapManager == null) {
				mapManager = new MapManager();
			}

			HexLocation robberLocation = map.getRobber();

			List<Hex> hexList = map.getHexes();

			for (Hex hex : hexList) {
				if (hex.getLocation().equals(robberLocation)) {
					hex.setHasRobber(true);
				} else {
					hex.setHasRobber(false);
				}
			}
			mapManager.setHexList(hexList);

			if (locationManager == null) {
				locationManager = new LocationManager();
				createUnsettledAreas(hexList);
				locationManager.setPorts(map.getPorts());
			}

			for (VertexObject settlement : map.getSettlements()) {
				Location settlementLocation = null;
				for (Location location : locationManager.getUnsettledLocations()) {
					if (locationManager.getSettledEdges().size() == 8) {
						location.setWhoCanBuild(new HashSet<Integer>());
					}
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
			boolean hasLargestArmy = (-1 == game.getTurnTracker().getLargestArmy());
			boolean hasLongestRoad = (-1 == game.getTurnTracker().getLongestRoad());
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
	 *       inside.
	 */
	public void endGame() {
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
	public void createGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
		locationManager = new LocationManager();
		mapManager = new MapManager();
		resourceManager = new ResourceManager();
		game = new GameInfo(name);

		ResourceList bank = new ResourceList();
		bank.setBrick(19);
		bank.setOre(19);
		bank.setSheep(19);
		bank.setWheat(19);
		bank.setWood(19);
		game.setBank(bank);
		DevCardList deck = new DevCardList();
		deck.setMonopoly(2);
		deck.setMonument(5);
		deck.setRoadBuilding(2);
		deck.setSoldier(14);
		deck.setYearOfPlenty(2);
		game.setDeck(deck);
		game.setChat(new MessageList());
		game.setLog(new MessageList());
		game.setTradeOffer(null);
		TurnTracker ti = new TurnTracker();
		ti.setStatus("FirstRound");
		game.setTurnTracker(ti);
		game.setVersion(0);
		game.setWinner(-1);

		game.getPlayers().add(null);
		game.getPlayers().add(null);
		game.getPlayers().add(null);
		game.getPlayers().add(null);

		List<Hex> hexes = new ArrayList<Hex>();

		Hex robberHex = new Hex(new HexLocation(0, -2), 0, null);
		robberHex.setHasRobber(true);

		hexes.add(new Hex(new HexLocation(2, -2), 11, ResourceType.wood));
		hexes.add(new Hex(new HexLocation(2, -1), 12, ResourceType.sheep));
		hexes.add(new Hex(new HexLocation(2, 0), 9, ResourceType.wheat));

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

		hexes.add(new Hex(new HexLocation(-2, 0), 5, ResourceType.ore));
		hexes.add(new Hex(new HexLocation(-2, 1), 2, ResourceType.wheat));
		hexes.add(new Hex(new HexLocation(-2, 2), 6, ResourceType.wood));

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

				int originalNumber = hexes.get(i).getNumber();
				hexes.get(i).setNumber(hexes.get(getRandomHex).getNumber());
				hexes.get(getRandomHex).setNumber(originalNumber);

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

		mapManager.setHexList(hexes);

		createUnsettledAreas(hexes);

		locationManager.setPorts(ports);

		Map map = new Map();
		map.setHexes(hexes);
		map.setPorts(ports);
		map.setRoads(new ArrayList<EdgeValue>());
		map.setSettlements(new ArrayList<VertexObject>());
		map.setCities(new ArrayList<VertexObject>());
		map.setRobber(mapManager.getRobberLocation());

		game.setMap(map);

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

		for (Location l : unsettledLocations) {
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

	public void saveResourcesIntoGame() {
		for (int i = 0; i < game.getPlayers().size(); i++) {
			game.getPlayers().get(i).setResources(resourceManager.getGameBanks().get(i).getResourcesCards());
			game.getPlayers().get(i).setOldDevCards(resourceManager.getGameBanks().get(i).getDevelopmentCards());
			game.getPlayers().get(i).setNewDevCards(resourceManager.getGameBanks().get(i).getUnusableDevCards());
			game.getPlayers().get(i).setMonuments(resourceManager.getGameBanks().get(i).getMonuments());
			game.getPlayers().get(i).setRoads(resourceManager.getGameBanks().get(i).getRoads());
			game.getPlayers().get(i).setSettlements(resourceManager.getGameBanks().get(i).getSettlements());
			game.getPlayers().get(i).setCities(resourceManager.getGameBanks().get(i).getCities());
			game.getPlayers().get(i).setSoldiers(resourceManager.getGameBanks().get(i).getSoldiers());

		}
		game.setBank(resourceManager.getGameBanks().get(4).getResourcesCards());
		game.setDeck(resourceManager.getGameBanks().get(4).getDevelopmentCards());
	}

	/**
	 * @author Curt
	 * @pre gameId matches an existing game. It's the start of a player's turn
	 * @post diceRollEvents from the MapManager are triggered.
	 */
	public void rollDice(int dieRoll) {

		if (dieRoll == 7) {
			game.getTurnTracker().setStatus("Robbing");
			for (int i = 0; i < 4; i++) {
				if (7 <= resourceManager.getGameBanks().get(i).getResourcesCards().getTotalResources()
						&& game.getPlayers().get(i).getId() >= 0) {
					game.getTurnTracker().setStatus("Discarding");
				}
			}
			return;
		} else {
			game.getTurnTracker().setStatus("Playing");
		}
		// int dieRoll = randomness.nextInt(6) + randomness.nextInt(6) + 2;
		List<Hex> hexesProducingResources = mapManager.getTerrainResourceHexes(dieRoll);
		ResourceList gameBank = resourceManager.getGameBanks().get(mainBankIndex).getResourcesCards();

		for (Hex h : hexesProducingResources) {
			ResourceList player1Resources = new ResourceList();
			ResourceList player2Resources = new ResourceList();
			ResourceList player3Resources = new ResourceList();
			ResourceList player4Resources = new ResourceList();

			ResourceType type = h.getResource();
			List<Location> locationsTriggered = new ArrayList<Location>();
			locationsTriggered.add(new Location(new VertexLocation(h.getLocation(), VertexDirection.NW)));
			locationsTriggered.add(new Location(new VertexLocation(h.getLocation(), VertexDirection.NE)));
			locationsTriggered.add(new Location(new VertexLocation(h.getLocation(), VertexDirection.E)));
			locationsTriggered.add(new Location(new VertexLocation(h.getLocation(), VertexDirection.SE)));
			locationsTriggered.add(new Location(new VertexLocation(h.getLocation(), VertexDirection.SW)));
			locationsTriggered.add(new Location(new VertexLocation(h.getLocation(), VertexDirection.W)));

			for (Location loc : locationsTriggered) {
				for (Location settled : locationManager.getSettledLocations()) {
					if (loc.getNormalizedLocation().equals(settled.getNormalizedLocation())) {
						int ownerId = settled.getOwnerID();
						boolean isCity = false;
						for(VertexObject city : game.getMap().getCities()){
							if(city.getLocation().getX() == settled.getNormalizedLocation().getHexLoc().getX() 
									&& city.getLocation().getY() == settled.getNormalizedLocation().getHexLoc().getY()){
								isCity = true;
							}
						}
						switch (ownerId) {
						case 0:
							player1Resources.add(type, isCity ? 2 : 1);
							break;
						case 1:
							player2Resources.add(type, isCity ? 2 : 1);
							break;
						case 2:
							player3Resources.add(type, isCity ? 2 : 1);
							break;
						case 3:
							player4Resources.add(type, isCity ? 2 : 1);
							break;
						default:
							// do nothing
						}
					}
				}
			}

			ResourceList mainBank = resourceManager.getGameBanks().get(mainBankIndex).getResourcesCards();
			int amountNeeded = 0;
			int amountAvailable = 0;
			switch (type) {
			case brick:
				amountNeeded = player1Resources.getBrick() + player2Resources.getBrick() + player3Resources.getBrick()
						+ player4Resources.getBrick();
				amountAvailable = mainBank.getBrick();
				break;
			case ore:
				amountNeeded = player1Resources.getOre() + player2Resources.getOre() + player3Resources.getOre()
						+ player4Resources.getOre();
				amountAvailable = mainBank.getOre();
				break;
			case sheep:
				amountNeeded = player1Resources.getSheep() + player2Resources.getSheep() + player3Resources.getSheep()
						+ player4Resources.getSheep();
				amountAvailable = mainBank.getSheep();
				break;
			case wheat:
				amountNeeded = player1Resources.getWheat() + player2Resources.getWheat() + player3Resources.getWheat()
						+ player4Resources.getWheat();
				amountAvailable = mainBank.getWheat();
				break;
			case wood:
				amountNeeded = player1Resources.getWood() + player2Resources.getWood() + player3Resources.getWood()
						+ player4Resources.getWood();
				amountAvailable = mainBank.getWood();
				break;
			default:
				// do nothing
			}

			if (amountAvailable >= amountNeeded) {
				resourceManager.transferResourceCard(mainBankIndex, 0, player1Resources);
				resourceManager.transferResourceCard(mainBankIndex, 1, player2Resources);
				resourceManager.transferResourceCard(mainBankIndex, 2, player3Resources);
				resourceManager.transferResourceCard(mainBankIndex, 3, player4Resources);
			}
		}
		saveResourcesIntoGame();

		// for (ResourceType resourceType : ResourceType.values()) {
		// List<Hex> hexesProducingAParticularResource = new ArrayList<>();
		//
		// for (Hex particularHex : hexesProducingResources) {
		// if (resourceType == particularHex.getResource()) {
		// hexesProducingAParticularResource.add(particularHex);
		// }
		// }
		//
		// if (hexesProducingAParticularResource.isEmpty()) {
		// continue;
		// }
		//
		// // If a player shows up, add one resource to them
		// List<Integer> playersEarningResources = new ArrayList<>();
		//
		// for (Hex hexProducingResources : hexesProducingAParticularResource) {
		// playersEarningResources
		// .addAll(locationManager.awardTerrainResource(hexProducingResources.getLocation()));
		// }
		//
		// int amountAvailable = 0;
		// switch (resourceType) {
		// case ore:
		// amountAvailable = gameBank.getOre();
		// break;
		// case wood:
		// amountAvailable = gameBank.getWood();
		// break;
		// case brick:
		// amountAvailable = gameBank.getBrick();
		// break;
		// case sheep:
		// amountAvailable = gameBank.getSheep();
		// break;
		// case wheat:
		// amountAvailable = gameBank.getWheat();
		// break;
		// default:
		// break;
		// }
		//
		// if (amountAvailable >= playersEarningResources.size()) {
		// ResourceList resource = new ResourceList(resourceType);
		// for (Integer earningPlayer : playersEarningResources) {
		// resourceManager.transferResourceCard(mainBankIndex, earningPlayer,
		// resource);
		// }
		// }
		//
		// }
	}

	public boolean placeFreeRoad(EdgeLocation edge) {
		int currentPlayer = game.getTurnTracker().getCurrentTurn();
		Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
		if (playerBank.getRoads() > 0) {
			locationManager.settleEdge(edge, currentPlayer);
			resourceManager.placedRoad(currentPlayer);
			EdgeValue edgeValue = new EdgeValue();
			edgeValue.setOwner(currentPlayer);
			XYEdgeLocation xy = new XYEdgeLocation();
			xy.setX(edge.getHexLoc().getX());
			xy.setY(edge.getHexLoc().getY());
			xy.setDirection(edge.getDir());
			edgeValue.setLocation(xy);
			edgeValue.setLocation2(edge);
			game.getMap().getRoads().add(edgeValue);
			saveResourcesIntoGame();
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
		int currentPlayer = game.getTurnTracker().getCurrentTurn();
		Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
		locationManager.settleLocation(v, currentPlayer, true);
		resourceManager.placedSettlement(currentPlayer);
		VertexObject vertexObject = new VertexObject(currentPlayer, v);
		SettlementLocation settlementLocation = new SettlementLocation();
		settlementLocation.setDirection(v.getDir());
		settlementLocation.setX(v.getHexLoc().getX());
		settlementLocation.setY(v.getHexLoc().getY());
		vertexObject.setLocation(settlementLocation);
		game.getMap().getSettlements().add(vertexObject);
		game.getPlayers().get(currentPlayer).setVictoryPoints(game.getPlayers().get(currentPlayer).getVictoryPoints() + 1);
		saveResourcesIntoGame();
	}

	public void placeSecondSettlement(VertexLocation v) {
		int currentPlayer = game.getTurnTracker().getCurrentTurn();
		locationManager.settleLocation(v, currentPlayer, true);
		resourceManager.placedSettlement(currentPlayer);
		VertexObject vertexObject = new VertexObject(currentPlayer, v);
		SettlementLocation settlementLocation = new SettlementLocation();
		settlementLocation.setDirection(v.getDir());
		settlementLocation.setX(v.getHexLoc().getX());
		settlementLocation.setY(v.getHexLoc().getY());
		vertexObject.setLocation(settlementLocation);
		game.getMap().getSettlements().add(vertexObject);
		game.getPlayers().get(currentPlayer).setVictoryPoints(game.getPlayers().get(currentPlayer).getVictoryPoints() + 1);
		List<HexLocation> neighbors = locationManager.getHexLocationsAroundVertexLocation(v);
		ResourceList resourceList = new ResourceList(0, 0, 0, 0, 0);
		for (Hex hex : mapManager.getHexList()) {
			if (hex.getLocation().equals(neighbors.get(0)) || hex.getLocation().equals(neighbors.get(1)) || hex.getLocation().equals(neighbors.get(2))) {
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
		saveResourcesIntoGame();
	}

	public boolean canBuildRoad() {
		ResourceList resList = resourceManager.getGameBanks().get(currentPlayer()).getResourcesCards();
		Bank bank = resourceManager.getGameBanks().get(currentPlayer());
		return resList.getWood() > 0 && resList.getBrick() > 0 && bank.getRoads() > 0;
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		boolean canPlaceRoad = false;
		if (locationManager.getSettledEdges().size() > 4 && locationManager.getSettledEdges().size() < 8) {
			Edge myEdge = null;
			ArrayList<Location> mySettledLocations = new ArrayList<Location>();
			for (Location l : locationManager.getSettledLocations()) {
				if (l.getOwnerID() == currentPlayer()) {
					mySettledLocations.add(l);
				}
			}
			for (Edge edge : locationManager.getSettledEdges()) {
				if (edge.getOwnerId() == currentPlayer()) {
					myEdge = edge;
				}
			}

			if (myEdge == null) {
				assert false;
			}

			EdgeLocation e1;
			EdgeLocation e2;
			EdgeLocation e3;
			for (int i = 0; i < mySettledLocations.size(); i++) {
				if (mySettledLocations.get(i).getNormalizedLocation().getDir() == VertexDirection.NW) {
					e1 = new EdgeLocation(mySettledLocations.get(i).getNormalizedLocation().getHexLoc(),
							EdgeDirection.N);
					e2 = new EdgeLocation(mySettledLocations.get(i).getNormalizedLocation().getHexLoc(),
							EdgeDirection.NW);
					e3 = new EdgeLocation(
							new HexLocation(mySettledLocations.get(i).getNormalizedLocation().getHexLoc().getX() - 1,
									mySettledLocations.get(i).getNormalizedLocation().getHexLoc().getY()),
							EdgeDirection.NE);
				} else {
					e1 = new EdgeLocation(mySettledLocations.get(i).getNormalizedLocation().getHexLoc(),
							EdgeDirection.N);
					e2 = new EdgeLocation(mySettledLocations.get(i).getNormalizedLocation().getHexLoc(),
							EdgeDirection.NE);
					e3 = new EdgeLocation(
							new HexLocation(mySettledLocations.get(i).getNormalizedLocation().getHexLoc().getX() + 1,
									mySettledLocations.get(i).getNormalizedLocation().getHexLoc().getY() - 1),
							EdgeDirection.NW);
				}
				if (myEdge.getEdgeLocation().getNormalizedLocation().equals(e1)
						|| myEdge.getEdgeLocation().getNormalizedLocation().equals(e2)
						|| myEdge.getEdgeLocation().getNormalizedLocation().equals(e3)) {
					continue;
				}

				if (edgeLoc.equals(e1) || edgeLoc.equals(e2) || edgeLoc.equals(e3)) {
					return true;
				}
				return false;
			}
			return false;
		} else {
			for (Edge edge : locationManager.getUnsettledEdges()) {
				if (edge.getEdgeLocation().equals(edgeLoc)) {
					if (edge.getWhoCanBuild().contains(currentPlayer())) {
						canPlaceRoad = true;
						break;
					}
				}
			}
			return canPlaceRoad;
		}
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
		int currentPlayer = game.getTurnTracker().getCurrentTurn();
		Bank playerBank = resourceManager.getGameBanks().get(currentPlayer);
		ResourceList cost = new ResourceList(1, 0, 0, 0, 1);
		if (playerBank.getRoads() > 0 && playerBank.getResourcesCards().hasCardsAvailable(cost)) {
			resourceManager.transferResourceCard(currentPlayer, mainBankIndex, cost);
			resourceManager.placedRoad(currentPlayer);
			boolean builtRoad = locationManager.settleEdge(edge, currentPlayer);

			if (builtRoad) {
				if (game.getTurnTracker().getLongestRoad() == -1) {
					if (5 <= 14 - resourceManager.getGameBanks().get(currentPlayer).getRoads()) {
						game.getTurnTracker().setLongestRoad(currentPlayer);
						game.getPlayers().get(currentPlayer)
								.setVictoryPoints(game.getPlayers().get(currentPlayer).getVictoryPoints() + 2);
					}
				} else {
					int roadsPreviousHolderHas = resourceManager.getGameBanks()
							.get(game.getTurnTracker().getLongestRoad()).getRoads();
					int roadsCurrentPlayerHas = resourceManager.getGameBanks().get(currentPlayer).getRoads();
					if (roadsCurrentPlayerHas < roadsPreviousHolderHas) {
						game.getPlayers().get(currentPlayer)
								.setVictoryPoints(game.getPlayers().get(currentPlayer).getVictoryPoints() + 2);
						game.getPlayers().get(game.getTurnTracker().getLongestRoad()).setVictoryPoints(
								game.getPlayers().get(game.getTurnTracker().getLongestRoad()).getVictoryPoints() - 2);
						game.getTurnTracker().setLongestRoad(currentPlayer);
					}
				}

				EdgeValue edgeValue = new EdgeValue();
				edgeValue.setOwner(currentPlayer);

				XYEdgeLocation xy = new XYEdgeLocation();

				xy.setX(edge.getHexLoc().getX());
				xy.setY(edge.getHexLoc().getY());
				xy.setDirection(edge.getDir());

				edgeValue.setLocation(xy);
				edgeValue.setLocation2(edge);

				game.getMap().getRoads().add(edgeValue);
				saveResourcesIntoGame();

			}
			return builtRoad;
		} else {
			return false;
		}
	}

	public boolean canBuildSettlement() {
		ResourceList resList = resourceManager.getGameBanks().get(currentPlayer()).getResourcesCards();
		Bank bank = resourceManager.getGameBanks().get(currentPlayer());
		return resList.getBrick() > 0 && resList.getSheep() > 0 && resList.getWheat() > 0 && resList.getWood() > 0
				&& bank.getSettlements() > 0;
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {

		boolean canPlaceSettlement = false;
		int i = 0;
		for (Location lc : locationManager.getUnsettledLocations()) {
			if (lc.getNormalizedLocation().equals(vertLoc)) {

				if (lc.getWhoCanBuild().contains(currentPlayer()) && lc.getCanBeSettled()) {
					canPlaceSettlement = true;
					i++;
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
				boolean builtSettlement = locationManager.settleLocation(v, currentPlayer, false);
				if (builtSettlement) {
					VertexObject vertexObject = new VertexObject(currentPlayer, v);
					SettlementLocation settlementLocation = new SettlementLocation();
					settlementLocation.setDirection(v.getDir());
					settlementLocation.setX(v.getHexLoc().getX());
					settlementLocation.setY(v.getHexLoc().getY());
					vertexObject.setLocation(settlementLocation);
					game.getMap().getSettlements().add(vertexObject);
					game.getPlayers().get(currentPlayer)
							.setVictoryPoints(game.getPlayers().get(currentPlayer).getVictoryPoints() + 1);
					saveResourcesIntoGame();
				}
				return builtSettlement;
			} else {
				return false;
			}
		case CITY:
			cost = new ResourceList(0, 3, 0, 2, 0);
			if (playerBank.getResourcesCards().hasCardsAvailable(cost)) {
				resourceManager.transferResourceCard(currentPlayer, mainBankIndex, cost);
				resourceManager.placedCity(currentPlayer);
				boolean builtCity = locationManager.upgradeToCity(v);
				if (builtCity) {
					VertexObject vertexObject = new VertexObject(currentPlayer, v);
					SettlementLocation settlementLocation = new SettlementLocation();
					settlementLocation.setDirection(v.getDir());
					settlementLocation.setX(v.getHexLoc().getX());
					settlementLocation.setY(v.getHexLoc().getY());
					vertexObject.setLocation(settlementLocation);
					game.getMap().getSettlements().remove(vertexObject);
					game.getMap().getCities().add(vertexObject);
					game.getPlayers().get(currentPlayer)
							.setVictoryPoints(game.getPlayers().get(currentPlayer).getVictoryPoints() + 1);
					saveResourcesIntoGame();
				}
				return builtCity;
			} else {
				return false;
			}
		default:
			return false;
		}
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		for (Hex hex : mapManager.getHexList()) {
			if (hex.getLocation().equals(hexLoc)) {
				if (!hex.getHasRobber()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean canRobPlayer(HexLocation hexLoc, int playerId) {
		for (Location location : locationManager.getSettledLocations()) {
			if (location.getOwnerID() == playerId) {
				List<HexLocation> hexes = locationManager
						.getHexLocationsAroundVertexLocation(location.getNormalizedLocation());
				for (HexLocation hex : hexes) {
					if (hexLoc.equals(hex)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void placeRobber(HexLocation hexLocation) {
		game.getMap().setRobber(hexLocation);
		saveResourcesIntoGame();
	}

	public boolean canBuyCard() {
		ResourceList resList = resourceManager.getGameBanks().get(currentPlayer()).getResourcesCards();
		if (game.getDeck().totalCardsRemaining() <= 0) {
			return false;
		}
		return resList.getOre() > 0 && resList.getSheep() > 0 && resList.getWheat() > 0;
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
	public DevCardType buyDevCard() {
		int currentPlayer = game.getTurnTracker().getCurrentTurn();
		ResourceList cost = new ResourceList(0, 1, 1, 1, 0);

		resourceManager.transferResourceCard(currentPlayer, mainBankIndex, cost);
		DevCardType dev = resourceManager.devCardBought(currentPlayer);
		saveResourcesIntoGame();
		return dev;
	}

	public boolean tradeOffer(int senderID, int receiverID, ResourceList offer, ResourceList request) {
		ResourceList senderResources = resourceManager.getGameBanks().get(senderID).getResourcesCards();
		ResourceList receiverResources = resourceManager.getGameBanks().get(receiverID).getResourcesCards();
		if (senderResources.hasCardsAvailable(offer) && receiverResources.hasCardsAvailable(request)) {
			if (receiverID == mainBankIndex) {
				tradeAccepted(senderID, receiverID, offer, request);
				saveResourcesIntoGame();
				return true;
			} else {
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
	public boolean tradeAccepted(int senderID, int receiverID, ResourceList offer, ResourceList request) {
		ResourceList senderResources = resourceManager.getGameBanks().get(senderID).getResourcesCards();
		ResourceList receiverResources = resourceManager.getGameBanks().get(receiverID).getResourcesCards();
		if (senderResources.hasCardsAvailable(offer) && receiverResources.hasCardsAvailable(request)) {
			resourceManager.transferResourceCard(senderID, receiverID, offer);
			resourceManager.transferResourceCard(receiverID, senderID, request);
			saveResourcesIntoGame();
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
		getGame().getPlayers().get(currentPlayer).setPlayedDevCard(true);
		saveResourcesIntoGame();
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
		saveResourcesIntoGame();
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
		getGame().getPlayers().get(currentPlayer).setPlayedDevCard(true);
		saveResourcesIntoGame();
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
			getGame().getPlayers().get(currentPlayer).setPlayedDevCard(true);
			saveResourcesIntoGame();
			return true;
		} else {
			return false;
		}
	}

	public boolean canUseSoldier() {
		DevCardList devList = resourceManager.getGameBanks().get(currentPlayer()).getDevelopmentCards();
		return devList.getSoldier() > 0;
	}

	public void useSoldier() {
		int currentPlayer = game.getTurnTracker().getCurrentTurn();
		resourceManager.soldierUsed(currentPlayer);

		getGame().getPlayers().get(currentPlayer).setPlayedDevCard(true);

		if (game.getTurnTracker().getLargestArmy() == -1) {
			if (3 <= resourceManager.getGameBanks().get(currentPlayer).getSoldiers()) {
				game.getPlayers().get(currentPlayer)
						.setVictoryPoints(game.getPlayers().get(currentPlayer).getVictoryPoints() + 2);
				game.getTurnTracker().setLargestArmy(currentPlayer);
			}
		} else {
			if (resourceManager.getGameBanks().get(game.getTurnTracker().getLargestArmy())
					.getSoldiers() < resourceManager.getGameBanks().get(currentPlayer).getSoldiers()) {
				game.getPlayers().get(currentPlayer)
						.setVictoryPoints(game.getPlayers().get(currentPlayer).getVictoryPoints() + 2);
				game.getPlayers().get(game.getTurnTracker().getLargestArmy()).setVictoryPoints(
						game.getPlayers().get(game.getTurnTracker().getLargestArmy()).getVictoryPoints() - 2);
				game.getTurnTracker().setLargestArmy(currentPlayer);
			}
		}
		saveResourcesIntoGame();
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
		resourceManager.makeCardsUsable();
		if (game.getTurnTracker().getStatus() == "SecondRound") {
			for (Location l : locationManager.getUnsettledLocations()) {
				l.setWhoCanBuild(new HashSet<Integer>());
			}
		}
		getGame().getPlayers().get(currentPlayer()).setPlayedDevCard(true);
		saveResourcesIntoGame();
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
