package client.proxy;

import java.util.Timer;
import java.util.TimerTask;

import client.data.Game;
import client.managers.GameManager;

/**
 * A Poller class which will occasionally call for an update on the map.
 * @author Aaron
 *
 */
public class Poller {
	
	public Poller() {
		
	}
	
	public void run() {
		String game = ServerProxy.getSingleton().UpdateMap();
		GameManager manager = new GameManager();
		manager.initializeGame(game);
	}
}
