package shared.model;

/**
 * Contains information for a buy dev card request
 *
 * @author Aaron
 *
 */
public class BuyDevCard extends Command {

    public BuyDevCard(int playerIndex) {
        super("buyDevCard", playerIndex);
    }

}
