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
<<<<<<< HEAD
    private int version = 0;
    private boolean firstTime = true;
=======
    private int version = -1;
>>>>>>> 36af4841bf00139842f13d6b7e5f2b56fca177bd
    public Poller() {
    }

    public void run() {
        // get the model version
        try {
            
            GameInfo game = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getGameModel(version + "");
            //only update if it is a newer version
<<<<<<< HEAD
            if (version < game.getVersion() ||  (game.getVersion() == 0 && firstTime)) {
                
                GameManager manager = ClientCommunicator.getSingleton().getGameManager();
                manager.initializeGame(game, version+"");
                
                //update the version
                version = game.getVersion();
                firstTime = false;
=======
            if (version < game.getVersion()) {
                System.out.println("Poller: Old Version is " + version + " New Version is " + game.getVersion());
                version=game.getVersion();
                GameManager manager = ClientCommunicator.getSingleton().getGameManager();
                manager.initializeGame(game, version+"");
                
//                ClientCommunicator.getSingleton().setGameManager(manager);
>>>>>>> 36af4841bf00139842f13d6b7e5f2b56fca177bd
            }
        } catch (ClientException ex) {
            Logger.getLogger(Poller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
