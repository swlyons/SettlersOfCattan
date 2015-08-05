/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;
import server.receiver.AllOfOurInformation;
import client.managers.GameManager;
import shared.data.*;
/**
 *
 * @author Samuel
 */
public class CreateGameCommand implements Command{
    private boolean randomTiles;
    private boolean randomNumbers;
    private boolean randomPorts;
    private String name;
    private int gameId;
    
    public CreateGameCommand(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name){
        this.randomTiles=randomTiles;
        this.randomNumbers=randomNumbers;
        this.randomPorts=randomPorts;
        this.name=name;
    }
    
    public int getGameId(){
        return gameId;
    }
    
    @Override
    public boolean execute() {
        return createGame();
    }
    
    public boolean createGame(){
        if(name==null||name.equals("")){
            return false;
        }
        
        GameManager gm = new GameManager();
        gm.createGame(randomTiles, randomNumbers, randomPorts, name);
                
        AllOfOurInformation.getSingleton().getGames().add(gm);
        
        for(int i=0;i<AllOfOurInformation.getSingleton().getGames().size();i++){
            if(gm.equals(AllOfOurInformation.getSingleton().getGames().get(i))){
                gameId = i;
                break;
            }
        }
        
        GameInfo gi = AllOfOurInformation.getSingleton().getGames().get(gameId).getGame();
        gi.setId(gameId);
        AllOfOurInformation.getSingleton().getGames().get(gameId).setGame(gi);
        
        //add the game to the database
        AllOfOurInformation.getSingleton().addGameToDatabase(gi);
        return true;
    }
    
}
