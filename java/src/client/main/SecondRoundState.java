/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
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
public class SecondRoundState extends State {

    final static int MAX_PLAYERS = 4;
    private int version = -1;
    private boolean firstTime = true;
    private String status = "SecondRound";

    @Override
    public void doAction(Controller controller) {
        MapController mapController = (MapController) controller;
        int playerIndex = -1;

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

            } catch (ClientException ex) {
                ex.printStackTrace();
                Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
            }

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

        }
    }

    @Override
    public String toString() {
        return "SecondRound{" + '}';
    }
}
