/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.ClientException;
import client.base.Controller;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import shared.data.GameInfo;
import shared.data.Location;
import client.managers.GameManager;
import client.map.MapController;
import client.map.MapView;
import shared.model.FinishMove;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.definitions.PieceType;

/**
 *
 * @author ddennis
 */
public class FirstRoundState extends State {

    final static int MAX_PLAYERS = 4;
    int version = -1;
    private String status = "FirstRound";
    private int turns = 0;

    @Override
    public void doAction(Controller controller) {
        MapController mapController = (MapController) controller;
        int playerIndex = -1;

        System.out.println(this.toString());
        while (status.equals("FirstRound")) {
            GameManager gameManager = ClientCommunicator.getSingleton().getGameManager();

            GameInfo gameInformation = null;
            try {
                gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton()
                        .getGameModel(version + "");
            } catch (ClientException ex) {
            	ex.printStackTrace();
                Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
            }

            gameManager.initializeGame(gameInformation);
            version = gameInformation.getVersion();
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
                            mapController.startMove(PieceType.SETTLEMENT, true, true);
                        } else {
                            mapController.startMove(PieceType.ROAD, true, false);
                        }
                    }
                }
            }

            //end your turn
            if (mapController.isEndTurn()) {

                FinishMove fm = new FinishMove(gameManager.getGame().getTurnTracker().getCurrentTurn());
                fm.setType("finishTurn");
                fm.setPlayerIndex(playerIndex);
                try {
                    gameManager.initializeGame(ClientCommunicatorFascadeSettlersOfCatan.getSingleton().finishMove(fm));
                } catch (ClientException ex) {
                	ex.printStackTrace();
                    Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
                }
                mapController.setEndTurn(false);

                mapController.initFromModel();

                //first round is over
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "FirstRound{" + '}';
    }
}
