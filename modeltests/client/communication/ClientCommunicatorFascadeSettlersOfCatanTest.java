/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import client.data.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
        User user = new User("Sam", "sam");
        ClientCommunicatorFascadeSettlersOfCatan instance = new ClientCommunicatorFascadeSettlersOfCatan();
        boolean expResult = true;
        boolean result = instance.login(user);
        assertEquals(expResult, result);
        
        user = new User("Sam", "lyons");
        expResult = false;
        result = instance.login(user);
        assertEquals(expResult, result);
        
        System.out.print("...PASSED");
        System.out.println();
        
    }
    
}
