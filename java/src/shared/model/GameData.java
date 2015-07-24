package shared.model;

import java.util.List;

import shared.data.PlayerInfo;

/**
 * Contains basic game information for the join game screen.
 *
 * @author Aaron
 *
 */
public class GameData {

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInfo> players) {
        this.players = players;
    }
    private String gameTitle;
    private int gameId;
    private List<PlayerInfo> players;
}
