/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import client.data.Game;
import client.data.User;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.UUID;
import static org.junit.Assert.*;

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
     * @throws java.lang.Exception
     */
    @Test
    public void testLogin() throws Exception {
        System.out.println("login");
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
     * Test of register method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testRegister() throws Exception {
        System.out.println("register");
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
     * Test of listGames method, of class ClientCommunicatorFascadeSettlersOfCatan.
     */
    @Test
    public void testListGames() throws Exception {
        System.out.println("listGames");
        int playerID = 0;// Sam
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        String expResult = "Sam";
        ArrayList<Game> result = instance.listGames(playerID);
        
        //1st player should be Sam
        assertEquals(expResult, result.get(0).getPlayers().get(0).getName());
        
        //1st Game should be default game
        expResult = "Default Game";
        assertEquals(expResult, result.get(0).getTitle());
        
        System.out.print("...PASSED");
        System.out.println();
    }
    
    
}
