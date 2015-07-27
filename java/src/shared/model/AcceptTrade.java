package shared.model;

/**
 * contains information on the action of accepting/rejecting a trade
 *
 * @author Aaron
 *
 */
public class AcceptTrade extends Command {

    private Boolean willAccept;

    public Boolean isWillAccept() {
        return willAccept;
    }

    public void setWillAccept(Boolean willAccept) {
        this.willAccept = willAccept;
    }
}
