/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map;

import java.util.TimerTask;
import client.data.GameInfo;
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
    public MapPoller(MapController mapController){
        super();
        this.mapController = mapController;
        version=-1;
    }
    
    public void run(){
        if(ClientCommunicator.getSingleton().getJoinedGame()){
            try{
            GameInfo gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getGameModel(version+"");
            version = gameInformation.getVersion();
            System.out.println(version);
            }catch (Exception e){
            }
        }
        System.out.println("meow");
    }
    
}
