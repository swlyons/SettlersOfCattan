package server.command;

import shared.locations.*;
import shared.data.*;
import shared.model.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import client.managers.GameManager;
import server.receiver.AllOfOurInformation;
import shared.data.PlayerInfo;
import shared.data.SettlementLocation;
import shared.data.User;
import shared.data.XYEdgeLocation;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.model.AcceptTrade;
import shared.model.BuildRoad;
import shared.model.BuildSettlement;
import shared.model.Road_Building;
import shared.model.BuyDevCard;

public class CommandTest {

	private int testGameID;

	public CommandTest() {

	}

	@BeforeClass
	public static void setUpClass() {

	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		if (AllOfOurInformation.getSingleton().getUsers().size() < 4) {
			for (int i = AllOfOurInformation.getSingleton().getUsers().size(); i < 4; i++) {
				User u = new User("test" + i, "password");
				RegisterCommand register = new RegisterCommand(u);
				assertTrue(register.execute());
			}
		}
		assertTrue(AllOfOurInformation.getSingleton().getUsers().size() >= 4);

		CreateGameCommand setUp = new CreateGameCommand(false, false, false, "test setUp game");
		setUp.execute();
		assertTrue(AllOfOurInformation.getSingleton().getGames().size() >= 0);

		GameManager testGame = AllOfOurInformation.getSingleton().getGames()
				.get(AllOfOurInformation.getSingleton().getGames().size() - 1);
		testGameID = testGame.getGame().getId();
		for (int i = 0; i < 4; i++) {
			PlayerInfo p = new PlayerInfo();
			p.setPlayerIndex(i);
			p.setPlayerID(i);
			if (i == 0)
				p.setColor("Purple");
			else if (i == 1)
				p.setColor("Blue");
			else if (i == 2)
				p.setColor("Green");
			else if (i == 3)
				p.setColor("Brown");

			testGame.getGame().getPlayers().add(p);
		}

		SettlementLocation location = new SettlementLocation();
		XYEdgeLocation roadLoc = new XYEdgeLocation();

		// p1's first 2 turns stuff
		BuildSettlement p1Settlement1 = new BuildSettlement();
		BuildRoad p1Road1 = new BuildRoad();
		BuildSettlement p1Settlement2 = new BuildSettlement();
		BuildRoad p1Road2 = new BuildRoad();
		p1Settlement1.setFree(true);
		p1Settlement1.setGameId(testGameID);
		p1Settlement1.setPlayerIndex(0);
		p1Settlement1.setType("buildSettlement");
		location.setX(-2);
		location.setY(1);
		location.setDirection(VertexDirection.NE);
		p1Settlement1.setVertexLocation(location);
		BuildSettlementCommand p1Settle1 = new BuildSettlementCommand(p1Settlement1);
		p1Settle1.execute();

		p1Settlement2.setFree(true);
		p1Settlement2.setGameId(testGameID);
		p1Settlement2.setPlayerIndex(0);
		p1Settlement2.setType("buildSettlement");
		location.setX(-1);
		location.setY(0);
		location.setDirection(VertexDirection.NE);
		p1Settlement2.setVertexLocation(location);
		BuildSettlementCommand p1Settle2 = new BuildSettlementCommand(p1Settlement2);
		p1Settle2.execute();

		p1Road1.setFree(true);
		p1Road1.setGameId(testGameID);
		p1Road1.setPlayerIndex(0);
		p1Road1.setType("buildRoad");
		roadLoc.setDirection(EdgeDirection.NW);
		roadLoc.setX(-1);
		roadLoc.setY(0);
		p1Road1.setRoadLocation(roadLoc);
		BuildRoadCommand p1BldRoad1 = new BuildRoadCommand(p1Road1);
		p1BldRoad1.execute();

		p1Road2.setFree(true);
		p1Road2.setGameId(testGameID);
		p1Road2.setPlayerIndex(0);
		p1Road2.setType("buildRoad");
		roadLoc.setDirection(EdgeDirection.N);
		roadLoc.setX(-1);
		roadLoc.setY(0);
		p1Road2.setRoadLocation(roadLoc);
		BuildRoadCommand p1BldRoad2 = new BuildRoadCommand(p1Road2);
		p1BldRoad2.execute();

		// p2's first 2 turns stuff
		BuildSettlement p2Settlement1 = new BuildSettlement();
		BuildRoad p2Road1 = new BuildRoad();
		BuildSettlement p2Settlement2 = new BuildSettlement();
		BuildRoad p2Road2 = new BuildRoad();
		p2Settlement1.setFree(true);
		p2Settlement1.setGameId(testGameID);
		p2Settlement1.setPlayerIndex(0);
		p2Settlement1.setType("buildSettlement");
		location.setX(-2);
		location.setY(2);
		location.setDirection(VertexDirection.NE);
		p2Settlement1.setVertexLocation(location);
		BuildSettlementCommand p2Settle1 = new BuildSettlementCommand(p2Settlement1);
		p2Settle1.execute();

		p2Settlement2.setFree(true);
		p2Settlement2.setGameId(testGameID);
		p2Settlement2.setPlayerIndex(0);
		p2Settlement2.setType("buildSettlement");
		location.setX(-1);
		location.setY(2);
		location.setDirection(VertexDirection.NE);
		p2Settlement2.setVertexLocation(location);
		BuildSettlementCommand p2Settle2 = new BuildSettlementCommand(p2Settlement2);
		p2Settle2.execute();

		p2Road1.setFree(true);
		p2Road1.setGameId(testGameID);
		p2Road1.setPlayerIndex(0);
		p2Road1.setType("buildRoad");
		roadLoc.setDirection(EdgeDirection.NE);
		roadLoc.setX(-2);
		roadLoc.setY(2);
		p2Road1.setRoadLocation(roadLoc);
		BuildRoadCommand p2BldRoad1 = new BuildRoadCommand(p2Road1);
		p2BldRoad1.execute();

		p2Road2.setFree(true);
		p2Road2.setGameId(testGameID);
		p2Road2.setPlayerIndex(0);
		p2Road2.setType("buildRoad");
		roadLoc.setDirection(EdgeDirection.N);
		roadLoc.setX(-1);
		roadLoc.setY(2);
		p2Road2.setRoadLocation(roadLoc);
		BuildRoadCommand p2BldRoad2 = new BuildRoadCommand(p2Road2);
		p2BldRoad2.execute();

		// p3's first 2 turns stuff
		BuildSettlement p3Settlement1 = new BuildSettlement();
		BuildRoad p3Road1 = new BuildRoad();
		BuildSettlement p3Settlement2 = new BuildSettlement();
		BuildRoad p3Road2 = new BuildRoad();
		p3Settlement1.setFree(true);
		p3Settlement1.setGameId(testGameID);
		p3Settlement1.setPlayerIndex(0);
		p3Settlement1.setType("buildSettlement");
		location.setX(1);
		location.setY(-1);
		location.setDirection(VertexDirection.NW);
		p3Settlement1.setVertexLocation(location);
		BuildSettlementCommand p3Settle1 = new BuildSettlementCommand(p3Settlement1);
		p3Settle1.execute();

		p3Settlement2.setFree(true);
		p3Settlement2.setGameId(testGameID);
		p3Settlement2.setPlayerIndex(0);
		p3Settlement2.setType("buildSettlement");
		location.setX(1);
		location.setY(-1);
		location.setDirection(VertexDirection.E);
		p3Settlement2.setVertexLocation(location);
		BuildSettlementCommand p3Settle2 = new BuildSettlementCommand(p3Settlement2);
		p3Settle2.execute();

		p3Road1.setFree(true);
		p3Road1.setGameId(testGameID);
		p3Road1.setPlayerIndex(0);
		p3Road1.setType("buildRoad");
		roadLoc.setDirection(EdgeDirection.N);
		roadLoc.setX(1);
		roadLoc.setY(-1);
		p3Road1.setRoadLocation(roadLoc);
		BuildRoadCommand p3BldRoad1 = new BuildRoadCommand(p3Road1);
		p3BldRoad1.execute();

		p3Road2.setFree(true);
		p3Road2.setGameId(testGameID);
		p3Road2.setPlayerIndex(0);
		p3Road2.setType("buildRoad");
		roadLoc.setDirection(EdgeDirection.NE);
		roadLoc.setX(1);
		roadLoc.setY(-1);
		p3Road2.setRoadLocation(roadLoc);
		BuildRoadCommand p3BldRoad2 = new BuildRoadCommand(p3Road2);
		p3BldRoad2.execute();

		// p4's first 2 turns stuff
		BuildSettlement p4Settlement1 = new BuildSettlement();
		BuildRoad p4Road1 = new BuildRoad();
		BuildSettlement p4Settlement2 = new BuildSettlement();
		BuildRoad p4Road2 = new BuildRoad();
		p4Settlement1.setFree(true);
		p4Settlement1.setGameId(testGameID);
		p4Settlement1.setPlayerIndex(0);
		p4Settlement1.setType("buildSettlement");
		location.setX(1);
		location.setY(0);
		location.setDirection(VertexDirection.E);
		p4Settlement1.setVertexLocation(location);
		BuildSettlementCommand p4Settle1 = new BuildSettlementCommand(p4Settlement1);
		p4Settle1.execute();

		p4Settlement2.setFree(true);
		p4Settlement2.setGameId(testGameID);
		p4Settlement2.setPlayerIndex(0);
		p4Settlement2.setType("buildSettlement");
		location.setX(1);
		location.setY(0);
		location.setDirection(VertexDirection.SW);
		p4Settlement2.setVertexLocation(location);
		BuildSettlementCommand p4Settle2 = new BuildSettlementCommand(p4Settlement2);
		p4Settle2.execute();

		p1Road1.setFree(true);
		p1Road1.setGameId(testGameID);
		p1Road1.setPlayerIndex(0);
		p1Road1.setType("buildRoad");
		roadLoc.setDirection(EdgeDirection.SE);
		roadLoc.setX(1);
		roadLoc.setY(0);
		p4Road1.setRoadLocation(roadLoc);
		BuildRoadCommand p4BldRoad1 = new BuildRoadCommand(p4Road1);
		p4BldRoad1.execute();

		p4Road2.setFree(true);
		p4Road2.setGameId(testGameID);
		p4Road2.setPlayerIndex(0);
		p4Road2.setType("buildRoad");
		roadLoc.setDirection(EdgeDirection.S);
		roadLoc.setX(1);
		roadLoc.setY(0);
		p4Road2.setRoadLocation(roadLoc);
		BuildRoadCommand p4BldRoad2 = new BuildRoadCommand(p4Road2);
		p4BldRoad2.execute();
	}

