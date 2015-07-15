package client.turntracker;

import shared.definitions.CatanColor;
import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.proxy.FinishMove;
import client.managers.GameManager;

/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController {

	public TurnTrackerController(ITurnTrackerView view) {
		
		super(view);
		
		initFromModel();
	}
	
	@Override
	public ITurnTrackerView getView() {
		
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		FinishMove fm = new FinishMove(gm.getGame().getTurnTracker().getCurrentTurn());
		try {
			gm.initializeGame(ClientCommunicatorFascadeSettlersOfCatan.getSingleton().finishMove(fm), "");
			getView().updateGameState("Waiting for other players", false);
		} catch(Exception e) {
			System.out.println("Couldn't end turn");
		}		
	}
	
	private void initFromModel() {
		//<temp>
		getView().setLocalPlayerColor(CatanColor.RED);
		//</temp>
	}

}

