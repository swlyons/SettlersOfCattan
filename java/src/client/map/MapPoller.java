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
import shared.data.GameInfo;
import client.managers.GameManager;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import client.communication.GameHistoryView;
import client.communication.LogEntry;
import shared.data.MessageLine;
import shared.data.PlayerInfo;
import shared.data.ResourceList;
import client.discard.DiscardController;
import client.domestic.DomesticTradeController;
import client.points.PointsController;
import client.resources.ResourceBarController;
import client.resources.ResourceBarElement;
import client.roll.RollController;
import client.turntracker.TurnTrackerView;
import java.util.ArrayList;
import java.util.Timer;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.definitions.ResourceType;

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
    private boolean discardedOnce;
    private Timer gamePlayTimer;

    public MapPoller() {
        super();
        discardedOnce = false;
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
        if (ClientCommunicator.getSingleton().getJoinedGame()) {

            try {
                ChatView chatView = catanPanel.getLeftPanel().getChatView();
                GameHistoryView historyView = catanPanel.getLeftPanel().getHistoryView();
                TurnTrackerView turnTrackerView = catanPanel.getLeftPanel().getTurnView();
                RollController rollController = catanPanel.getRollController();
                DiscardController dis = catanPanel.getDiscardController();
                GameStatePanel gameStatePanel = catanPanel.getMidPanel().getGameStatePanel();

                PointsController pointsController = catanPanel.getRightPanel().getPointsController();
                ResourceBarController resourceBarController = catanPanel.getRightPanel().getResourceController();
                MapView mapView = (MapView) catanPanel.getMidPanel().getMapController().getView();

                GameManager gameManager = ClientCommunicator.getSingleton().getGameManager();
                String status = "";
                GameInfo gameInformation = ClientFascade.getSingleton()
                        .getGameModel("?version=" + (gameManager.getGame().getVersion() - 1));
                status = gameInformation.getTurnTracker().getStatus();

                gameManager.initializeGame(gameInformation);
                version = gameInformation.getVersion();
                mapView.getController().initFromModel();
               
                //these must be done whether the version changes or not
                // <editor-fold desc="Version Independent">
                /*  ---------- BEGIN ----------THESE UPDATES WILL BE DONE WHETHER THE VERSION CHANGES OR NOT */
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
                //update the button color to match whomever the current player is
                if (gameStatePanel.getBackground() != CatanColor.valueOf(gameInformation.getPlayers().get(gameInformation.getTurnTracker().getCurrentTurn()).getColor().toUpperCase()).getJavaColor()) {

                    gameStatePanel.getCentered().setBackground(CatanColor.valueOf(gameInformation.getPlayers().get(gameInformation.getTurnTracker().getCurrentTurn()).getColor().toUpperCase()).getJavaColor());
                }

                // Toggle the button to Finish Turn (this ultimately controls whether the button is enable or not)
                if (status.equals("Playing") && playerIndex == gameInformation.getTurnTracker().getCurrentTurn()) {
                    if (rollController.getRollView().isModalShowing()) {
                        rollController.getRollView().closeModal();
                        //may not be necessary, but just in case the timer is running
                        if (rollController.getRollView().getRollTimer().isRunning()) {
                            rollController.getRollView().getRollTimer().stop();
                        }
                    }
                    //enable the domestic trade button when it's your turn
                    catanPanel.getMidPanel().getTradePanel()
                            .getDomesticController().getTradeView().enableDomesticTrade(true);

                    status = "Finish Turn";
                } else if (status.equals("Rolling") && playerIndex != gameInformation.getTurnTracker().getCurrentTurn()) {
                    //disable the domestic trade button when it's your turn
                    catanPanel.getMidPanel().getTradePanel()
                            .getDomesticController().getTradeView().enableDomesticTrade(false);

                }

                // <editor-fold desc="Roll Update">
                /* Begin Roll Update */
                if (status.equals("Rolling") && playerIndex == gameInformation.getTurnTracker().getCurrentTurn()) {

                    if (!rollController.getRollView().isModalShowing()) {
                        rollController.getRollView().showModal();
                        rollController.getRollView().getRollTimer().start();
                        rollController.setClickedOk(false);
                    }
                    //enable = true;
                }
                /* End Roll Update */
                // </editor-fold>

                // <editor-fold desc="Turn Tracker Update">
                /**
                 * ******* Begin Turn Tracker Update ********
                 */
                for (PlayerInfo player : gameInformation.getPlayers()) {

                    boolean longestRoad = false;
                    boolean largestArmy = false;
                    boolean highlight = false;
                    int index = player.getPlayerIndex();
                    
                    // only update local player color for local player
                    if (player.getPlayerID() == ClientCommunicator.getSingleton().getPlayerId()) { 
                        turnTrackerView.setLocalPlayerColor(CatanColor.valueOf(player.getColor().toUpperCase()));
                        pointsController.getPointsView().setPoints(player.getVictoryPoints());
                    }
                    // decide the awards
                    if (gameInformation.getTurnTracker().getLargestArmy() == index) {
                        largestArmy = true;
                    }
                    if (gameInformation.getTurnTracker().getLongestRoad() == index) {
                        longestRoad = true;
                    }
                    // decide who is current player
                    if (gameInformation.getTurnTracker().getCurrentTurn() == index) {
                        highlight = true;
                    }

                    turnTrackerView.updatePlayer(index, player.getVictoryPoints(), highlight, largestArmy,
                            longestRoad);

                    /* Begin Points Controller Update */
                    if (player.getVictoryPoints() == MAX_POINTS) {

                        pointsController.getFinishedView().setWinner(player.getName(),
                                (playerIndex == index));
                        if (!pointsController.getFinishedView().isModalShowing()) {
                            //Game is over (stop the timer)\
                            //also cancel the gamePlay poller
                            gamePlayTimer.cancel();
                            gamePlayTimer.purge();

                            //hide the game so no interaction   
                            catanPanel.setVisible(false);
                            pointsController.getFinishedView().showModal();

                        }
                    }
                    /* End Points Controller Update */

                }
                /**
                 * ******* End Turn Track Update ********
                 */
                // </editor-fold>

                //updates the game state panel with the new status
                turnTrackerView.updateGameState(status, status.equalsIgnoreCase("Finish Turn"));

                //<editor-fold desc="Chat View Update">
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
                //</editor-fold>

                //<editor-fold desc="Game History Update">
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
                // </editor-fold>

                //<editor-fold desc="Trade Panel Update">
                // If they haven't initialized before or it isn't the client's turn
                /* Begin Trade Panel Update */
                if (gameManager.getGame().getTradeOffer() == null) {
                    seenTrade = false;
                    if (catanPanel.getMidPanel().getTradePanel().getDomesticController().getWaitingForOffer()) {
                        catanPanel.getMidPanel().getTradePanel().getDomesticController().setWaitingForOffer(false);
                        catanPanel.getMidPanel().getTradePanel().getDomesticController().getWaitOverlay().closeModal();
                        catanPanel.getMidPanel().getTradePanel().getDomesticController().getTradeOverlay().closeModal();
                        catanPanel.getMidPanel().getTradePanel().getDomesticController().getTradeOverlay().reset();
                    }
                }
                /* End Domestic Trade Panel Update */
                // </editor-fold>

                //<editor-fold desc="Resource Bar Update">
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
                // </editor-fold>
                //<editor-fold desc="Discard Controller">
                /* Begin Discard Controller */
                if (doneOnce && gameInformation.getTurnTracker().getCurrentTurn() == playerIndex && rollController.getClickedOk()) {
                    if (status.equals("Discarding") && !gameInformation.getPlayers().get(playerIndex).isDiscarded() && gameManager.getResourceManager()
                            .getGameBanks().get(playerIndex).getResourcesCards().getTotalResources() > 7 && !discardedOnce) {
                        if (!dis.getDiscardView().isModalShowing()) {
                            dis.initFromModel();
                            dis.getDiscardView().showModal();
                            discardedOnce = true;
                        }
                    }
                    if (status.equals("Robbing")) {
                        mapView.getController().startMove(PieceType.ROBBER, true, false);
                        rollController.setClickedOk(false);

                        discardedOnce = false;
                    }
                }
                /* End Discard Controller */
                //</editor-fold>

                //<editor-fold desc="Updated Not Your Turn">
                /* Begin Updated Not Your Turn */
                // Things need updating when it's not the current player's turn
                if (!doneOnce || gameInformation.getTurnTracker().getCurrentTurn() != playerIndex) {
                    doneOnce = true;
                    if (gameInformation.getTurnTracker().getCurrentTurn() == playerIndex) {
                        if (status.equals("Robbing")) {
                            mapView.getController().startMove(PieceType.ROBBER, true, true);
                        }

                        if (status.equals("Discarding") && !gameInformation.getPlayers().get(playerIndex).isDiscarded() && gameManager.getResourceManager()
                                .getGameBanks().get(playerIndex).getResourcesCards().getTotalResources() > 7) {

                            if (!dis.getDiscardView().isModalShowing()) {
                                dis.initFromModel();
                                dis.getDiscardView().showModal();
                            }
                        }
                    } else {

                        //<editor-fold desc="Discard Window Update">
                        /* Begin Discard Window Update */
                        if (status.equals("Discarding") && !gameInformation.getPlayers().get(playerIndex).isDiscarded() && gameManager.getResourceManager()
                                .getGameBanks().get(playerIndex).getResourcesCards().getTotalResources() > 7) {
                            if (!dis.getDiscardView().isModalShowing()) {
                                dis.initFromModel();
                                dis.getDiscardView().showModal();
                            }
                        }
                        /* End Discard Window Update */
                        // </editor-fold>

                        //<editor-fold desc="Trade Window Update">
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
                                        ResourceList offer = gameManager.getGame().getTradeOffer().getOffer();
                                        String name = ClientCommunicator.getSingleton().getGameManager().getGame().getPlayers().get(playerIndex).getName();
                                        domesticController.getAcceptOverlay().setPlayerName(name);

                                        if (offer.getBrick() > 0) {
                                            domesticController.getAcceptOverlay().addGetResource(ResourceType.brick, offer.getBrick());
                                        } else if (offer.getBrick() < 0) {
                                            domesticController.getAcceptOverlay().addGiveResource(ResourceType.brick, Math.abs(offer.getBrick()));
                                        }
                                        if (offer.getOre() > 0) {
                                            domesticController.getAcceptOverlay().addGetResource(ResourceType.ore, offer.getOre());
                                        } else if (offer.getOre() < 0) {
                                            domesticController.getAcceptOverlay().addGiveResource(ResourceType.ore, Math.abs(offer.getOre()));
                                        }

                                        if (offer.getSheep() > 0) {
                                            domesticController.getAcceptOverlay().addGetResource(ResourceType.sheep, offer.getSheep());
                                        } else if (offer.getSheep() < 0) {
                                            domesticController.getAcceptOverlay().addGiveResource(ResourceType.sheep, Math.abs(offer.getSheep()));
                                        }

                                        if (offer.getWheat() > 0) {
                                            domesticController.getAcceptOverlay().addGetResource(ResourceType.wheat, offer.getWheat());
                                        } else if (offer.getWheat() < 0) {
                                            domesticController.getAcceptOverlay().addGiveResource(ResourceType.wheat, Math.abs(offer.getWheat()));
                                        }

                                        if (offer.getWood() > 0) {
                                            domesticController.getAcceptOverlay().addGetResource(ResourceType.wood, offer.getWood());
                                        } else if (offer.getWood() < 0) {
                                            domesticController.getAcceptOverlay().addGiveResource(ResourceType.wood, Math.abs(offer.getWood()));
                                        }

                                        //set all the information for the trade overlay
                                        domesticController.getAcceptOverlay().addTradeInformation();

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
                        // </editor-fold>
                    }
                }
                // </editor-fold>

                /* ---------- END ----------THESE UPDATES WILL BE DONE WHETHER THE VERSION CHANGES OR NOT */
                // </editor-fold>
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void setGamePlayTimer(Timer gamePlayTimer) {
        this.gamePlayTimer = gamePlayTimer;
    }

}
