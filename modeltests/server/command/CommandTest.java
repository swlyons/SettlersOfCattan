package server.command;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import shared.data.XYEdgeLocation;
import shared.model.AcceptTrade;
import shared.model.BuildRoad;

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
	public void BuildRoadTestCommandTest() {
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
		assertTrue(command.execute());
		
		roadLocation.setX(1);
		buildRoad.setFree(false);
		buildRoad.setRoadLocation(roadLocation);
		command = new BuildRoadCommand(buildRoad);
		assertTrue(command != null);
		assertFalse(command.execute());
	}
	
	// TODO: put tests 7-12 here
	
	// TODO: put tests 13-19 here
}
