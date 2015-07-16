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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ddennis
 */
public class PlayerWaitingViewPoller extends TimerTask {

    private JoinGameController joinGameController;
    private Timer playerWaitingTimer;
    private Timer joinGameTimer;
    private boolean firstTime = true;
    private boolean update = false;

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
        } catch (ClientException ex) {
            Logger.getLogger(PlayerWaitingViewPoller.class.getName()).log(Level.SEVERE, null, ex);
        }
            int activePlayer = ClientCommunicator.getSingleton().getPlayerId();
            for (GameInfo game : activeGames) {
                if (game.getId() == getJoinGameController().getGameId()) {
                    for (PlayerInfo player : game.getPlayers()) {
                        if (player.getId() != -1) {
                            activePlayers.add(player);
                        }
                    }
                    winner = game.getWinner();
                    PlayerInfo[] players = new PlayerInfo[activePlayers.size()];
                    activePlayers.toArray(players);
                    // only update if there is a new player added or removed
                    if(!firstTime){
                        if(activePlayers.size() != getJoinGameController().getPlayerWaitingView().getPlayers().length){
                            update = true;
                        }
                        else{
                            update = false;
                        }
                    }
                    else{
                        update = true;
                    }
                    if (firstTime || update || (activePlayers.size() == 4)) {
                        getJoinGameController().getPlayerWaitingView().setPlayers(players);
                        firstTime = false;
                    }
                    break;
                }
            }
            if (getJoinGameController().getSelectColorView().isModalShowing()) {
                getJoinGameController().getSelectColorView().closeModal();
            }
            if (getJoinGameController().getJoinGameView().isModalShowing()) {
                getJoinGameController().getJoinGameView().closeModal();
            }

            if (update) {
                getJoinGameController().getJoinAction().execute();
            }

            //the game is over (no need to update anymore)
            if (winner > 0) {
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
//                System.out.println("There are four players");
                ClientCommunicator.getSingleton().setJoinedGame(true);

           }
       
    }
}
