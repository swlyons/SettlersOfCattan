/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.join;

import client.main.ClientException;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import java.awt.Dimension;
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
public class JoinGameViewPoller extends TimerTask {

    private JoinGameController joinGameController;
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

    @Override
    public void run() {
        ArrayList<GameInfo> activeGames = new ArrayList();

        try {
            activeGames = ClientFascade.getSingleton().listGames();
        } catch (ClientException ex) {
            ex.printStackTrace();
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
        int newGamePlayers = 0;

        GameInfo[] oldGames = getJoinGameController().getJoinGameView().getGames();
        int oldGamePlayers = 0;

        if (oldGames == null) {
            update = true;
        } else {
            int oldGameSize = oldGames.length;

            int newGameSize = activeGames.size();

            for (GameInfo game : oldGames) {
                oldGamePlayers += game.getPlayers().size();
            }

            for (GameInfo game : activeGames) {
                newGamePlayers += game.getPlayers().size();
            }

            update = (newGameSize != oldGameSize) || (oldGamePlayers != newGamePlayers);
            
        }
        if (getJoinGameController().getJoinGameView().isModalShowing()) {
            
            //only update when necessary
            
            if (update) {
                getJoinGameController().getJoinGameView().setGames(games, playerInfo);
                getJoinGameController().getJoinGameView().closeModal();
                getJoinGameController().getJoinGameView().showModal();
            }
            
        }

    }

}
