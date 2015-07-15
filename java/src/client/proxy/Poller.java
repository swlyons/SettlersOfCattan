package client.proxy;

import client.ClientException;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import java.util.TimerTask;

import client.data.GameInfo;
import client.managers.GameManager;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Poller class which will occasionally call for an update on the map.
 *
 * @author Aaron
 *
 */
public class Poller extends TimerTask {
    private int version = 0;
    public Poller() {

    }

    public void run() {
        int currentGameID = -1;
        String gameID = "";

        for (Map.Entry<Integer, String> entry : ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getCookies().entrySet()) {
            //System.out.println(entry.getValue());
            gameID = entry.getValue().split("catan.game=")[1];
        }

        currentGameID = Integer.parseInt(gameID);
        
        //get the current game version
        ArrayList<GameInfo> games = null;
        try {
            games = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
            for (GameInfo game : games) {
                if (currentGameID == game.getId()) {
                    //version = game.getVersion();
                    break;
                }
            }
        } catch (ClientException ex) {
            Logger.getLogger(Poller.class.getName()).log(Level.SEVERE, null, ex);
        }

        // get the model version
        try {
            GameInfo game = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getGameModel(version + "");
            //only update if it is a newer version
            
            if (version < game.getVersion()) {
                version++;
                System.out.println("Poller: Old Version is " + version + " New Version is " + game.getVersion());
                GameManager manager = ClientCommunicator.getSingleton().getGameManager();
                manager.initializeGame(game);
                
            }
        } catch (ClientException ex) {
            Logger.getLogger(Poller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
