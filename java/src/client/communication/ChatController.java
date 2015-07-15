package client.communication;

import client.base.*;
import client.data.GameInfo;
import client.data.MessageLine;
import client.data.MessageList;
import client.data.PlayerInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        MessageList entries = game.getChat();
        CatanColor color = null;
        //List<LogEntry> logEntries = new ArrayList<>();
        for (MessageLine entry : entries.getLines()) {
            
            for(PlayerInfo player : game.getPlayers()){
                if(player.getName().equals(entry.getSource())){
                    color = CatanColor.valueOf(player.getColor().toUpperCase());
                }
            }
        }
        //add the new message
        getView().getEntries().add(new LogEntry(color, message));
        getView().setEntries(getView().getEntries());
    }

}
