package client.proxy;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A Poller class which will occasionally call for an update on the map.
 * @author Aaron
 *
 */
public class Poller {
	
	private Timer timer;
	
	private static Poller singleton = null;
	
	/**
	 * Gets the single instance of the poller.
	 * @return The Poller
	 * @pre None
	 * @post The result is an initialized Poller, and is the same one that is returned from other calls.
	 */
	public static Poller getSingleton() {
		if (singleton == null) {
			singleton = new Poller();
		}
		return singleton;
	}
	
	private Poller() {
		timer = new Timer();
		timer.schedule(new Run(), 10000);
	}
	
	private class Run extends TimerTask {
		/**
		 * The periodic method called by poller. This will turn around and call get model on the ServerProxy
		 * @pre None
		 * @post ServerProxy singleton had getModel() called.
		 */
		public void run() {
			
		}
	}
}