	@After
	public void tearDown() {
	}

	// Inserting some comments to help prevent future merge conflicts.
	// We will need to remove them after we are done.

	// TODO: put tests 1-6 here
	@Test
	public void CreateGameCommandTest() {
		CreateGameCommand command;

		command = new CreateGameCommand(false, false, false, "test game");
		assertTrue(command != null);
		assertTrue(command.execute());

		command = new CreateGameCommand(true, false, false, "test game");
		assertTrue(command != null);
		assertTrue(command.execute());

		command = new CreateGameCommand(false, true, false, "test game");
		assertTrue(command != null);
		assertTrue(command.execute());

		command = new CreateGameCommand(false, false, true, "test game");
		assertTrue(command != null);
		assertTrue(command.execute());

		command = new CreateGameCommand(true, true, false, "test game");
		assertTrue(command != null);
		assertTrue(command.execute());

		command = new CreateGameCommand(true, false, true, "test game");
		assertTrue(command != null);
		assertTrue(command.execute());

		command = new CreateGameCommand(true, true, true, "test game");
		assertTrue(command != null);
		assertTrue(command.execute());
	}

	@Test
	public void AcceptTradeCommandTest() {
		CreateGameCommand testGame = new CreateGameCommand(false, false, false, "test game");
		testGame.execute();

		// Try some cases where a trade hasn't been offered.
		AcceptTrade acceptTrade = new AcceptTrade();
		acceptTrade.setGameId(testGame.getGameId());
		acceptTrade.setWillAccept(false);
		AcceptTradeCommand command = new AcceptTradeCommand(acceptTrade);
		assertTrue(command != null);
		assertFalse(command.execute());

		acceptTrade.setWillAccept(true);
		command = new AcceptTradeCommand(acceptTrade);
		assertTrue(command != null);
		assertFalse(command.execute());
	}

