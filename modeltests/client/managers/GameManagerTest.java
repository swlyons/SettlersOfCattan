/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.managers;

import client.data.Game;
import client.data.Player;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
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
        String jsonDataIn = "";
        String jsonDataOut = "";
        try {
            jsonDataIn = FileUtils.readFileToString(new File("C:\\Users\\ddennis\\Documents\\SettlersOfCattan" + File.separator + "sample" + File.separator + "model.json"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "\n\t" + jsonDataIn);
        }
        GameManager instance = new GameManager();
        instance.initializeGame(jsonDataIn);

        Game game = instance.getGame();
        //print Json Model (Game)
        System.out.println(game);

        assertTrue("Model was successfully initialized", jsonDataIn.equals(jsonDataOut) && !jsonDataOut.isEmpty());

    }

}
