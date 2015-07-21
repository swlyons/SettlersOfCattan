package shared.data;

import shared.definitions.*;

/**
 * Used to pass player information into views<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique player ID</li>
 * <li>PlayerIndex: Player's order in the game [0-3]</li>
 * <li>Name: Player's name (non-empty string)</li>
 * <li>Color: Player's color (cannot be null)</li>
 * </ul>
 *
 */
public class PlayerInfo {

    private String name;
    private String color;
    private int cities;
    private int settlements;
    private int roads;
    private int victoryPoints;
    private ResourceList resources;
    private int playerIndex;
    private int id;
    private boolean discarded;
    private boolean playedDevCard;
    private int playerID;

    public PlayerInfo() {
        setId(-1);
        setPlayerIndex(-1);
        setPlayerID(-1);
        setName("");
        setColor(CatanColor.WHITE.name().toLowerCase());
        this.cities = 4;
        this.settlements = 5;
        this.roads = 15;
        this.victoryPoints = 0;
        this.resources = new ResourceList();
        this.discarded = false;
        this.playedDevCard = false;
        this.monuments = 0;
        this.oldDevCards = new DevCardList();
        this.newDevCards = new DevCardList();
        this.soldiers = 0;

    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getCities() {
        return cities;
    }

    public void setCities(int cities) {
        this.cities = cities;
    }

    public int getSettlements() {
        return settlements;
    }

    public void setSettlements(int settlements) {
        this.settlements = settlements;
    }

    public int getRoads() {
        return roads;
    }

    public void setRoads(int roads) {
        this.roads = roads;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public ResourceList getResources() {
        return resources;
    }

    public void setResources(ResourceList resources) {
        this.resources = resources;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public boolean isPlayedDevCard() {
        return playedDevCard;
    }

    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public int getMonuments() {
        return monuments;
    }

    public void setMonuments(int monuments) {
        this.monuments = monuments;
    }

    public DevCardList getOldDevCards() {
        return oldDevCards;
    }

    public void setOldDevCards(DevCardList oldDevCards) {
        this.oldDevCards = oldDevCards;
    }

    public DevCardList getNewDevCards() {
        return newDevCards;
    }

    public void setNewDevCards(DevCardList newDevCards) {
        this.newDevCards = newDevCards;
    }

    public int getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
    }
    private int monuments;
    private DevCardList oldDevCards;
    private DevCardList newDevCards;
    private int soldiers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int hashCode() {
        return 31 * this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerInfo other = (PlayerInfo) obj;

        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "{" + "name : " + name + ", color : " + color + ", cities : " + cities + ", settlements : " + settlements
                + ", roads : " + roads + ", victoryPoints : " + victoryPoints + ", resources : " + resources
                + ", playerIndex : " + playerIndex + ", id : " + id + ", playerID : " + playerID + ", discarded : " + discarded
                + ", playedDevCard : " + playedDevCard + ", monuments : " + monuments + ", oldDevCards : " + oldDevCards
                + ", newDevCards : " + newDevCards + ", soldiers : " + soldiers + '}';
    }
}
