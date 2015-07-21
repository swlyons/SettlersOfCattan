package client.resources;

import java.util.*;

import client.base.*;
import client.communication.ClientCommunicator;
import client.managers.GameManager;

/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController {

    private Map<ResourceBarElement, IAction> elementActions;

    public ResourceBarController(IResourceBarView view) {

        super(view);

        elementActions = new HashMap<ResourceBarElement, IAction>();
    }

    @Override
    public IResourceBarView getView() {
        return (IResourceBarView) super.getView();
    }

    /**
     * Sets the action to be executed when the specified resource bar element is
     * clicked by the user
     *
     * @param element The resource bar element with which the action is
     * associated
     * @param action The action to be executed
     */
    public void setElementAction(ResourceBarElement element, IAction action) {

        elementActions.put(element, action);
    }

    public boolean canBuildRoad() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        boolean canBuild = gm.canBuildRoad() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
        getView().setElementEnabled(ResourceBarElement.ROAD, canBuild);
        return canBuild;
    }

    public boolean canBuildSettlement() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        boolean canBuild = gm.canBuildSettlement() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
        getView().setElementEnabled(ResourceBarElement.SETTLEMENT, canBuild);
        return canBuild;
    }

    public boolean canBuildCity() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        boolean canBuild = gm.canBuildCity() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
        getView().setElementEnabled(ResourceBarElement.CITY, canBuild);
        return canBuild;
    }

    public boolean canBuyCard() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        boolean canBuild = gm.canBuyCard() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
        getView().setElementEnabled(ResourceBarElement.BUY_CARD, canBuild);
        return canBuild;
    }

    public boolean canPlayCard() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        boolean canBuild = gm.canPlayCard() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
        return canBuild;
    }

    @Override
    public void buildRoad() {
        executeElementAction(ResourceBarElement.ROAD);
    }

    @Override
    public void buildSettlement() {
        executeElementAction(ResourceBarElement.SETTLEMENT);
    }

    @Override
    public void buildCity() {
        executeElementAction(ResourceBarElement.CITY);
    }

    @Override
    public void buyCard() {
        executeElementAction(ResourceBarElement.BUY_CARD);
    }

    @Override
    public void playCard() {
        executeElementAction(ResourceBarElement.PLAY_CARD);
    }

    private void executeElementAction(ResourceBarElement element) {

        if (elementActions.containsKey(element)) {

            IAction action = elementActions.get(element);
            action.execute();
        }
    }

}
