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
import client.communication.GameHistoryView;
import client.communication.LogEntry;
import client.data.MessageLine;
import client.data.PlayerInfo;
import client.data.ResourceList;
import client.discard.DiscardController;
import client.domestic.DomesticTradeController;
import client.points.PointsController;
import client.resources.ResourceBarController;
import client.resources.ResourceBarElement;
import client.roll.RollController;
import client.turntracker.TurnTrackerView;
import java.util.ArrayList;
import java.util.Date;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;

/**
 *
 * @author Samuel
 */
public class MapPoller extends TimerTask {

    private CatanPanel catanPanel;
    private int version;
    private boolean doneOnce;
    private boolean seenTrade;
    private int playerIndex;
    private String playerColor;
    private final int MAX_POINTS = 10;
    private boolean initializedPlayers;
    private boolean discardedOnce;

    public MapPoller() {
        super();

        discardedOnce=false;
        this.initializedPlayers = false;
        this.doneOnce = false;
        this.seenTrade = false;
        playerIndex = -1;
        playerColor = "";
        version = -1;
    }

    public CatanPanel getCatanPanel() {
        return catanPanel;
    }

    public void setCatanPanel(CatanPanel catanPanel) {
        this.catanPanel = catanPanel;
    }

    public void run() {
        Date time1 = new Date();
        if (ClientCommunicator.getSingleton().getJoinedGame()) {

            try {
                ChatView chatView = catanPanel.getLeftPanel().getChatView();
                GameHistoryView historyView = catanPanel.getLeftPanel().getHistoryView();
                TurnTrackerView turnTrackerView = catanPanel.getLeftPanel().getTurnView();
                RollController rollController = catanPanel.getRollController();
//              GameStatePanel gameStatePanel = catanPanel.getMidPanel().getGameStatePanel();
                PointsController pointsController = catanPanel.getRightPanel().getPointsController();
                ResourceBarController resourceBarController = catanPanel.getRightPanel().getResourceController();
                MapView mapView = (MapView) catanPanel.getMidPanel().getMapController().getView();

                GameManager gameManager = ClientCommunicator.getSingleton().getGameManager();

                GameInfo gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton()
                        .getGameModel(version + "");

                gameManager.initializeGame(gameInformation);
                version = gameInformation.getVersion();

                String status = gameInformation.getTurnTracker().getStatus();

                mapView.getController().initFromModel();

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
                
                /* Begin MapView Update */
                // If they haven't initialized before or it isn't the client's
                // turn
                if (gameManager.getGame().getTradeOffer() == null) {
                    seenTrade = false;
                    if (catanPanel.getMidPanel().getTradePanel().getDomesticController().getWaitingForOffer()) {
                        catanPanel.getMidPanel().getTradePanel().getDomesticController().setWaitingForOffer(false);
                        catanPanel.getMidPanel().getTradePanel().getDomesticController().getWaitOverlay().closeModal();
                        catanPanel.getMidPanel().getTradePanel().getDomesticController().getTradeOverlay().closeModal();
                        catanPanel.getMidPanel().getTradePanel().getDomesticController().getTradeOverlay().reset();
                    }
                }

                /* Begin Resource Bar Update */
                for (PlayerInfo player : gameInformation.getPlayers()) {
                    if (player.getPlayerIndex() == playerIndex) {
                        int sumCards = player.getRoads() + player.getCities() + player.getSettlements()
                                + player.getSoldiers();
                        // TODO: add logic to only update when they are
                        // different
                        resourceBarController.getView().setElementAmount(ResourceBarElement.BRICK,
                                player.getResources().getBrick());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.ORE,
                                player.getResources().getOre());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.SHEEP,
                                player.getResources().getSheep());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.WHEAT,
                                player.getResources().getWheat());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.WOOD,
                                player.getResources().getWood());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.ROAD, player.getRoads());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.CITY, player.getCities());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.SETTLEMENT,
                                player.getSettlements());
                        resourceBarController.getView().setElementAmount(ResourceBarElement.SOLDIERS,
                                player.getSoldiers());
                        resourceBarController.canBuildCity();
                        resourceBarController.canBuyCard();
                        resourceBarController.canBuildSettlement();
                        resourceBarController.canBuildRoad();
                        resourceBarController.canPlayCard();
                    }
                }
                /* End Resource Bar Update */
                DiscardController dis = catanPanel.getDiscardController();
                if(doneOnce && gameInformation.getTurnTracker().getCurrentTurn() == playerIndex  && rollController.getClickedOk() ){
                    if(status.equals("Discarding")&&!gameInformation.getPlayers().get(playerIndex).isDiscarded() && gameManager.getResourceManager() 
                                .getGameBanks().get(playerIndex).getResourcesCards().getTotalResources() > 7&&!discardedOnce) {
                        if (!dis.getDiscardView().isModalShowing()) {
                            dis.initFromModel();
                            dis.getDiscardView().showModal();
                            discardedOnce=true;
                        }
                    }
                    if(status.equals("Robbing")){
                        catanPanel.getMidPanel().getMapController().startMove(PieceType.ROBBER, true, true);
                        rollController.setClickedOk(false);
                        discardedOnce=false;
                    }
                }

                if (!doneOnce || gameInformation.getTurnTracker().getCurrentTurn() != playerIndex) {
                    doneOnce = true;
                    if (gameInformation.getTurnTracker().getCurrentTurn() == playerIndex) {
                        if (status.equals("Robbing")) {
                            catanPanel.getMidPanel().getMapController().startMove(PieceType.ROBBER, true, true);
                        }
                        
                        if (status.equals("Discarding") && !gameInformation.getPlayers().get(playerIndex).isDiscarded() && gameManager.getResourceManager()
                                .getGameBanks().get(playerIndex).getResourcesCards().getTotalResources() > 7) {
                            
                            if (!dis.getDiscardView().isModalShowing()) {
                                dis.initFromModel();
                                dis.getDiscardView().showModal();
                            }
                        }
                    } else {

                        /* Begin Discard Window Update */
                        if (status.equals("Discarding") && !gameInformation.getPlayers().get(playerIndex).isDiscarded() && gameManager.getResourceManager() 
                                .getGameBanks().get(playerIndex).getResourcesCards().getTotalResources() > 7) {
                            if (!dis.getDiscardView().isModalShowing()) {
                                dis.initFromModel();
                                dis.getDiscardView().showModal();
                            }
                        }
                        /* End Discard Window Update */

                        /* Begin tradeWindow Update */
                        if (gameManager.getGame().getTradeOffer() != null
                                && !catanPanel.getMidPanel().getTradePanel().getDomesticController().getWaitingForOffer()) {
                            if (!seenTrade) {
                                seenTrade = true;
                                if (gameManager.getGame().getTradeOffer().getReceiver() == playerIndex) {
                                    DomesticTradeController domesticController = catanPanel.getMidPanel().getTradePanel()
                                            .getDomesticController();
                                    boolean canAccept = true;

                                    ResourceList resourcesPlayerHas = gameManager.getResourceManager().getGameBanks()
                                            .get(playerIndex).getResourcesCards();

                                    if (resourcesPlayerHas.getBrick()
                                            + gameManager.getGame().getTradeOffer().getOffer().getBrick() < 0
                                            || resourcesPlayerHas.getOre()
                                            + gameManager.getGame().getTradeOffer().getOffer().getOre() < 0
                                            || resourcesPlayerHas.getSheep()
                                            + gameManager.getGame().getTradeOffer().getOffer().getSheep() < 0
                                            || resourcesPlayerHas.getWheat()
                                            + gameManager.getGame().getTradeOffer().getOffer().getWheat() < 0
                                            || resourcesPlayerHas.getWood()
                                            + gameManager.getGame().getTradeOffer().getOffer().getWood() < 0) {
                                        canAccept = false;
                                    }
                                    if (!domesticController.getAcceptOverlay().isModalShowing()) {
                                        domesticController.getAcceptOverlay().showModal();
                                        domesticController.getAcceptOverlay().setAcceptEnabled(canAccept);
                                    }
                                } else {
                                    if (gameManager.getGame().getTradeOffer().getSender() == playerIndex) {
                                        catanPanel.getMidPanel().getTradePanel().getDomesticController()
                                                .setWaitingForOffer(true);
                                        catanPanel.getMidPanel().getTradePanel().getDomesticController().startTrade();
                                        if (!catanPanel.getMidPanel().getTradePanel().getDomesticController()
                                                .getWaitOverlay().isModalShowing()) {
                                            catanPanel.getMidPanel().getTradePanel().getDomesticController()
                                                    .getWaitOverlay().showModal();
                                            catanPanel.getMidPanel().getTradePanel().getDomesticController()
                                                    .getWaitOverlay().setMessage("Waiting for Trade to Go Through");
                                        }
                                    }
                                }
                            }
                        }
                        /* End tradeWindow Update */
                    }
                }

                /* Begin Turn Tracker Update */
                // TODO: need to figure out which statuses need to be disable or
                // enabled
                boolean enable = false;
                /* Begin Roll Update */
                // every turn starts with a roll
                if (status.equals("Rolling") && playerIndex == gameInformation.getTurnTracker().getCurrentTurn()) {

                    if (!rollController.getRollView().isModalShowing()) {
                        rollController.getRollView().showModal();
                        rollController.getRollView().getRollTimer().start();
                        rollController.setClickedOk(false);
                    }

                }
                /* End Roll Update */

                if (status.equals("Playing") && playerIndex == gameInformation.getTurnTracker().getCurrentTurn()) {
                    enable = true;
                    // gameStatePanel.getButton().setBackground(CatanColor.valueOf(player.getColor().toUpperCase()).getJavaColor());
                    status = "Finish Turn";
                }

                /* Begin turnTracker Update */
                turnTrackerView.updateGameState(status, enable);
                for (PlayerInfo player : gameInformation.getPlayers()) {
                    boolean longestRoad = false;
                    boolean largestArmy = false;
                    boolean highlight = false;
                    int currentPlayerIndex = player.getPlayerIndex();

                    if (!initializedPlayers) {
                        turnTrackerView.initializePlayer(currentPlayerIndex, player.getName(),
                                CatanColor.valueOf(player.getColor().toUpperCase()));
                    }

                    // only update local player color for local player
                    if (player.getPlayerID() == ClientCommunicator.getSingleton().getPlayerId()) {
                        turnTrackerView.setLocalPlayerColor(CatanColor.valueOf(player.getColor().toUpperCase()));
                        pointsController.getPointsView().setPoints(player.getVictoryPoints());
                    }
                    // decide the awards
                    if (gameInformation.getTurnTracker().getLargestArmy() == currentPlayerIndex) {
                        largestArmy = true;
                    }
                    if (gameInformation.getTurnTracker().getLongestRoad() == currentPlayerIndex) {
                        longestRoad = true;
                    }
                    // decide who is current player
                    if (gameInformation.getTurnTracker().getCurrentTurn() == currentPlayerIndex) {
                        highlight = true;
                    }

                    turnTrackerView.updatePlayer(currentPlayerIndex, player.getVictoryPoints(), highlight, largestArmy,
                            longestRoad);

                    /* Begin Points Controller Update */
                    if (player.getVictoryPoints() == MAX_POINTS) {
                        pointsController.getFinishedView().setWinner(player.getName(),
                                (player.getPlayerIndex() == playerIndex));
                        if (!pointsController.getFinishedView().isModalShowing()) {
                            pointsController.getFinishedView().showModal();
                        }
                    }
                    /* End Points Controller Update */

                }
                turnTrackerView.updateGameState(status, status.equalsIgnoreCase("Finish Turn"));
                initializedPlayers = false;
                /* End Turn Tracker Update */

                /* Begin Chat View Update */
                int oldChatSize = chatView.getEntries().size();
                int newChatSize = gameInformation.getChat().getLines().size();
                CatanColor color = CatanColor.WHITE;

                if (oldChatSize != newChatSize || (newChatSize == 0)) {
                    ArrayList<LogEntry> newChatEntries = new ArrayList<>();
                    for (MessageLine messageLine : gameInformation.getChat().getLines()) {
                        // get the color
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
                        // get the color
                        for (PlayerInfo player : gameInformation.getPlayers()) {
                            if (player.getName().equals(messageLine.getSource())) {
                                colorHistory = CatanColor.valueOf(player.getColor().toUpperCase());
                                break;
                            }
                        }
                        LogEntry logEntry = new LogEntry(colorHistory, messageLine.getMessage());
                        newHistoryEntries.add(logEntry);
                    }
                    historyView.setEntries(newHistoryEntries);
                }
                /* End Game History View Update */

            } catch (Exception e) {
            	e.printStackTrace();
            }

        }

    }

}
