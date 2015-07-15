package client.communication;

import client.ClientException;
import client.base.*;
import client.data.GameInfo;
import client.data.MessageLine;
import client.data.MessageList;
import client.data.PlayerInfo;
import client.proxy.SendChat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        getView().getEntries();

        GameInfo game = (GameInfo) ClientCommunicator.getSingleton().getGameManager().getGame();
        if (game != null) {
            MessageList entries = game.getChat();
            CatanColor color = null;
            int index = -1;
            //List<LogEntry> logEntries = new ArrayList<>();
            for (MessageLine entry : entries.getLines()) {

                for (PlayerInfo player : game.getPlayers()) {
                    if (player.getName().equals(entry.getSource())) {
                        color = CatanColor.valueOf(player.getColor().toUpperCase());
                        index = player.getPlayerIndex();
                    }
                }
            }
            //add the new message to the GUI
            getView().getEntries().add(new LogEntry(color, message));
            getView().setEntries(getView().getEntries());

            //add the new messages to the server
            SendChat request = new SendChat(index);
            request.setContent(message);

            try {
                ClientCommunicatorFascadeSettlersOfCatan.getSingleton().sendChat(request);
            } catch (ClientException ex) {
                Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
