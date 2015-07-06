package client.data;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private String name;
    private ResourceList bank;
    private MessageList chat;
    private MessageList log;
    private Map map;
    private List<Player> players;
    private TradeOffer tradeOffer;
    private TurnTracker turnTracker;
    private int version;
    private int winner;

    public Game(List<Player> players, String name) {
        this.players = players;
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
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

    public ResourceList getBank() {
        return bank;
    }

    public void setBank(ResourceList bank) {
        this.bank = bank;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "{" + "bank : " + bank + ", chat : " + chat + ", log : " + log + ", map : " + map + ", players : " + players + ", tradeOffer : " + tradeOffer + ", turnTracker : " + turnTracker + ", version : " + version + ", winner : " + winner + '}';
    }

    

}
