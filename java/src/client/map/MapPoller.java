/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map;

import java.util.TimerTask;
import client.data.GameInfo;
import client.managers.GameManager;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.map.MapController;

/**
 *
 * @author Samuel
 */
public class MapPoller extends TimerTask{
    
    private MapController mapController;
    private int version;
    private boolean firstInitialization;
    private int playerIndex;
    
    
    public MapPoller(MapController mapController){
        super();
        this.mapController = mapController;
        this.firstInitialization = false;
        playerIndex=-1;
        version=-1;
    }
    
    public void run(){
        if(ClientCommunicator.getSingleton().getJoinedGame()){
            try{
	            GameInfo gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getGameModel(version+"");
	            version = gameInformation.getVersion();
	            ClientCommunicator.getSingleton().getGameManager().initializeGame(gameInformation);
	            System.out.println(version);
	            if(playerIndex==-1){
	            	GameManager gm = ClientCommunicator.getSingleton().getGameManager();
	                Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
	                for(int i=0;i<gm.getGame().getPlayers().size();i++){
	                    if(gm.getGame().getPlayers().get(i).getId()==playerId){
	                        playerIndex=i;
	                        break;
	                    }
	                }
	            }
	            //If they haven't initialized before or it isn't the client's turn
	            if (!firstInitialization) {
	            	firstInitialization = true;
	            	
	            	mapController.initFromModel();
	            }
	            
	            //mapController.initFromModel();
            }catch (Exception e){
            }
        }
//        System.out.println("meow");
    }
    
}
