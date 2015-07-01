package client.proxy;

/**
 * represents a reqeust to force a particular roll.
 * @author Aaron
 *
 */
public class RollNumber extends Request  {
	private String type = "rollNumber";
	private int playerIndex;
	private int number;
	
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
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
}
