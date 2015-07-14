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
public class JoinGameViewPoller extends TimerTask {

    private JoinGameController joinGameController;

    public JoinGameViewPoller() {
    }

    public JoinGameViewPoller(JoinGameController joinGameController) {
        this.joinGameController = joinGameController;
    }

    public JoinGameController getJoinGameController() {
        return joinGameController;
    }

    public void setJoinGameController(JoinGameController joinGameController) {
        this.joinGameController = joinGameController;
    }

    public void run() {
        ArrayList<GameInfo> activeGames = new ArrayList();
        try {
            activeGames = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
            GameInfo[] games = new GameInfo[activeGames.size()];
            activeGames.toArray(games);

            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setId(ClientCommunicator.getSingleton().getPlayerId());
            playerInfo.setName(ClientCommunicator.getSingleton().getName());

            getJoinGameController().getJoinGameView().setGames(games, playerInfo);
        } catch (ClientException ex) {
            Logger.getLogger(Poller.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!getJoinGameController().getSelectColorView().isModalShowing() && !getJoinGameController().getPlayerWaitingView().isModalShowing() && !getJoinGameController().getNewGameView().isModalShowing()) {
            getJoinGameController().getJoinGameView().showModal();
        }
    }

}
