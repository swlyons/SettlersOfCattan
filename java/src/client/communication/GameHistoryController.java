package client.communication;

import java.util.*;
import java.util.List;

import client.base.*;
import shared.data.GameInfo;
import shared.data.MessageLine;
import shared.data.PlayerInfo;
import shared.definitions.CatanColor;

/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController {

    public GameHistoryController(IGameHistoryView view) {
        super(view);
        initFromModel();
    }

    @Override
    public IGameHistoryView getView() {

        return (IGameHistoryView) super.getView();
    }

    private void initFromModel() {
        //clear all the existing entries (may not be necessary)
        GameInfo game = (GameInfo) ClientCommunicator.getSingleton().getGameManager().getGame();

        CatanColor color = null;
        int playerId = ClientCommunicator.getSingleton().getPlayerId();
        if (game != null) {
            for (PlayerInfo player : game.getPlayers()) {
                if (player.getPlayerID() == playerId) {
                    color = CatanColor.valueOf(player.getColor().toUpperCase());
                    break;
                }
            }

        //add the new message to the GUI (replace default if exists)
            //if (getView().getEntries().size() != game.getLog().getLines().size()) {
            getView().getEntries().clear();
            for (MessageLine messageLine : game.getLog().getLines()) {
                getView().getEntries().add(new LogEntry(color, messageLine.getMessage()));
            }
        
        //}
         getView().setEntries(getView().getEntries());
        }
    }

}
