/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map;

import client.catan.CatanPanel;
import client.catan.GameStatePanel;
import client.communication.ChatView;
import java.util.TimerTask;
import client.data.GameInfo;
import client.managers.GameManager;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.communication.LogEntry;
import client.data.MessageLine;
import client.data.PlayerInfo;
import client.points.PointsController;
import client.roll.RollController;
import client.turntracker.TurnTrackerView;
import java.util.ArrayList;
import shared.definitions.CatanColor;

/**
 *
 * @author Samuel
 */
public class MapPoller extends TimerTask {

    private CatanPanel catanPanel;
    private int version;
    private boolean firstInitialization;
    private boolean firstTime;
    private int playerIndex;
    private String playerColor;
    private final int MAX_POINTS = 10;

    public MapPoller(CatanPanel catanPanel) {
        super();
        this.catanPanel = catanPanel;
        this.firstInitialization = false;
        this.firstTime = true;
        playerIndex = -1;
        playerColor = "";
        version = -1;
    }

    public void run() {
        if (ClientCommunicator.getSingleton().getJoinedGame()) {
            try {
                ChatView chatView = catanPanel.getLeftPanel().getChatView();
                TurnTrackerView turnTrackerView = catanPanel.getLeftPanel().getTurnView();
                RollController rollController = catanPanel.getRollController();
                GameStatePanel gameStatePanel = catanPanel.getMidPanel().getGameStatePanel();
                PointsController pointsController = catanPanel.getRightPanel().getPointsController();
                
                GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                
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

                /* Begin Roll Update */
                //may need to add more so that this knows when to trigger (more than just it being your turn)
                if (playerIndex == gameInformation.getTurnTracker().getCurrentTurn()) {
                    if (firstTime) {
                        firstTime = false;
                        rollController.getResultView().showModal();
                        rollController.getRollView().showModal();
                    }

                }
                /* End Roll Update */

                /* Begin Turn Tracker Update */
                String status = gameInformation.getTurnTracker().getStatus();

                //TODO: need to figure out which statuses need to be disable or enabled
                boolean enable = false;
                if(status.equals("Playing") && playerIndex == gameInformation.getTurnTracker().getCurrentTurn()){
                    enable = true;
                    //set the button color
                    for (PlayerInfo player : gameInformation.getPlayers()) {
                            if (player.getPlayerIndex() == playerIndex) {
                                //TODO: figure out how to change the actual button's color
                                gameStatePanel.getButton().setBackground(CatanColor.valueOf(player.getColor().toUpperCase()).getJavaColor());
                                break;
                            }
                        }
                    
                    status = "Finish Turn";
                }
                else{
                    status = "Waiting For Other Players";
                }
                
                turnTrackerView.updateGameState(status, enable);
                //always update the local player's color

                for (PlayerInfo player : gameInformation.getPlayers()) {
                    boolean longestRoad = false;
                    boolean largestArmy = false;
                    boolean highlight = false;
                    int currentPlayerIndex = player.getPlayerIndex();

                    turnTrackerView.initializePlayer(currentPlayerIndex, player.getName(), CatanColor.valueOf(player.getColor().toUpperCase()));

                    //only update local player color for local player
                    if (player.getPlayerID() == ClientCommunicator.getSingleton().getPlayerId()) {

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
                    
                    /* Begin Points Controller Update */
                    pointsController.getPointsView().setPoints(player.getVictoryPoints());
                    if(player.getVictoryPoints() == MAX_POINTS){
                        pointsController.getFinishedView().setWinner(player.getName(), (player.getPlayerIndex() == playerIndex));
                        pointsController.getFinishedView().showModal();
                    }
                    /* End  Points Controller Update */

                }
                /* End Turn Tracker Update */
                
                

            } catch (Exception e) {
            }

        }

        // System.out.println("meow");
    }

}
