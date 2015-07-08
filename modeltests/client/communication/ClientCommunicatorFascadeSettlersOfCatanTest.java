/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import client.proxy.LoadGameRequest;
import client.proxy.JoinGameRequest;
import client.proxy.CreateGameRequest;
import client.proxy.SaveGameRequest;
import client.data.Game;
import client.data.User;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import java.util.UUID;
import static org.junit.Assert.*;

@Ignore

/**
 *
 * @author ddennis
 */
public class ClientCommunicatorFascadeSettlersOfCatanTest {

    public ClientCommunicatorFascadeSettlersOfCatanTest() {
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
     * Test of login method, of class ClientCommunicatorFascadeSettlersOfCatan.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLogin() throws Exception {
        System.out.print("login");
        User credentials = new User("Sam", "sam");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        boolean expResult = true;
        boolean result = instance.login(credentials);
        assertEquals(expResult, result);

        credentials = new User("Sam", "lyons");
        expResult = false;
        result = instance.login(credentials);
        assertEquals(expResult, result);

        System.out.print("...PASSED");
        System.out.println();

    }

    /**
     * Test of register method, of class
     * ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testRegister() throws Exception {
        System.out.print("register");
        User credentials = new User("Sam", "sam");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        boolean expResult = false;
        boolean result = instance.register(credentials);
        assertEquals(expResult, result);

        // need to change username each time since it remembers
        credentials = new User("Samuel" + UUID.randomUUID(), "Lyons");
        expResult = true;
        result = instance.register(credentials);
        assertEquals(expResult, result);

        // try logging in as the new user
        result = instance.login(credentials);
        assertEquals(expResult, result);

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of listGames method, of class
     * ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testListGames() throws Exception {
        System.out.print("listGames");
        // test games for Sam
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        String expResult = "Sam";
        ArrayList<Game> result = instance.listGames();

        //1st player should be Sam
        assertEquals(expResult, result.get(0).getPlayers().get(0).getName());

        //1st Game should be default game
        expResult = "Default Game";
        assertEquals(expResult, result.get(0).getTitle());

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of createGame method, of class
     * ClientCommunicatorFascadeSettlersOfCatan.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreateGame() throws Exception {
        System.out.print("createGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        String expResult = "Dubbs";
        CreateGameRequest createGameRequest = new CreateGameRequest(true, false, true, "Dubbs");
        Game result = instance.createGame(createGameRequest);
        assertEquals(expResult, result.getTitle());

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of joinGame method, of class
     * ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testJoinGame() throws Exception {
        System.out.print("joinGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        boolean expResult = true;
        JoinGameRequest joinGameRequest = new JoinGameRequest(0, "puce");
        boolean result = instance.joinGame(joinGameRequest);
        assertEquals(expResult, result);

        expResult = false;
        joinGameRequest = new JoinGameRequest(-1, "red");
        result = instance.joinGame(joinGameRequest);
        assertEquals(expResult, result);

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of saveGame method, of class
     * ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testSaveGame() throws Exception {
        System.out.print("saveGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        boolean expResult = false;
        SaveGameRequest saveGameRequest = new SaveGameRequest(0, "gameBuggySam");
        boolean result = instance.saveGame(saveGameRequest);
        assertEquals(expResult, result);

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of loadGame method, of class
     * ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testLoadGame() throws Exception {
        System.out.print("loadGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        boolean expResult = false;
        LoadGameRequest loadGameRequest = new LoadGameRequest("gameBuggySam");
        boolean result = instance.loadGame(loadGameRequest);
        assertEquals(expResult, result);

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of getGameModel method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testGetGameModel() throws Exception {
        System.out.print("getGameModel");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        String expResult = "Sam";
        Game result = instance.getGameModel("");
        assertEquals(expResult, result.getPlayers().get(0).getName());
        
        //test with query param
        expResult = "\"true\"";
        result = instance.getGameModel("?version=0");
        assertEquals(expResult, result.getTitle());
        
        expResult = "Rolling";
        result = instance.getGameModel("?version=1");
        assertEquals(expResult, result.getTurnTracker().getStatus());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of resetGame method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testResetGame() throws Exception {
        System.out.print("resetGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        Game result = instance.resetGame();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGameCommands method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testGetGameCommands() throws Exception {
        System.out.print("getGameCommands");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        Game result = instance.getGameCommands();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of executeGameCommand method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testExecuteGameCommand() throws Exception {
        System.out.print("executeGameCommand");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        Game result = instance.executeGameCommand();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAIToGame method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testAddAIToGame() throws Exception {
        System.out.print("addAIToGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        Game result = instance.addAIToGame();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listAITypesInGame method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testListAITypesInGame() throws Exception {
        System.out.print("listAITypesInGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        Game result = instance.listAITypesInGame();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
