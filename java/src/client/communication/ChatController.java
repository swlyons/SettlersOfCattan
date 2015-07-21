package client.communication;

import client.main.ClientException;
import client.base.*;
import shared.data.GameInfo;
import shared.data.MessageLine;
import shared.data.MessageList;
import shared.data.PlayerInfo;
import shared.model.SendChat;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.definitions.CatanColor;

/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController {

    public ChatController(IChatView view) {

        super(view);
    }

    @Override
    public IChatView getView() {
        return (IChatView) super.getView();
    }

    @Override
    public void sendMessage(String message) {
        //clear all the existing entries (may not be necessary)
        GameInfo game = (GameInfo) ClientCommunicator.getSingleton().getGameManager().getGame();

        CatanColor color = null;
        int index = -1;
        int playerId = ClientCommunicator.getSingleton().getPlayerId();

        for (PlayerInfo player : game.getPlayers()) {
            if (player.getPlayerID() == playerId) {
                color = CatanColor.valueOf(player.getColor().toUpperCase());
                index = player.getPlayerIndex();
                break;
            }
        }

        //add the new message to the GUI (replace default if exists)
        if (getView().getEntries().size() == 1 && getView().getEntries().get(0).getMessage().equals("No messages")) {
            getView().getEntries().clear();
        }
        getView().getEntries().add(new LogEntry(color, message));
        getView().setEntries(getView().getEntries());

        //add the new messages to the server
        SendChat request = new SendChat(index);
        request.setContent(message);

        try {
            ClientCommunicatorFascadeSettlersOfCatan.getSingleton().sendChat(request);
        } catch (ClientException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

}
