/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.join;

import client.ClientException;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.proxy.Poller;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.definitions.CatanColor;

/**
 *
 * @author ddennis
 */
public class PlayerWaitingViewPoller extends TimerTask {

    private JoinGameController joinGameController;
    private Timer playerWaitingTimer;
    private Timer joinGameTimer;

    public PlayerWaitingViewPoller() {
    }

    public PlayerWaitingViewPoller(JoinGameController joinGameController, Timer playerWaitingTimer, Timer joinGameTimer) {
        this.joinGameController = joinGameController;
        this.playerWaitingTimer = playerWaitingTimer;
        this.joinGameTimer = joinGameTimer;
    }

    public JoinGameController getJoinGameController() {
        return joinGameController;
    }

    public void setJoinGameController(JoinGameController joinGameController) {
        this.joinGameController = joinGameController;
    }

    public void run() {
        ArrayList<PlayerInfo> activePlayers = new ArrayList();
        ArrayList<GameInfo> activeGames = new ArrayList();
        int winner = -1;
        try {
            activeGames = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
            int activePlayer = ClientCommunicator.getSingleton().getPlayerId();
            for (GameInfo game : activeGames) {
                if (game.getId() == getJoinGameController().getGameId()) {
                    for (PlayerInfo player : game.getPlayers()) {
                        if (player.getId() != -1) {
                            activePlayers.add(player);
                            //current player should be able to choose another color
                            if (activePlayer != player.getId()) {
                                getJoinGameController().getSelectColorView().setColorEnabled(CatanColor.valueOf(player.getColor().toUpperCase()), false);
                            }
                        }
                    }
                    winner = game.getWinner();
                    PlayerInfo[] players = new PlayerInfo[activePlayers.size()];
                    activePlayers.toArray(players);
                    getJoinGameController().getPlayerWaitingView().setPlayers(players);
                    break;
                }
            }
            if (getJoinGameController().getSelectColorView().isModalShowing()) {
                getJoinGameController().getSelectColorView().closeModal();
            }
            if (getJoinGameController().getJoinGameView().isModalShowing()) {
                getJoinGameController().getJoinGameView().closeModal();
            }
            System.out.println("Here");
            getJoinGameController().getJoinAction().execute();

            //the game is over (no need to update anymore)
            if (winner > -1) {
                joinGameTimer.cancel();
                joinGameTimer.purge();
                playerWaitingTimer.cancel();
                playerWaitingTimer.purge();
            }
            //make the map accessible
            if (activePlayers.size() == 4) {
                joinGameTimer.cancel();
                joinGameTimer.purge();
                playerWaitingTimer.cancel();
                playerWaitingTimer.purge();
                getJoinGameController().getJoinGameView().closeModal();
            }
        } catch (ClientException ex) {
            Logger.getLogger(Poller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
