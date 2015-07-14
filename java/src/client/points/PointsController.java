package client.points;

import java.util.Map;

import client.base.*;
import client.communication.ClientCommunicator;
import client.data.Game;
import client.data.Player;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController {

	private IGameFinishedView finishedView;
	
	/**
	 * PointsController constructor
	 * 
	 * @param view Points view
	 * @param finishedView Game finished view, which is displayed when the game is over
	 */
	public PointsController(IPointsView view, IGameFinishedView finishedView) {
		
		super(view);
		
		setFinishedView(finishedView);
		
		initFromModel();
	}
	
	public IPointsView getPointsView() {
		
		return (IPointsView)super.getView();
	}
	
	public IGameFinishedView getFinishedView() {
		return finishedView;
	}
	public void setFinishedView(IGameFinishedView finishedView) {
		this.finishedView = finishedView;
	}

	private void initFromModel() {
		Game game = ClientCommunicator.getSingleton().getGameManager().getGame();
		
		int points = 0;
		
		Map<Integer, String> cookies = ClientCommunicator.getSingleton().getCookies();
		for(Map.Entry<Integer, String> cook : cookies.entrySet()){
            int playerID = cook.getKey();
            points = game.getPlayers().get(playerID).getVictoryPoints();
        }
		
		//<temp>		
		getPointsView().setPoints(points);
		//</temp>
	}
	
}

