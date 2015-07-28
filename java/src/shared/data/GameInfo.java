package shared.data;

import java.util.*;

/**
 * Used to pass game information into views<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique game ID</li>
 * <li>Title: Game title (non-empty string)</li>
 * <li>Players: List of players who have joined the game (can be empty)</li>
 * </ul>
 *
 */
public class GameInfo {

    private String title;
    private int id;
    private ResourceList bank;
    private MessageList chat;
    private MessageList log;
    private Map map;
    private List<PlayerInfo> players;
    private TradeOffer tradeOffer;
    private TurnTracker turnTracker;
    private DevCardList deck;
    private int version;
    private int winner;

    public GameInfo(String title) {
        setId(-1);
        setTitle(title);
        players = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResourceList getBank() {
        return bank;
    }

    public void setBank(ResourceList bank) {
        this.bank = bank;
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

    public shared.data.Map getMap() {
        return map;
    }

    public void setMap(shared.data.Map map) {
        this.map = map;
    }

    public List<PlayerInfo> getPlayers() {
        return players;
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

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DevCardList getDeck() {
        return deck;
    }

    public void setDeck(DevCardList deck) {
        this.deck = deck;
    }

    public void addPlayer(PlayerInfo newPlayer) {
        players.add(newPlayer);
    }

    @Override
    public String toString() {
        String output = "{\"deck\":"+deck.toString()+
                ",\"map\":"+map.toString()+
                ",\"players\":"+players.toString()+ 
                ", \"bank\" : " + bank.toString() + 
                ", \"chat\" : " + chat.toString() + 
                ", \"log\" : " + log.toString()+
                ", \"turnTracker\" : " + turnTracker.toString() + 
                ", \"version\" : " + version + ", \"winner\" : " + winner + "}";
        System.out.println("Game Info : " + output);
        return output;
//        return "{" + "\"title\" : \"" + title + "\" , \"id\" : " + id + ", \"bank\" : " + bank + ", \"chat\" : " + chat + ", \"log\" : " + log + ", \"map\" : " + map + ", \"players\" : " + players + ", \"tradeOffer\" : " + tradeOffer + ", \"turnTracker\" : " + turnTracker + ", \"version\" : " + version + ", \"winner\" : " + winner + "}";
    }

}
