/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.join;

import client.main.ClientException;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import shared.data.GameInfo;
import shared.data.PlayerInfo;
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
    private boolean update = false;
    private final int MAX_PLAYERS = 4;

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
            activeGames = ClientFascade.getSingleton().listGames();
        } catch (ClientException ex) {
            ex.printStackTrace();
            Logger.getLogger(PlayerWaitingViewPoller.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (GameInfo game : activeGames) {
            if (game.getId() == getJoinGameController().getGameId()) {
                for (PlayerInfo player : game.getPlayers()) {
                    if (player.getId() != -1) {
                        activePlayers.add(player);
                    }
                }
                PlayerInfo[] players = new PlayerInfo[activePlayers.size()];
                activePlayers.toArray(players);
                PlayerInfo[] oldPlayers = getJoinGameController().getPlayerWaitingView().getPlayers();
                // only update if there is a new player added or removed
                if (oldPlayers == null) {
                    update = true;
                } else {
                    update = (activePlayers.size() != getJoinGameController().getPlayerWaitingView().getPlayers().length);
                }

                if (update) {
                    getJoinGameController().getPlayerWaitingView().setPlayers(players);

                }
                break;
            }
        }

        if (update) {
            if (getJoinGameController().getPlayerWaitingView().isModalShowing()) {
                getJoinGameController().getPlayerWaitingView().closeModal();
            }
            getJoinGameController().getJoinAction().execute();
        }

        //make the map accessible
        if (activePlayers.size()
                == MAX_PLAYERS) {

            if (getJoinGameController().getPlayerWaitingView().isModalShowing()) {
                getJoinGameController().getPlayerWaitingView().closeModal();
            }
            //switch to the next state
            ClientCommunicator.getSingleton().setJoinedGame(true);

            //start the game cause we have 4 players
            ((PlayerWaitingController) getJoinGameController().getPlayerWaitingView().getController()).ready();

        }

    }
}
