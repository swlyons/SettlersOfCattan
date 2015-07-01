package client.proxy;

/**
 * Contains the information for posting a new chat message.
 * @author Aaron
 *
 */
public class SendChat extends Request  {
	private String type = "sendChat";
	private int playerIndex;
	private String content;
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
