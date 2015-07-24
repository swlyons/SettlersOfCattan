package client.points;

import java.util.Map;

import client.base.*;
import client.communication.ClientCommunicator;
import shared.data.GameInfo;
import client.managers.GameManager;

/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController {

    private IGameFinishedView finishedView;

    /**
     * PointsController constructor
     *
     * @param view Points view
     * @param finishedView Game finished view, which is displayed when the game
     * is over
     */
    public PointsController(IPointsView view, IGameFinishedView finishedView) {

        super(view);

        setFinishedView(finishedView);

//		initFromModel();
    }

    public IPointsView getPointsView() {

        return (IPointsView) super.getView();
    }

    public IGameFinishedView getFinishedView() {
        return finishedView;
    }

    public void setFinishedView(IGameFinishedView finishedView) {
        this.finishedView = finishedView;
    }

    private void initFromModel() {
        GameInfo game = ClientCommunicator.getSingleton().getGameManager().getGame();
        int points = 0;

        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }

        points = game.getPlayers().get(playerIndex).getVictoryPoints();
        getPointsView().setPoints(points);
    }

}
