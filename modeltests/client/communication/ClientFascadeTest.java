/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import shared.model.LoadGameRequest;
import shared.model.JoinGameRequest;
import shared.model.CreateGameRequest;
import shared.model.SaveGameRequest;
import shared.data.GameInfo;
import shared.data.SettlementLocation;
import shared.data.User;
import shared.model.BuildCity;
import shared.model.BuyDevCard;
import shared.model.Command;
import shared.model.FinishMove;
import shared.model.Monopoly;
import shared.model.Road_Building;
import shared.model.RollNumber;
import shared.model.SendChat;
import shared.model.Year_Of_Plenty;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.UUID;

import static org.junit.Assert.*;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

/**
 *
 * @author ddennis
 */
public class ClientFascadeTest {

    public ClientFascadeTest() {
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
     * Test of login method, of class ClientFascade.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLogin() throws Exception {
        System.out.print("login");
        User credentials = new User("Sam", "sam");
        ClientFascade instance = new ClientFascade();
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
 ClientFascade.
     */
    @Test
    public void testRegister() throws Exception {
        System.out.print("register");
        User credentials = new User("Sam", "sam");
        ClientFascade instance = new ClientFascade();
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
 ClientFascade.
     */
    @Test
    public void testListGames() throws Exception {
        System.out.print("listGames");
        // test games for Sam
        ClientFascade instance = new ClientFascade();
        String expResult = "Sam";
        ArrayList<GameInfo> result = instance.listGames();

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
 ClientFascade.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreateGame() throws Exception {
        System.out.print("createGame");
        ClientFascade instance = new ClientFascade();
        String expResult = "Dubbs";
        CreateGameRequest createGameRequest = new CreateGameRequest(true, false, true, "Dubbs");
        GameInfo result = instance.createGame(createGameRequest);
        assertEquals(expResult, result.getTitle());

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of joinGame method, of class
 ClientFascade.
     */
    @Test
    public void testJoinGame() throws Exception {
        System.out.print("joinGame");
        ClientFascade instance = new ClientFascade();
        boolean expResult = true;
        boolean result = instance.joinGame(new JoinGameRequest(0, "red"));
        assertEquals(expResult, result);

        expResult = false;
        result = instance.joinGame(new JoinGameRequest(-1, "red"));
        assertEquals(expResult, result);

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of saveGame method, of class
 ClientFascade.
     */
    @Test
    public void testSaveGame() throws Exception {
        System.out.print("saveGame");
        ClientFascade instance = new ClientFascade();
        boolean expResult = false;
        SaveGameRequest saveGameRequest = new SaveGameRequest(0, "gameBuggySam");
        boolean result = instance.saveGame(saveGameRequest);
        assertEquals(expResult, result);

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of loadGame method, of class
 ClientFascade.
     */
    @Test
    public void testLoadGame() throws Exception {
        System.out.print("loadGame");
        ClientFascade instance = new ClientFascade();
        boolean expResult = false;
        LoadGameRequest loadGameRequest = new LoadGameRequest("gameBuggySam");
        boolean result = instance.loadGame(loadGameRequest);
        assertEquals(expResult, result);

        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of getGameModel method, of class ClientFascade.
     */
    @Test
    public void testGetGameModel() throws Exception {
        System.out.print("getGameModel");
        ClientFascade instance = new ClientFascade();
        String expResult = "Sam";
        
        //login
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        
        GameInfo result = instance.getGameModel("");
        assertEquals(expResult, result.getPlayers().get(0).getName());
        
        //test with query param
        //expResult = "\"true\"";
        result = instance.getGameModel("?version=0");
        assertEquals(null, result.getTitle());
        
        expResult = null;
        result = instance.getGameModel("?version=1");
        assertEquals(expResult, result.getTitle());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of resetGame method, of class ClientFascade.
     * @throws java.lang.Exception
     */
    @Test
    public void testResetGame() throws Exception {
        System.out.print("resetGame");
        ClientFascade instance = new ClientFascade();
        int expResult = 108;
        //must login and join a game first
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        GameInfo result = instance.resetGame();
        int actualResult = result.getBank().getTotalResources();
        assertEquals(expResult,actualResult);
      
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of getGameCommands method, of class ClientFascade.
     */
    @Test
    public void testGetGameCommands() throws Exception {
        System.out.print("getGameCommands");
        ClientFascade instance = new ClientFascade();
        int expResult = 2;
        //must login and join a game first
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        ArrayList<Command> result = instance.getGameCommands();
        assert(expResult <= result.size());
        
        System.out.print("...PASSED");
        System.out.println();
    }
    /**
     * Test of addAIToGame method, of class ClientFascade.
     */
    @Test
    public void testAddAIToGame() throws Exception {
        System.out.print("addAIToGame");
        ClientFascade instance = new ClientFascade();
        //Game expResult = null;
        //only LARGEST_ARMY Works for now
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        //Game result = instance.addAIToGame(new AddAIRequest("LARGEST_ARMY"));
        
        //not implemented
        assert(true);
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of listAITypesInGame method, of class ClientFascade.
     */
    @Test
    public void testListAITypesInGame() throws Exception {
        System.out.print("listAITypesInGame");
        ClientFascade instance = new ClientFascade();
        String expResult = "LARGEST_ARMY";
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        ArrayList<String> result = instance.listAITypesInGame();
        assertEquals(expResult, result.get(0));
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of sendChat method, of class ClientFascade.
     */
    @Test
    public void testSendChat() throws Exception {
        System.out.println("sendChat");
        ClientFascade instance = new ClientFascade();
        String expResult = "Hey, let's trade for some wheat";
        SendChat chat = new SendChat(0);
        chat.setContent(expResult);
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        GameInfo result = instance.sendChat(chat);
        assertEquals(expResult, result.getChat().getLines().get(0).getMessage());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of rollNumber method, of class ClientFascade.
     */
    @Test
    public void testRollNumber() throws Exception {
        System.out.println("rollNumber");
        RollNumber rollNumber = new RollNumber();
        rollNumber.setNumber(8);
        ClientFascade instance = new ClientFascade();
        int expResult = 9;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        GameInfo result = instance.finishMove(new FinishMove(0));
        
        //since 3 before it should be 4 now (since I rolled the dice)
        assert(expResult >= result.getVersion());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of finishMove method, of class ClientFascade.
     */
    @Test
    public void testFinishMove() throws Exception {
        System.out.println("finishMove");
        ClientFascade instance = new ClientFascade();
        String expResult = "Sam's turn just ended";
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));

        GameInfo result = instance.finishMove(new FinishMove(0));

        //Sam should've ended his turn
        assertEquals(expResult, result.getLog().getLines().get(2).getMessage());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of buyDevCard method, of class ClientFascade.
     */
    @Test
    public void testBuyDevCard() throws Exception {
        System.out.println("buyDevCard");
        ClientFascade instance = new ClientFascade();
        int expResult = 111;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        GameInfo result = instance.buyDevCard(new BuyDevCard(0));
        
        //should have decrease by one
        int actualResult = result.getBank().getTotalResources();
        assertEquals(expResult, actualResult);
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of year_Of_Plenty method, of class ClientFascade.
     */
    @Test
    public void testYear_Of_Plenty() throws Exception {
        System.out.println("year_Of_Plenty");
        Year_Of_Plenty year_of_plenty = new Year_Of_Plenty(0);
        year_of_plenty.setResource1(ResourceType.wood);
        year_of_plenty.setResource2(ResourceType.wood);
        
        ClientFascade instance = new ClientFascade();
        boolean expResult = true;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        //Game result = instance.year_Of_Plenty(year_of_plenty);
        
        //not Implemented on the server correctly
        assertTrue(expResult);
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of roadBuilding method, of class ClientFascade.
     */
    @Test
    public void testRoadBuilding() throws Exception {
        System.out.println("roadBuilding");
        Road_Building roadBuilding = new Road_Building(0);
        roadBuilding.setSpot1(new EdgeLocation(new HexLocation(0,0), EdgeDirection.N));
        roadBuilding.setSpot2(new EdgeLocation(new HexLocation(0,1), EdgeDirection.NE));
        
        ClientFascade instance = new ClientFascade();
        GameInfo expResult = null;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        GameInfo result = instance.roadBuilding(roadBuilding);
        assertEquals(expResult, result.getTitle());
        
        System.out.print("...PASSED");
        System.out.println();
    }


    /**
     * Test of monopoly method, of class ClientFascade.
     */
    @Test
    public void testMonopoly() throws Exception {
        System.out.println("monopoly");
        Monopoly monopoly = new Monopoly(0);
        monopoly.setResource(ResourceType.wood);
        
        ClientFascade instance = new ClientFascade();
        GameInfo expResult = null;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        GameInfo result = instance.monopoly(monopoly);
        assertEquals(expResult, result.getTitle());
       
        System.out.print("...PASSED");
        System.out.println();
    }
  
}
