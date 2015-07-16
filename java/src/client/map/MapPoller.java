/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map;

import client.catan.CatanPanel;
import client.communication.ChatController;
import client.communication.ChatView;
import java.util.TimerTask;
import client.data.GameInfo;
import client.managers.GameManager;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.communication.LogEntry;
import client.data.MessageLine;
import client.data.PlayerInfo;
import client.map.MapController;
import client.turntracker.TurnTrackerView;
import java.util.ArrayList;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;

/**
 *
 * @author Samuel
 */
public class MapPoller extends TimerTask {

    private CatanPanel catanPanel;
    private int version;
    private boolean firstInitialization;;
    private int playerIndex;
    private String playerColor;

    public MapPoller(CatanPanel catanPanel) {
        super();
        this.catanPanel = catanPanel;
        this.firstInitialization = false;
        playerIndex = -1;
        playerColor = "";
        version = -1;
    }

    public void run() {
        if (ClientCommunicator.getSingleton().getJoinedGame()) {
            try {
                ChatView chatView = catanPanel.getLeftPanel().getChatView();
                TurnTrackerView turnTrackerView = catanPanel.getLeftPanel().getTurnView();

                GameInfo gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton()
                        .getGameModel(version + "");
                version = gameInformation.getVersion();

                GameManager gameManager = ClientCommunicator.getSingleton().getGameManager();
                gameManager.initializeGame(gameInformation);

                if (playerIndex == -1) {

                    Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
                    for (int i = 0; i < gameManager.getGame().getPlayers().size(); i++) {
                        if (gameManager.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                            playerIndex = gameManager.getGame().getPlayers().get(i).getPlayerIndex();
                            playerColor = gameManager.getGame().getPlayers().get(i).getColor().toUpperCase();
                            break;
                        }
                    }
                }
                // If they haven't initialized before or it isn't the client's
                // turn
                if (!firstInitialization || gameInformation.getTurnTracker().getCurrentTurn() != playerIndex) {

                    catanPanel.getMidPanel().getMapController().initFromModel();
                    GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                    if (gameInformation.getTurnTracker().getCurrentTurn() != playerIndex) // This boolean toggles on after your turn, so when the
                    // turn track comes around again, you get exactly one
                    // update on each of your turns. This way, your client
                    // will switch to a "It is your turn" state.
                    {
                        firstInitialization = false;
                    } else {
                        firstInitialization = true;
                        if (gm.getLocationManager().getSettledLocations().size() < 4) {
                            // We are on the first round.
                            //mapController.getView().startDrop(PieceType.SETTLEMENT, CatanColor.valueOf(playerColor), false);
                            //mapController.startMove(PieceType.SETTLEMENT, true, true);
                            //mapController.getView().startDrop(PieceType.ROAD, CatanColor.valueOf(playerColor), false);
                            //mapController.startMove(PieceType.ROAD, false, true);
                        } else if (gm.getLocationManager().getSettledLocations().size() < 8) {
                            // We are on the second round.
                            //mapController.getView().startDrop(PieceType.SETTLEMENT, CatanColor.valueOf(playerColor), false);
                            //mapController.startMove(PieceType.SETTLEMENT, true, true);
                            //mapController.getView().startDrop(PieceType.ROAD, CatanColor.valueOf(playerColor), false);
                            //mapController.startMove(PieceType.ROAD, false, true);
                        }
                    }

                }

                /* Begin Chat View Update */
                int oldChatSize = chatView.getEntries().size();
                int newChatSize = gameInformation.getChat().getLines().size();
                CatanColor color = CatanColor.WHITE;

                if (oldChatSize != newChatSize || (newChatSize == 0)) {
                    ArrayList<LogEntry> newChatEntries = new ArrayList<>();
                    for (MessageLine messageLine : gameInformation.getChat().getLines()) {
                        //get the color
                        for (PlayerInfo player : gameInformation.getPlayers()) {
                            if (player.getName().equals(messageLine.getSource())) {
                                color = CatanColor.valueOf(player.getColor().toUpperCase());
                                break;
                            }
                        }
                        LogEntry logEntry = new LogEntry(color, messageLine.getMessage());
                        newChatEntries.add(logEntry);
                    }
                    chatView.setEntries(newChatEntries);
                }
                /* End Chat View Update */

                /* Begin Turn Tracker Update */
               
                System.out.println("Turn Tracker Update");
                String status = gameInformation.getTurnTracker().getStatus();

                //TODO: need to figure out which statuses need to be disable or enabled
                turnTrackerView.updateGameState(status, false);
                //always update the local player's color
                
                
                for (PlayerInfo player : gameInformation.getPlayers()) {
                    boolean longestRoad = false;
                    boolean largestArmy = false;
                    boolean highlight = false;
                    int currentPlayerIndex = player.getPlayerIndex();
                    
                    turnTrackerView.initializePlayer(currentPlayerIndex, player.getName(), CatanColor.valueOf(player.getColor().toUpperCase()));
                    
                    //only update local player color for local player
                    if(player.getPlayerID() == ClientCommunicator.getSingleton().getPlayerId()){
                        
                        turnTrackerView.setLocalPlayerColor(CatanColor.valueOf(player.getColor().toUpperCase()));
                    }
                    //decide the awards
                    if (gameInformation.getTurnTracker().getLargestArmy() == currentPlayerIndex) {
                        largestArmy = true;
                    }
                    if (gameInformation.getTurnTracker().getLongestRoad() == currentPlayerIndex) {
                        longestRoad = true;
                    }
                    //decide who is current player
                    if (gameInformation.getTurnTracker().getCurrentTurn() == currentPlayerIndex) {
                        highlight = true;
                    }
                    turnTrackerView.updatePlayer(currentPlayerIndex, player.getVictoryPoints(), highlight, largestArmy, longestRoad);
                    
                }
                /* End Turn Tracker Update */

            } catch (Exception e) {
            }

        }

        // System.out.println("meow");
    }

}
