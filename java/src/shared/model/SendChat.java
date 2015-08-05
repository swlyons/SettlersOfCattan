package shared.model;

import java.io.Serializable;

/**
 * Contains the information for posting a new chat message.
 *
 * @author Aaron
 *
 */
public class SendChat extends Command implements Serializable {

    private String content;
    private int gameId;

    public SendChat(int playerIndex) {
        super("sendChat", playerIndex);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

}
