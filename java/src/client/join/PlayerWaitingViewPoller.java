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
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ddennis
 */
public class PlayerWaitingViewPoller extends TimerTask {

    private JoinGameController joinGameController;

    public PlayerWaitingViewPoller() {
    }

    public PlayerWaitingViewPoller(JoinGameController joinGameController) {
        this.joinGameController = joinGameController;
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
        try {
            activeGames = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
            for (GameInfo game : activeGames) {
                if (game.getId() == getJoinGameController().getGameId()) {
                    for (PlayerInfo player : game.getPlayers()) {
                        if (player.getId() != -1) {
                            activePlayers.add(player);
                        }
                    }
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
            
            getJoinGameController().getJoinAction().execute();
        } catch (ClientException ex) {
            Logger.getLogger(Poller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
