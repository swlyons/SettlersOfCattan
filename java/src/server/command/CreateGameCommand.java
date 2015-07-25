/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;
import server.receiver.AllOfOurInformation;
import client.managers.GameManager;
import java.util.ArrayList;
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
        ResourceList bank = new ResourceList();
        bank.setBrick(19);
        bank.setOre(19);
        bank.setSheep(19);
        bank.setWheat(19);
        bank.setWood(19);
        gi.setBank(bank);
        DevCardList deck = new DevCardList();
        deck.setMonopoly(2);
        deck.setMonument(5);
        deck.setRoadBuilding(2);
        deck.setSoldier(14);
        deck.setYearOfPlenty(2);
        gi.setDeck(deck);
        gi.setChat(new MessageList());
        gi.setLog(new MessageList());
        gi.setTradeOffer(null);
        TurnTracker ti = new TurnTracker();
        ti.setStatus("FirstRound");
        gi.setTurnTracker(ti);
        gi.setVersion(0);
        gi.setWinner(-1);
        
        Map map = new Map();
        map.setHexes(gm.getMapManager().getHexList());
        map.setPorts(gm.getLocationManager().getPorts());
        map.setRoads(new ArrayList<EdgeValue>());
        map.setSettlements(new ArrayList<VertexObject>());
        map.setCities(new ArrayList<VertexObject>());
        map.setRobber(gm.getMapManager().getRobberLocation());
                
        gi.setMap(map);
                
        gi.getPlayers().add(null);
        gi.getPlayers().add(null);
        gi.getPlayers().add(null);
        gi.getPlayers().add(null);
        AllOfOurInformation.getSingleton().getGames().get(gameId).setGame(gi);
        return true;
    }
    
}
