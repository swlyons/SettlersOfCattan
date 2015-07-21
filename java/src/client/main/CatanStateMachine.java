/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.catan.CatanPanel;

/**
 *
 * @author ddennis
 */
public class CatanStateMachine {

    private State[] states = {new LoginState(), new JoinGameState(), new PlayerWaitingState(),
        new SetupState(), new FirstRoundState(), new SecondRoundState(), new GamePlayState()};

    private int[][] transition = {{1, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 6}};
    private int current = 0;

    private void next(int message) {
        current = transition[current][message];
    }

    public void stay(Controller controller) {
        states[current].doAction(controller);
        next(0);
    }

    public void move(Controller controller) {
        states[current].doAction(controller);
        next(1);
    }

}
