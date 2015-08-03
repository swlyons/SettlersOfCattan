/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.catan.CatanPanel;
import client.catan.GameStatePanel;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import shared.data.GameInfo;
import shared.data.Location;
import client.managers.GameManager;
import client.map.MapController;
import client.map.MapView;
import client.points.PointsController;
import client.turntracker.TurnTrackerView;
import shared.model.FinishMove;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.data.PlayerInfo;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;

/**
 *
 * @author ddennis
 */
public class FirstRoundState extends State {

    final static int MAX_PLAYERS = 4;
    final static int MAX_POINTS = 10;
    int version = -1;
    private String status = "FirstRound";
    private boolean initializedPlayers = false;
    private TurnTrackerView turnTrackerView;
    private PointsController pointsController;
    private CatanPanel catanPanel;

    public FirstRoundState() {
    }

    public CatanPanel getCatanPanel() {
        return catanPanel;
    }

    public void setCatanPanel(CatanPanel catanPanel) {
        this.catanPanel = catanPanel;
    }

    @Override
    public void doAction(Controller controller) {
        MapController mapController = (MapController) controller;
        int playerIndex = -1;

        turnTrackerView = catanPanel.getLeftPanel().getTurnView();
        pointsController = catanPanel.getRightPanel().getPointsController();

        GameManager gameManager = null;
        GameStatePanel gameStatePanel = catanPanel.getMidPanel().getGameStatePanel();

        System.out.println(this.toString());
        while (status.equals("FirstRound")) {
            gameManager = ClientCommunicator.getSingleton().getGameManager();
            try {
                GameInfo gameInformation = ClientFascade.getSingleton()
                        .getGameModel("?version=" + -1);

                status = gameInformation.getTurnTracker().getStatus();
                gameManager.initializeGame(gameInformation);
                version = gameInformation.getVersion();

                if (gameStatePanel.getBackground() != CatanColor.valueOf(gameInformation.getPlayers().get(gameInformation.getTurnTracker().getCurrentTurn()).getColor().toUpperCase()).getJavaColor()) {

                    gameStatePanel.getCentered().setBackground(CatanColor.valueOf(gameInformation.getPlayers().get(gameInformation.getTurnTracker().getCurrentTurn()).getColor().toUpperCase()).getJavaColor());
                }
                mapController.initFromModel();
                Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
                int size = gameManager.getGame().getPlayers().size();

                for (int i = 0; i < size; i++) {
                    if (gameManager.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                        playerIndex = gameManager.getGame().getPlayers().get(i).getPlayerIndex();
                        break;
                    }
                }

                if (playerIndex == gameInformation.getTurnTracker().getCurrentTurn()) {
                    if (status.equals("FirstRound")) {
                        boolean builtSettlement = false;
                        for (Location location : gameManager.getLocationManager().getSettledLocations()) {
                            if (location.getOwnerID() == playerIndex) {
                                builtSettlement = true;
                            }
                        }
                        if (((MapView) mapController.getView()).getOverlay() == null) {
                            mapController.initFromModel();
                            if (!builtSettlement) {
                                mapController.startMove(PieceType.SETTLEMENT, true, false);
                            } else {
                                mapController.startMove(PieceType.ROAD, true, false);
                            }
                        }
                    }
                }
                /* ******* Begin Turn Tracker Update *********/
                /* Begin Turn Tracker Initialization */
                for (PlayerInfo player : gameInformation.getPlayers()) {
                    if (!initializedPlayers) {
                        turnTrackerView.initializePlayer(player.getPlayerIndex(), player.getName(),
                                CatanColor.valueOf(player.getColor().toUpperCase()));

                    }
                }
                //only initialize the players once (multiple times creates garbled data)
                initializedPlayers = true;
                /* End Turn Tracker Initialization */
                for (PlayerInfo player : gameInformation.getPlayers()) {
                    if (initializedPlayers) {
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
                    }

                }
                /**
                 * ******* End Turn Track Update *******
                 */
                turnTrackerView.updateGameState("FirstRound", false);
                //end your turn
                if (mapController.isEndTurn()) {
                    FinishMove fm = new FinishMove(gameManager.getGame().getTurnTracker().getCurrentTurn());
                    fm.setType("finishTurn");
                    fm.setPlayerIndex(playerIndex);
                    try {
                        ClientFascade.getSingleton().finishMove(fm);
                        gameInformation = ClientFascade.getSingleton()
                                .getGameModel("?version=" + -1);
                        gameManager.initializeGame(gameInformation);
                        //update the highligher at the end of the turn since we are leaving this state
                        for (PlayerInfo player : gameManager.getGame().getPlayers()) {
                            turnTrackerView.updatePlayer(player.getPlayerIndex(), player.getVictoryPoints(), player.getPlayerIndex() == gameInformation.getTurnTracker().getCurrentTurn(), false,
                                    false);
                        }
                    } catch (ClientException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    mapController.setEndTurn(false);
                    mapController.initFromModel();

                    //first round is over if last player has played road and settlement
                    if (status.equals("SecondRound")) {
                        break;
                    }
                }
            } catch (ClientException ex) {
                ex.printStackTrace();
                Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //check before leaving the state (need to update the player the just ended his turn's display)
        GameInfo gameInformation = null;
        try {
            gameInformation = ClientFascade.getSingleton().getGameModel("?version=" + -1);
        } catch (ClientException ex) {
            Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (PlayerInfo player : gameInformation.getPlayers()) {
            turnTrackerView.updatePlayer(player.getPlayerIndex(), player.getVictoryPoints(), player.getPlayerIndex() == gameInformation.getTurnTracker().getCurrentTurn(), false,
                    false);
        }
    }

    @Override
    public String toString() {
        return "FirstRound{" + '}';
    }
}
