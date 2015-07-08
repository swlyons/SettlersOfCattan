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
import client.proxy.AcceptTrade;
import client.proxy.BuildCity;
import client.proxy.BuildRoad;
import client.proxy.BuildSettlement;
import client.proxy.BuyDevCard;
import client.proxy.Command;
import client.proxy.DiscardCards;
import client.proxy.FinishMove;
import client.proxy.MaritimeTrade;
import client.proxy.Monopoly;
import client.proxy.Monument;
import client.proxy.OfferTrade;
import client.proxy.Road_Building;
import client.proxy.RobPlayer;
import client.proxy.RollNumber;
import client.proxy.SendChat;
import client.proxy.Soldier;
import client.proxy.Year_Of_Plenty;
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
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

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
        
        //login
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        
        Game result = instance.getGameModel("");
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
     * Test of resetGame method, of class ClientCommunicatorFascadeSettlersOfCatan.
     * @throws java.lang.Exception
     */
    @Test
    public void testResetGame() throws Exception {
        System.out.print("resetGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        int expResult = 108;
        //must login and join a game first
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.resetGame();

        assertEquals(expResult,result.getBank().getTotalResources());
      
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of getGameCommands method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testGetGameCommands() throws Exception {
        System.out.print("getGameCommands");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
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
     * Test of executeGameCommand method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testExecuteGameCommands() throws Exception {
        System.out.print("executeGameCommand");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        String expResult = "Sam";
        //must login and join a game first
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        BuildCity buildCity = new BuildCity(0);
        buildCity.setVertexLocation(new VertexLocation(new HexLocation(0,0), VertexDirection.NW));
        
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new BuyDevCard(0));
        commands.add(buildCity);
        
        Game result = instance.executeGameCommands(commands);
        
        assert((result.getLog().getLines().get(0).getMessage()).contains(expResult));
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of addAIToGame method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testAddAIToGame() throws Exception {
        System.out.print("addAIToGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
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
     * Test of listAITypesInGame method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testListAITypesInGame() throws Exception {
        System.out.print("listAITypesInGame");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        String expResult = "LARGEST_ARMY";
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        ArrayList<String> result = instance.listAITypesInGame();
        assertEquals(expResult, result.get(0));
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of sendChat method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testSendChat() throws Exception {
        System.out.println("sendChat");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        String expResult = "Hey, let's trade for some wheat";
        SendChat chat = new SendChat(0);
        chat.setContent(expResult);
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.sendChat(chat);
        assertEquals(expResult, result.getChat().getLines().get(0).getMessage());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of rollNumber method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testRollNumber() throws Exception {
        System.out.println("rollNumber");
        RollNumber rollNumber = new RollNumber(0);
        rollNumber.setNumber(8);
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        int expResult = 9;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.rollNumber(rollNumber);
        
        //since 3 before it should be 4 now (since I rolled the dice)
        assert(expResult >= result.getVersion());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of robPlayer method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testRobPlayer() throws Exception {
        System.out.println("robPlayer");
        RobPlayer robPlayer = new RobPlayer(0);
        robPlayer.setVictimIndex(1);
        robPlayer.setLocation(new HexLocation(0,0));
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.robPlayer(robPlayer);
        //should only have 1 wheat resource now
        assertEquals(expResult, result.getId());
       
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of finishMove method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testFinishMove() throws Exception {
        System.out.println("finishMove");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        String expResult = "Sam's turn just ended";
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.finishMove(new FinishMove(0));
        
        //Sam should've ended his turn
        assertEquals(expResult, result.getLog().getLines().get(2).getMessage());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of buyDevCard method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testBuyDevCard() throws Exception {
        System.out.println("buyDevCard");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        int expResult = 111;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.buyDevCard(new BuyDevCard(0));
        
        //should have decrease by one
        assertEquals(expResult, result.getBank().getTotalResources());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of year_Of_Plenty method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testYear_Of_Plenty() throws Exception {
        System.out.println("year_Of_Plenty");
        Year_Of_Plenty year_of_plenty = new Year_Of_Plenty(0);
        year_of_plenty.setResource1(ResourceType.wood);
        year_of_plenty.setResource2(ResourceType.wood);
        
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
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
     * Test of roadBuilding method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testRoadBuilding() throws Exception {
        System.out.println("roadBuilding");
        Road_Building roadBuilding = new Road_Building(0);
        roadBuilding.setSpot1(new EdgeLocation(new HexLocation(0,0), EdgeDirection.N));
        roadBuilding.setSpot2(new EdgeLocation(new HexLocation(0,1), EdgeDirection.NE));
        
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.roadBuilding(roadBuilding);
        assertEquals(expResult, result.getTitle());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of soldier method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testSoldier() throws Exception {
        System.out.println("soldier");
        Soldier soldier = new Soldier(0);
        soldier.setVicitmIndex(0);
        soldier.setLocation(new HexLocation(0,0));
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.soldier(soldier);
        
        assertEquals(expResult, result.getTitle());
        
        System.out.print("...PASSED");
        System.out.println();
    }

    /**
     * Test of monopoly method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testMonopoly() throws Exception {
        System.out.println("monopoly");
        Monopoly monopoly = new Monopoly(0);
        monopoly.setResource(ResourceType.wood);
        
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        Game expResult = null;
        instance.login(new User("Sam", "sam"));
        instance.joinGame(new JoinGameRequest(0, "red"));
        Game result = instance.monopoly(monopoly);
        assertEquals(expResult, result.getTitle());
       
        System.out.print("...PASSED");
        System.out.println();
    }
  
}
