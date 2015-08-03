/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import shared.data.GameInfo;
import client.managers.GameManager;
import client.map.MapController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ddennis
 */
public class SetupState extends State {

    @Override
    public void doAction(Controller controller) {
        MapController mapController = (MapController) controller;
        System.out.println(this.toString());
        GameManager gameManager = ClientCommunicator.getSingleton().getGameManager();

        GameInfo gameInformation = null;
        try {
            gameInformation = ClientFascade.getSingleton().getGameModel("?version=" + -1);
        } catch (ClientException ex) {
            ex.printStackTrace();
            Logger.getLogger(SetupState.class.getName()).log(Level.SEVERE, null, ex);
        }

        gameManager.initializeGame(gameInformation);
        
        mapController.initFromModel();
    }

    @Override
    public String toString() {
        return "SetupState{" + '}';
    }
}
