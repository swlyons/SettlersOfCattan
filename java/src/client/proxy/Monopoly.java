package client.proxy;

import shared.definitions.ResourceType;

/**
 * Contains information about playing a monopoly card
 *
 * @author Aaron
 *
 */
public class Monopoly extends Command {

    private ResourceType resource;

    public Monopoly(int playerIndex) {
        super("Monopoly", playerIndex);
    }
    
    public ResourceType getResource() {
        return resource;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

}
