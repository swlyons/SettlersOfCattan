package client.proxy;

/**
 * contains information on the action of accepting/rejecting a trade
 *
 * @author Aaron
 *
 */
public class AcceptTrade extends Command {

    private boolean willAccept;

    public boolean isWillAccept() {
        return willAccept;
    }

    public void setWillAccept(boolean willAccept) {
        this.willAccept = willAccept;
    }
}