	@Test
	public void BuildRoadCommandTest() {
		CreateGameCommand testGame = new CreateGameCommand(false, false, false, "test game");
		testGame.execute();

		XYEdgeLocation roadLocation = new XYEdgeLocation();
		roadLocation.setX(0);
		roadLocation.setY(0);
		roadLocation.setDirection(EdgeDirection.NW);
		BuildRoad buildRoad = new BuildRoad();
		buildRoad.setFree(true);
		buildRoad.setRoadLocation(roadLocation);
		buildRoad.setGameId(testGame.getGameId());
		BuildRoadCommand command = new BuildRoadCommand(buildRoad);
		assertTrue(command != null);
		assertFalse(command.execute());

		roadLocation.setX(1);
		buildRoad.setFree(false);
		buildRoad.setRoadLocation(roadLocation);
		command = new BuildRoadCommand(buildRoad);
		assertTrue(command != null);
		assertFalse(command.execute());
	}

	@Test
	public void BuildSettlementCommandTest() {
		CreateGameCommand testGame = new CreateGameCommand(false, false, false, "test game");
		testGame.execute();

		SettlementLocation vertexLocation = new SettlementLocation();
		vertexLocation.setX(0);
		vertexLocation.setY(0);
		vertexLocation.setDirection(VertexDirection.E);
		BuildSettlement buildSettlement = new BuildSettlement();
		buildSettlement.setVertexLocation(vertexLocation);
		buildSettlement.setFree(true);
		buildSettlement.setGameId(testGame.getGameId());
		buildSettlement.setPlayerIndex(0);
		BuildSettlementCommand command = new BuildSettlementCommand(buildSettlement);
		assertTrue(command != null);
		assertFalse(command.execute());
	}

