/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;

/**
 *
 * @author ddennis
 */
abstract class State {

    /**
     * INITIAL,
        LOGIN,
        WAITING,
        JOIN_GAME,
        PLAYER_WAITING,
        SETUP,
        FIRST_ROUND ,
        SECOND_ROUND ,
        GAME_PLAY ,
        GAME_OVER ;
     * @param context
     */
    public void doAction(Controller controller){}
}
