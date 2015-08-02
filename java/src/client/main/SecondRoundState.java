/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.catan.CatanPanel;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import static client.main.FirstRoundState.MAX_POINTS;
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
public class SecondRoundState extends State {

    final static int MAX_PLAYERS = 4;
    final static int MAX_POINTS = 10;
    private int version = -1;
    private boolean firstTime = true;
    private String status = "SecondRound";
    private TurnTrackerView turnTrackerView;
    private PointsController pointsController;
    private CatanPanel catanPanel;
    
    public SecondRoundState(){
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
        
        System.out.println(this.toString());
        while (status.equals("SecondRound")) {
            GameManager gameManager = ClientCommunicator.getSingleton().getGameManager();
            try {
                GameInfo gameInformation = gameInformation = ClientFascade.getSingleton()
                        .getGameModel("?version=" + -1);
                gameManager.initializeGame(gameInformation);
                version = gameInformation.getVersion();

                if (gameInformation.getTurnTracker().getStatus().equals("FirstRound")) {
                    try {
                        Thread.sleep(3000);
                        continue;
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;

                    }
                }
                status = gameInformation.getTurnTracker().getStatus();

                Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
                int size = gameManager.getGame().getPlayers().size();
                for (int i = 0; i < size; i++) {
                    if (gameManager.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                        playerIndex = gameManager.getGame().getPlayers().get(i).getPlayerIndex();
                        break;
                    }
                }

                if (playerIndex == gameInformation.getTurnTracker().getCurrentTurn()) {
                    if (status.equals("SecondRound")) {
                        if (((MapView) mapController.getView()).getOverlay() == null
                                || (firstTime && !((MapView) mapController.getView()).getOverlay().isModalShowing())) {
                            firstTime = false;
                            mapController.initFromModel();
                            int settlements = 0;
                            for (Location location : gameManager.getLocationManager().getSettledLocations()) {
                                if (playerIndex == location.getOwnerID()) {
                                    settlements++;
                                }
                            }
                            if (settlements == 1) {
                                mapController.startMove(PieceType.SETTLEMENT, true, false);
                            } else {
                                mapController.startMove(PieceType.ROAD, true, true);
                            }
                        }
                    }
                }

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
                                (player.getPlayerIndex() == index));
                        if (!pointsController.getFinishedView().isModalShowing()) {
                            pointsController.getFinishedView().showModal();
                        }
                    }
                    /* End Points Controller Update */

                }
                /**
                 * ******* End Turn Track Update *******
                 */
                //end your turn
                if (mapController.isEndTurn()) {

                    FinishMove fm = new FinishMove(gameManager.getGame().getTurnTracker().getCurrentTurn());
                    fm.setType("finishTurn");
                    fm.setPlayerIndex(playerIndex);
                    try {
                        gameManager.initializeGame(ClientFascade.getSingleton().finishMove(fm));
                    } catch (ClientException ex) {
                        Logger.getLogger(SecondRoundState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    mapController.setEndTurn(false);

                    mapController.initFromModel();

                    //second round is over
                    break;
                }
            } catch (ClientException ex) {
                ex.printStackTrace();
                Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public String toString() {
        return "SecondRound{" + '}';
    }
}
