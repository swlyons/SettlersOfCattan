/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.main;

import client.base.Controller;
import client.map.MapController;
import client.map.MapView;
import java.util.Timer;

/**
 *
 * @author ddennis
 */
public class GamePlayState extends State {

    private final Timer gamePlayTimer = new Timer();

    @Override
    public void doAction(Controller controller) {
        MapController mapController = (MapController) controller;
        
        //make sure no overlay is left over
        if (((MapView) mapController.getView()).getOverlay().isModalShowing()) {
            ((MapView) mapController.getView()).getOverlay().closeModal();
        }
        System.out.println(this.toString());
        gamePlayTimer.schedule(mapController.getMapPoller(), 0, 3000);
    }

    @Override
    public String toString() {
        return "GamePlayState{" + '}';
    }
}
