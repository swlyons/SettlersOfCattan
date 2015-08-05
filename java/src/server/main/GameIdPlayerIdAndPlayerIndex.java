/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.main;

/**
 *
 * @author Samuel
 */
public class GameIdPlayerIdAndPlayerIndex {

    private int gameId;
    private int playerId;
    private int playerIndex;

    public GameIdPlayerIdAndPlayerIndex(int gameId, int playerId, int playerIndex) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerIndex = playerIndex;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

}
