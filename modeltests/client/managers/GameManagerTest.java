/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.managers;

import client.data.Card;
import client.data.Game;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 *
 * @author ddennis
 */
public class GameManagerTest {
    
    public GameManagerTest() {
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

    /**
     * Test of initializeGame method, of class GameManager.
     */
    @Test
    public void testInitializeGame() {
        System.out.println("initializeGame");
        String jsonData = "";
        GameManager instance = new GameManager();
        instance.initializeGame(jsonData);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of endGame method, of class GameManager.
     */
    @Test
    public void testEndGame() {
        System.out.println("endGame");
        int gameID = 0;
        GameManager instance = new GameManager();
        instance.endGame(gameID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createGame method, of class GameManager.
     */
    @Test
    public void testCreateGame() {
        System.out.println("createGame");
        GameManager instance = new GameManager();
        instance.createGame();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listGames method, of class GameManager.
     */
    @Test
    public void testListGames() {
        System.out.println("listGames");
        int gameID = 0;
        GameManager instance = new GameManager();
        List<Game> expResult = null;
        List<Game> result = instance.listGames(gameID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of joinGame method, of class GameManager.
     */
    @Test
    public void testJoinGame() {
        System.out.println("joinGame");
        int gameID = 0;
        GameManager instance = new GameManager();
        instance.joinGame(gameID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gameHasFourPlayers method, of class GameManager.
     */
    @Test
    public void testGameHasFourPlayers() {
        System.out.println("gameHasFourPlayers");
        int gameID = 0;
        GameManager instance = new GameManager();
        boolean expResult = false;
        boolean result = instance.gameHasFourPlayers(gameID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of placeRoad method, of class GameManager.
     */
    @Test
    public void testPlaceRoad() {
        System.out.println("placeRoad");
        int gameID = 0;
        EdgeLocation edge = null;
        GameManager instance = new GameManager();
        instance.placeRoad(gameID, edge);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of placeSettlement method, of class GameManager.
     */
    @Test
    public void testPlaceSettlement() {
        System.out.println("placeSettlement");
        int gameID = 0;
        HexLocation h = null;
        VertexLocation v = null;
        GameManager instance = new GameManager();
        instance.placeSettlement(gameID, h, v);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rollDice method, of class GameManager.
     */
    @Test
    public void testRollDice() {
        System.out.println("rollDice");
        int dieRoll = 0;
        GameManager instance = new GameManager();
        instance.rollDice(dieRoll);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of diceIsSevenMoveRober method, of class GameManager.
     */
    @Test
    public void testDiceIsSevenMoveRober() {
        System.out.println("diceIsSevenMoveRober");
        HexLocation newLocationForRobber = null;
        GameManager instance = new GameManager();
        instance.diceIsSevenMoveRober(newLocationForRobber);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of buy method, of class GameManager.
     */
    @Test
    public void testBuy() {
        System.out.println("buy");
        int gameID = 0;
        String card = "";
        GameManager instance = new GameManager();
        boolean expResult = false;
        boolean result = instance.buy(gameID, card);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of build method, of class GameManager.
     */
    @Test
    public void testBuild() {
        System.out.println("build");
        int gameID = 0;
        String object = "";
        GameManager instance = new GameManager();
        boolean expResult = false;
        boolean result = instance.build(gameID, object);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of trade method, of class GameManager.
     */
    @Test
    public void testTrade() {
        System.out.println("trade");
        int gameID = 0;
        int senderID = 0;
        int receiverID = 0;
        ArrayList<Card> offer = null;
        ArrayList<Card> request = null;
        GameManager instance = new GameManager();
        boolean expResult = false;
        boolean result = instance.trade(gameID, senderID, receiverID, offer, request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of useCard method, of class GameManager.
     */
    @Test
    public void testUseCard() {
        System.out.println("useCard");
        int gameID = 0;
        Card c = null;
        GameManager instance = new GameManager();
        boolean expResult = false;
        boolean result = instance.useCard(gameID, c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of endTurn method, of class GameManager.
     */
    @Test
    public void testEndTurn() {
        System.out.println("endTurn");
        int gameID = 0;
        GameManager instance = new GameManager();
        instance.endTurn(gameID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
