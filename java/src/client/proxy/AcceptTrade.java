package client.proxy;

/**
 * contains information on the action of accepting/rejecting a trade
 * @author Aaron
 *
 */
public class AcceptTrade extends Request {
	private String type = "acceptTrade";
	private int playerIndex;
	private boolean willAccept;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}
	public boolean isWillAccept() {
		return willAccept;
	}
	public void setWillAccept(boolean willAccept) {
		this.willAccept = willAccept;
	}
}
