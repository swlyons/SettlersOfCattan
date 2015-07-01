package client.data;
import java.util.ArrayList;

public class Game {
		
	private ArrayList<Player> players;
	private Bank gameBank;
	private MessageList chat;
	private MessageList log;
	private TradeOffer tradeOffer;
	private TurnTracker turnTracker;
	private int version;
	private int winnerID;
	
	public Game() {
		players = new ArrayList<Player>();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public Bank getGameBank() {
		return gameBank;
	}
	public void setGameBank(Bank gameBank) {
		this.gameBank = gameBank;
	}
	public MessageList getChat() {
		return chat;
	}
	public void setChat(MessageList chat) {
		this.chat = chat;
	}
	public MessageList getLog() {
		return log;
	}
	public void setLog(MessageList log) {
		this.log = log;
	}
	public TradeOffer getTradeOffer() {
		return tradeOffer;
	}
	public void setTradeOffer(TradeOffer tradeOffer) {
		this.tradeOffer = tradeOffer;
	}
	public TurnTracker getTurnTracker() {
		return turnTracker;
	}
	public void setTurnTracker(TurnTracker turnTracker) {
		this.turnTracker = turnTracker;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getWinnerID() {
		return winnerID;
	}
	public void setWinnerID(int winnerID) {
		this.winnerID = winnerID;
	}
	
}