	@Test
	public void BuildCityCommandTest() {
		CreateGameCommand testGame = new CreateGameCommand(false, false, false, "test game");
		testGame.execute();

		SettlementLocation vertexLocation = new SettlementLocation();
		vertexLocation.setX(0);
		vertexLocation.setY(0);
		vertexLocation.setDirection(VertexDirection.E);
		BuildCity buildCity = new BuildCity(0);
		buildCity.setVertexLocation(vertexLocation);
		buildCity.setGameId(testGame.getGameId());
		BuildCityCommand command = new BuildCityCommand(buildCity);
		assertTrue(command != null);
		assertFalse(command.execute());
	}

	@Test
	public void BuyDevCardCommandTest() {
		CreateGameCommand testGame = new CreateGameCommand(false, false, false, "test game");
		testGame.execute();

		BuyDevCard buyDevCard = new BuyDevCard(0);
		buyDevCard.setGameId(testGame.getGameId());
		BuyDevCardCommand command = new BuyDevCardCommand(buyDevCard);
		assertTrue(command != null);
		assertFalse(command.execute());
	}

	@Test
	public void RoadBuildingCommandTest() {
		Road_Building input = new Road_Building(0);
		input.setGameId(testGameID);
		input.setPlayerIndex(0);
		input.setSpot1(new EdgeLocation(new HexLocation(-1, 0), EdgeDirection.NE));
		input.setSpot2(new EdgeLocation(new HexLocation(-1, 0), EdgeDirection.SW));
		input.setType("roadBuilding");
		RoadBuildingCommand command = new RoadBuildingCommand(input);
		assertTrue(command != null);
		assertFalse(command.execute());
	}
	
