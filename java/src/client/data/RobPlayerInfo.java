package client.data;

/**
 * Used to pass player information into the rob view<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique player ID</li>
 * <li>PlayerIndex: Player's order in the game [0-3]</li>
 * <li>Name: Player's name (non-empty string)</li>
 * <li>Color: Player's color (cannot be null)</li>
 * <li>NumCards: Number of development cards the player has (>= 0)</li>
 * </ul>
 *
 */
public class RobPlayerInfo extends PlayerInfo {

    private int numCards;

    public RobPlayerInfo() {
        super();
    }

    public RobPlayerInfo(PlayerInfo playerInfo, Integer numCards) {
        this.numCards = numCards;
        setName(playerInfo.getName());
        setColor(playerInfo.getColor());
        setCities(playerInfo.getCities());
        setSettlements(playerInfo.getSettlements());
        setRoads(playerInfo.getRoads());
        setVictoryPoints(playerInfo.getVictoryPoints());
        setResources(playerInfo.getResources());
        setPlayerIndex(playerInfo.getPlayerIndex());
        setId(playerInfo.getId());
        setDiscarded(playerInfo.isDiscarded());
        setPlayedDevCard(playerInfo.isPlayedDevCard());
        setPlayerID(playerInfo.getPlayerID());
    }

    public int getNumCards() {
        return numCards;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

}
