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
import client.data.GameInfo;
import client.managers.GameManager;
import client.map.MapController;
import client.map.MapView;
import client.proxy.FinishMove;
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

            GameInfo gameInformation = null;
            try {
                gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton()
                        .getGameModel(0 + "");
            } catch (ClientException ex) {
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
                if (status.equals("SecondRound")) {
                    if (!((MapView) mapController.getView()).getOverlay().isModalShowing() && firstTime) {
                        firstTime = false;

                        mapController.startMove(PieceType.SETTLEMENT, true, true);
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
                    Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
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