	@Test
	public void RobPlayerCommandTest() {
		RobPlayer input = new RobPlayer();
		input.setGameId(testGameID);
		input.setPlayerIndex(0);
		input.setLocation(new HexLocation(-1,1));
		input.setVictimIndex(1);
		input.setType("robPlayer");
		RobPlayerCommand command = new RobPlayerCommand(input);
		assertTrue(command != null);
		assertFalse(command.execute());		
	}
	
	@Test
	public void RollNumberCommandTest() {
		RollNumber input = new RollNumber();
		input.setGameId(testGameID);
		input.setPlayerIndex(0);
		input.setNumber(2);
		input.setType("rollNumber");
		RollNumberCommand command = new RollNumberCommand(input);
		assertTrue(command != null);
		assertFalse(command.execute());			
	}

	@Test
	public void SendChatCommandTest() {
		SendChat input = new SendChat(0);
		input.setPlayerIndex(0);
		input.setContent("Hi");
		input.setType("sendChat");
		SendChatCommand command = new SendChatCommand(1, "hi", testGameID);
		assertTrue(command != null);
	}

	@Test
	public void SoldierCommandTest() {
		Soldier input = new Soldier(0);
		input.setGameId(testGameID);
		input.setLocation(new HexLocation(-1,1));
		input.setVictimIndex(1);
		input.setType("soldier");
		SoldierCommand command = new SoldierCommand(input);
		assertTrue(command != null);
		assertFalse(command.execute());	
	}
	
	@Test
	public void YearOfPlentyCommandTest() {
		Year_Of_Plenty input = new Year_Of_Plenty(0);
		input.setGameId(testGameID);
		input.setResource1(ResourceType.wood);
		input.setResource1(ResourceType.ore);		
		input.setType("year_of_plenty");
		YearOfPlentyCommand command = new YearOfPlentyCommand(input);
		assertTrue(command != null);
		assertFalse(command.execute());
	}
	@Test
	public void DiscardCardsCommandTest() {
		DiscardCards input = new DiscardCards();
		input.setGameId(0);
		input.setPlayerIndex(0);		
		DiscardCardsCommand command = new DiscardCardsCommand(input);
                if(command.execute()){
                    assertTrue(AllOfOurInformation.getSingleton().getGames().get(0).getGame().getTurnTracker().getStatus().equals("Robbing"));
                }
		
	}
	
	@Test
	public void FinishTurnCommandTest() {
                //set the current turn to the first player
                FinishMove input = new FinishMove(0);
		input.setGameId(testGameID);
		input.setPlayerIndex(0);
                input.setType("FinishTurn");
		FinishTurnCommand command = new FinishTurnCommand(input);
                try{
                    assertFalse(command.execute());
                }
                catch(NullPointerException npe){
                        assertFalse(true);
                }
	}
	
	@Test
	public void MaritimeTradeCommandTest() {
		MaritimeTrade input = new MaritimeTrade();
		input.setGameId(testGameID);
		input.setPlayerIndex(0);
		input.setRatio(4);
                input.setInputResource(ResourceType.ore);
                input.setOutputResource(ResourceType.wood);
		MaritimeTradeCommand command = new MaritimeTradeCommand(input);
		assertTrue(command != null);
		assertFalse(command.execute());			
	}

	@Test
	public void MonopolyCommandTest() {
		Monopoly input = new Monopoly(0);
		input.setResource(ResourceType.ore);
		input.setGameId(testGameID);
		MonopolyCommand command = new MonopolyCommand(input);
		assertTrue(command != null);
	}

	@Test
	public void MonumentCommandTest() {
		Monument input = new Monument();
		input.setGameId(testGameID);
		input.setPlayerIndex(0);
                input.setType("monument");
		MonumentCommand command = new MonumentCommand(input);
		assertTrue(command != null);
		assertFalse(command.execute());	
	}
	
	@Test
	public void OfferTradeCommandTest() {
		OfferTrade input = new OfferTrade();
		input.setGameId(testGameID);
		input.setOffer(new ResourceList());
		input.setReceiver(2);
		input.setPlayerIndex(0);
		OfferTradeCommand command = new OfferTradeCommand(input);
		assertTrue(command != null);
		assertFalse(command.execute());
	}


}
