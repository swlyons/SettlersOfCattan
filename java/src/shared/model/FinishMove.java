package shared.model;

/**
 * Contains information for a finish turn request
 *
 * @author Aaron
 *
 */
public class FinishMove extends Command {

    public FinishMove(int playerIndex) {
        super("finishTurn", playerIndex);
    }

}
