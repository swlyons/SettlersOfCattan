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
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ddennis
 */
public class JoinGameViewPoller extends TimerTask {

    private JoinGameController joinGameController;
    private boolean firstTime = true;
    private boolean update = false;

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
           //
        } catch (ClientException ex) {
            Logger.getLogger(JoinGameViewPoller.class.getName()).log(Level.SEVERE, null, ex);
        }
            GameInfo[] games = new GameInfo[activeGames.size()];
            activeGames.toArray(games);

            for (GameInfo game : activeGames) {
                for (int i = 0; i < game.getPlayers().size(); i++) {
                    if (game.getPlayers().get(i).getId() == -1) {
                        game.getPlayers().remove(i);
                        i--;    
                    }
                }
            }
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setId(ClientCommunicator.getSingleton().getPlayerId());
            playerInfo.setName(ClientCommunicator.getSingleton().getName());

            //determine if we need to update
            if (!firstTime) {
                 
                GameInfo[] oldGames = getJoinGameController().getJoinGameView().getGames();
                int oldGamePlayers = 0;
                int oldGameSize = oldGames.length;
                int newGamePlayers = 0;
                int newGameSize = activeGames.size();

                for (GameInfo game : oldGames) {
                    oldGamePlayers += game.getPlayers().size();
                }

                for (GameInfo game : activeGames) {
                    newGamePlayers += game.getPlayers().size();
                }

                if ((newGameSize != oldGameSize) || (oldGamePlayers != newGamePlayers)) {
                    
                    update = true;
                } else {
                    update = false;
                }
            }
            else{
                update = true;
                
                firstTime = false;
            }

        getJoinGameController().getJoinGameView().setGames(games, playerInfo, update);
        if (!getJoinGameController().getSelectColorView().isModalShowing() && !getJoinGameController().getPlayerWaitingView().isModalShowing() && !getJoinGameController().getNewGameView().isModalShowing()) {
            //only update when necessary
            if (update) {
               getJoinGameController().getJoinGameView().showModal();
            }
        }
    }

}
