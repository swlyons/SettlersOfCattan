package client.roll;

import java.util.Random;

import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.managers.GameManager;
import client.proxy.FinishMove;
import client.proxy.RollNumber;


/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController {

	private IRollResultView resultView;

	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {

		super(view);
		
		setResultView(resultView);
	}
	
	public IRollResultView getResultView() {
		return resultView;
	}
	
	public void setResultView(IRollResultView resultView) {
		this.resultView = resultView;
	}

	public IRollView getRollView() {
		return (IRollView)getView();
	}
	
	@Override
	public void rollDice() {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		RollNumber rn = new RollNumber(gm.getGame().getTurnTracker().getCurrentTurn());
		Random r = new Random();
		int twoD6 = r.nextInt(6) + 1;
		rn.setNumber(twoD6);

		try {
			gm.initializeGame(ClientCommunicatorFascadeSettlersOfCatan.getSingleton().rollNumber(rn));
			getResultView().setRollValue(twoD6);
			getRollView().closeModal();
			getResultView().showModal();
		} catch(Exception e) {
			System.out.println("Couldn't end turn");
		}	
	}

}

