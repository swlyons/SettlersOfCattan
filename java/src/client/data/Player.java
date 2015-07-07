package client.data;

public class Player {

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
    private int monuments;
    private DevCardList oldDevCards;
    private DevCardList newDevCards;
    private int soldiers;
    
    public Player() {

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
    
    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "{" + "name : " + name + ", color : " + color + ", cities : " + cities + ", settlements : " + settlements + ", roads : " + roads + ", victoryPoints : " + victoryPoints + ", resources : " + resources + ", playerIndex : " + playerIndex + ", id : " + id + ", discarded : " + discarded + ", playedDevCard : " + playedDevCard + ", monuments : " + monuments + ", oldDevCards : " + oldDevCards + ", newDevCards : " + newDevCards + ", soldiers : " + soldiers + '}';
    }
   

}
