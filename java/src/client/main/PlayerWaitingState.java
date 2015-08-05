/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.join.JoinGameController;
import client.join.PlayerWaitingViewPoller;
import java.util.Timer;

/**
 *
 * @author ddennis
 */
public class PlayerWaitingState extends State {

    private Timer playerWaitingTimer = new Timer();

    @Override
    public void doAction(Controller controller) {
        JoinGameController joinGameController = null;
        //need to check since it can be a map controller once we have 4 players
        if (controller.getClass().getName().contains("Join")) {
            joinGameController = (JoinGameController) controller;

            System.out.println(this.toString());

            if (joinGameController.getPlayerWaitingView().isModalShowing()) {
                playerWaitingTimer.schedule(new PlayerWaitingViewPoller(joinGameController), 0, 1000);

                //you are in the player waiting no need to poll anymore on listing games
                joinGameController.getJoinGameTimer().cancel();
                joinGameController.getJoinGameTimer().purge();
            }

        } else {

            // we have 4 players no need to poll anymore
            playerWaitingTimer.cancel();
            playerWaitingTimer.purge();

        }
    }

    @Override
    public String toString() {
        return "PlayerWaitingState{" + '}';
    }
}
