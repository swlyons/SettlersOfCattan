package client.proxy;

import client.ClientException;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import java.util.TimerTask;

import client.data.GameInfo;
import client.managers.GameManager;
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
    private boolean firstTime = true;

    public Poller() {
    }

    public void run() {
        // get the model version
        try {
            
            GameInfo game = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getGameModel(version + "");
            //only update if it is a newer version
            if (version < game.getVersion() ||  (game.getVersion() == 0 && firstTime)) {
                
                GameManager manager = ClientCommunicator.getSingleton().getGameManager();
                manager.initializeGame(game, version+"");
                
                //update the version
                version = game.getVersion();
                firstTime = false;
                
//                ClientCommunicator.getSingleton().setGameManager(manager);
            }
        } catch (ClientException ex) {
            Logger.getLogger(Poller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
