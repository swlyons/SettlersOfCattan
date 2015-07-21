/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.join.JoinGameController;
import client.join.JoinGameViewPoller;
import java.util.Timer;

/**
 *
 * @author ddennis
 */
public class JoinGameState extends State {

    private Timer joinGameTimer = new Timer();

    @Override
    public void doAction(Controller controller) {
        JoinGameController joinGameController = (JoinGameController) controller;
        System.out.println(this.toString());

        if (joinGameController.getJoinGameView().isModalShowing()) {
            joinGameTimer.schedule(new JoinGameViewPoller(joinGameController), 0, 1000);
        }
    }

    @Override
    public String toString() {
        return "JoinGameState{" + '}';
    }
}
