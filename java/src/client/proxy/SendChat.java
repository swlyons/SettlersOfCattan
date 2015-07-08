package client.proxy;

/**
 * Contains the information for posting a new chat message.
 *
 * @author Aaron
 *
 */
public class SendChat extends Command {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
