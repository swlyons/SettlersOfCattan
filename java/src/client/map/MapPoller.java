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

	public MapPoller(MapController mapController) {
		super();
		this.mapController = mapController;
		this.firstInitialization = false;
		playerIndex = -1;
		version = -1;
	}

	public void run() {
		if (ClientCommunicator.getSingleton().getJoinedGame()) {
			try {
				GameInfo gameInformation = ClientCommunicatorFascadeSettlersOfCatan.getSingleton()
						.getGameModel(version + "");
				version = gameInformation.getVersion();
				ClientCommunicator.getSingleton().getGameManager().initializeGame(gameInformation);
				if (playerIndex == -1) {
					GameManager gm = ClientCommunicator.getSingleton().getGameManager();
					Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
					for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
						if (gm.getGame().getPlayers().get(i).getId() == playerId) {
							playerIndex = i;
							break;
						}
					}
				}
				// If they haven't initialized before or it isn't the client's
				// turn
				if (!firstInitialization || gameInformation.getTurnTracker().getCurrentTurn() != playerIndex) {
					mapController.initFromModel();
					GameManager gm = ClientCommunicator.getSingleton().getGameManager();

					System.out.println(gameInformation.getTurnTracker().getCurrentTurn() + " ->? " + playerIndex);
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
							mapController.startMove(PieceType.SETTLEMENT, true, true);
							mapController.startMove(PieceType.ROAD, false, true);
						} else if (gm.getLocationManager().getSettledLocations().size() < 8) {
							// We are on the second round.
							mapController.startMove(PieceType.SETTLEMENT, true, true);
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
