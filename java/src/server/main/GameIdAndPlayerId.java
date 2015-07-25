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
public class GameIdAndPlayerId {
    private int gameId;
    private int playerId;
    
    public GameIdAndPlayerId(int gameId, int playerId){
        this.gameId=gameId;
        this.playerId=playerId;
    }
    
    public int getGameId(){
        return gameId;
    }
    
    public int getPlayerId(){
        return playerId;
    }
}
