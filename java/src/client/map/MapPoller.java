/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map;

import client.catan.CatanPanel;
import client.catan.GameStatePanel;
import client.catan.TradePanel;
import client.communication.ChatView;
import java.util.TimerTask;
import client.data.GameInfo;
import client.managers.GameManager;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.communication.GameHistoryView;
import client.communication.LogEntry;
import client.data.MessageLine;
import client.data.PlayerInfo;
import client.data.ResourceList;
import client.domestic.DomesticTradeController;
import client.points.PointsController;
import client.resources.ResourceBarController;
import client.resources.ResourceBarElement;
import client.roll.RollController;
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
                GameHistoryView historyView = catanPanel.getLeftPanel().getHistoryView();
                TurnTrackerView turnTrackerView = catanPanel.getLeftPanel().getTurnView();
                RollController rollController = catanPanel.getRollController();
                GameStatePanel gameStatePanel = catanPanel.getMidPanel().getGameStatePanel();
                PointsController pointsController = catanPanel.getRightPanel().getPointsController();
                ResourceBarController resourceBarController = catanPanel.getRightPanel().getResourceController();
                MapView mapView = (MapView) catanPanel.getMidPanel().getMapController().getView();

                GameManager gm = ClientCommunicator.getSingleton().getGameManager();

                GameInfo gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton()
                        .getGameModel(version + "");
                version = gameInformation.getVersion();
                String status = gameInformation.getTurnTracker().getStatus();
                
                
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
                
                if(mapView.getController().isEndTurn()) {
                	catanPanel.getLeftPanel().getTurnTrackerController().endTurn();
                	firstTime = true;
                	mapView.getController().setEndTurn(false);
                }
                /* Begin MapView Update */
                // If they haven't initialized before or it isn't the client's
                // turn
                /*
                if(gameManager.getGame().getTradeOffer()!=null){
                    if(gameManager.getGame().getTradeOffer().getRecevier()==playerIndex){
                        DomesticTradeController domesticController = catanPanel.getMidPanel().getTradePanel().getDomesticController();
                        boolean canAccept = true;
                        
                        ResourceList resourcesPlayerHas = gameManager.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();
                        
                        if(resourcesPlayerHas.getBrick()+gameManager.getGame().getTradeOffer().getOffer().getBrick()<0||
                                resourcesPlayerHas.getOre()+gameManager.getGame().getTradeOffer().getOffer().getOre()<0||
                                resourcesPlayerHas.getSheep()+gameManager.getGame().getTradeOffer().getOffer().getSheep()<0||
                                resourcesPlayerHas.getWheat()+gameManager.getGame().getTradeOffer().getOffer().getWheat()<0||
                                resourcesPlayerHas.getWood()+gameManager.getGame().getTradeOffer().getOffer().getWood()<0){
                            canAccept = false;
                        }
                        
                        domesticController.getAcceptOverlay().showModal();
                        domesticController.getAcceptOverlay().setAcceptEnabled(canAccept);
                    }
                    
                    
                    if(gameManager.getGame().getTradeOffer().getSender()==playerIndex){}
                }
                  */  
                
                if (!firstInitialization || gameInformation.getTurnTracker().getCurrentTurn() != playerIndex) {

                    mapView.getController().initFromModel();
                    
                    /*if (gameInformation.getTurnTracker().getCurrentTurn() != playerIndex) // This boolean toggles on after your turn, so when the
                     // turn track comes around again, you get exactly one
                     // update on each of your turns. This way, your client
                     // will switch to a "It is your turn" state.
                     {
                     firstInitialization = false;
                     } else {
                     firstInitialization = true;
                     if (gm.getLocationManager().getSettledLocations().size() < 4) {
                     // We are on the first round.
                     if (gameInformation.getTurnTracker().getCurrentTurn() == playerIndex) {
                                
                                
                     // mapView.startDrop(PieceType.SETTLEMENT, CatanColor.valueOf(playerColor), false);
                     // mapView.getController().startMove(PieceType.SETTLEMENT, true, true);

                     //mapView.startDrop(PieceType.ROAD, CatanColor.valueOf(playerColor), false);
                     //mapView.getController().startMove(PieceType.ROAD, false, true);
                     }
                     } else if (gm.getLocationManager().getSettledLocations().size() < 8) {
                     // We are on the second round.
                     //mapController.getView().startDrop(PieceType.SETTLEMENT, CatanColor.valueOf(playerColor), false);
                     //mapController.startMove(PieceType.SETTLEMENT, true, true);
                     //mapController.getView().startDrop(PieceType.ROAD, CatanColor.valueOf(playerColor), false);
                     //mapController.startMove(PieceType.ROAD, false, true);
                     }
                     }*/
                    
                    // TODO: use this to TEST the overlay for the first two rounds (till we get the logic right)
                    if (gameInformation.getTurnTracker().getCurrentTurn() == playerIndex) {
                            
                            if(status.equals("FirstRound") && firstTime){
                                //mapView.startDrop(PieceType.SETTLEMENT, CatanColor.valueOf(playerColor), false);
                                mapView.getController().startMove(PieceType.SETTLEMENT, false, true);
                               	firstTime = false;
                            }
                            if(status.equals("SecondRound") && firstTime){
                                mapView.startDrop(PieceType.SETTLEMENT, CatanColor.valueOf(playerColor), false);
                                mapView.getController().startMove(PieceType.SETTLEMENT, false, true);
                                firstTime = false;
                            }     
                    }
                }
                /* End Map View Update */
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

                /* Begin Game History View Update */
                int oldHistorySize = historyView.getEntries().size();
                int newHistorySize = gameInformation.getLog().getLines().size();
                CatanColor colorHistory = CatanColor.WHITE;

                if (oldHistorySize != newHistorySize || (newHistorySize == 0)) {
                    ArrayList<LogEntry> newHistoryEntries = new ArrayList<>();
                    for (MessageLine messageLine : gameInformation.getLog().getLines()) {
                        //get the color
                        for (PlayerInfo player : gameInformation.getPlayers()) {
                            if (player.getName().equals(messageLine.getSource())) {
                                color = CatanColor.valueOf(player.getColor().toUpperCase());
                                break;
                            }
                        }
                        LogEntry logEntry = new LogEntry(color, messageLine.getMessage());
                        newHistoryEntries.add(logEntry);
                    }
                    historyView.setEntries(newHistoryEntries);
                }
                /* End Game History View Update */

                /* Begin Roll Update */
                //may need to add more so that this knows when to trigger (more than just it being your turn)
                if (playerIndex == gameInformation.getTurnTracker().getCurrentTurn() && status.equals("Rolling")) {
                    if (firstTime) {
                        firstTime = false;
                        rollController.getResultView().showModal();
                        rollController.getRollView().showModal();
                    }

                }
                /* End Roll Update */

                /* Begin Turn Tracker Update */
                //TODO: need to figure out which statuses need to be disable or enabled
                boolean enable = false;
                if (status.equals("Playing") && playerIndex == gameInformation.getTurnTracker().getCurrentTurn()) {
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
                } else {
                    if(!status.contains("Round")){
                        status = "Waiting For Other Players";
                    }
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
                        pointsController.getPointsView().setPoints(player.getVictoryPoints());
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
                    if (player.getVictoryPoints() == MAX_POINTS) {
                        pointsController.getFinishedView().setWinner(player.getName(), (player.getPlayerIndex() == playerIndex));
                        pointsController.getFinishedView().showModal();
                    }
                    /* End  Points Controller Update */

                }
                /* End Turn Tracker Update */

                /* Begin Resource Bar Update */
                for (PlayerInfo player : gameInformation.getPlayers()) {
                    if (player.getPlayerIndex() == playerIndex) {
                        int sumCards = player.getRoads() + player.getCities() + player.getSettlements() + player.getSoldiers();
                        //TODO: add logic to only update when they are different
                        resourceBarController.getView().setElementAmount(ResourceBarElement.BRICK, player.getResources().getBrick());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.ORE, player.getResources().getOre());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.SHEEP, player.getResources().getSheep());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.WHEAT, player.getResources().getWheat());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.WOOD, player.getResources().getWood());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.ROAD, player.getRoads());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.CITY, player.getCities());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.SETTLEMENT, player.getSettlements());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.SOLDIERS, player.getSoldiers());

                        /* TODO: Figure out what to do with these (not sure what they are)
                         resourceBarController.getView().setElementAmount(ResourceBarElement.BUY_CARD, player.getSoldiers());
                         resourceBarController.getView().setElementAmount(ResourceBarElement.PLAY_CARD, player.getSoldiers());*/
                    }
                }
                /* End Resource Bar Update */

            } catch (Exception e) {
            }

        }

        // System.out.println("meow");
    }

}
