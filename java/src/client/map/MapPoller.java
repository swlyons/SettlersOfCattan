/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map;

import java.util.TimerTask;
import client.data.GameInfo;
import client.managers.GameManager;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.map.MapController;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;

/**
 *
 * @author Samuel
 */
public class MapPoller extends TimerTask {

	private MapController mapController;
	private int version;
	private boolean firstInitialization;
	private int playerIndex;
        private String playerColor;

	public MapPoller(MapController mapController) {
		super();
		this.mapController = mapController;
		this.firstInitialization = false;
		playerIndex = -1;
                playerColor = "";
		version = -1;
	}

	public void run() {
		if (ClientCommunicator.getSingleton().getJoinedGame()) {
			try {
                                
				GameInfo gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton()
						.getGameModel(version+ "");
                                version = gameInformation.getVersion();

                                GameManager gameManager = ClientCommunicator.getSingleton().getGameManager();
                                gameManager.initializeGame(gameInformation);
                                
                                 
				if (playerIndex == -1) {
                                        
					
					Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
                                        for (int i = 0; i < gameManager.getGame().getPlayers().size(); i++) {
                                               if (gameManager.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                                                        playerIndex = gameManager.getGame().getPlayers().get(i).getPlayerIndex();
                                                        playerColor = gameManager.getGame().getPlayers().get(i).getColor().toUpperCase();
							break;
						}
					}
				}
				// If they haven't initialized before or it isn't the client's
				// turn
				if (!firstInitialization || gameInformation.getTurnTracker().getCurrentTurn() != playerIndex) {
                                        
					mapController.initFromModel();
					GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                                        if (gameInformation.getTurnTracker().getCurrentTurn() != playerIndex)
						// This boolean toggles on after your turn, so when the
						// turn track comes around again, you get exactly one
						// update on each of your turns. This way, your client
						// will switch to a "It is your turn" state.
						firstInitialization = false;
					else {
						firstInitialization = true;
						if (gm.getLocationManager().getSettledLocations().size() < 4) {
							// We are on the first round.
                                                        mapController.getView().startDrop(PieceType.SETTLEMENT, CatanColor.valueOf(playerColor), false);
							mapController.startMove(PieceType.SETTLEMENT, true, true);
                                                        mapController.getView().startDrop(PieceType.ROAD, CatanColor.valueOf(playerColor), false);
							mapController.startMove(PieceType.ROAD, false, true);
						} else if (gm.getLocationManager().getSettledLocations().size() < 8) {
							// We are on the second round.
							mapController.getView().startDrop(PieceType.SETTLEMENT, CatanColor.valueOf(playerColor), false);
							mapController.startMove(PieceType.SETTLEMENT, true, true);
                                                        mapController.getView().startDrop(PieceType.ROAD, CatanColor.valueOf(playerColor), false);
							mapController.startMove(PieceType.ROAD, false, true);
						}
					}

				}
			} catch (Exception e) {
			}
		}
		// System.out.println("meow");
	}

}
