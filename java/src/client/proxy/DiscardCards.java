package client.proxy;

import client.data.ResourceList;

/**
 * contains information about discarding cards
 * @author Aaron
 *
 */
public class DiscardCards extends Request {
	private String type = "discardCards";
	private int playerIndex;
	private ResourceList discardedCards;
}
