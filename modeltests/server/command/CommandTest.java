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

import shared.data.SettlementLocation;
import shared.data.XYEdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.model.AcceptTrade;
import shared.model.BuildRoad;
import shared.model.BuildSettlement;
import shared.model.BuyDevCard;

public class CommandTest {

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
		
		//Try some cases where a trade hasn't been offered.
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
	
	// TODO: put tests 7-12 here
	
	// TODO: put tests 13-19 here
}
