package shared.model;

import shared.definitions.ResourceType;

/**
 * Contains the information for playing a year of plenty
 *
 * @author Aaron
 *
 */
public class Year_Of_Plenty extends Command {

    private ResourceType resource1;
    private ResourceType resource2;

    public Year_Of_Plenty(int playerIndex) {
        super("Year_Of_Plenty", playerIndex);
    }

    public ResourceType getResource1() {
        return resource1;
    }

    public void setResource1(ResourceType resource1) {
        this.resource1 = resource1;
    }

    public ResourceType getResource2() {
        return resource2;
    }

    public void setResource2(ResourceType resource2) {
        this.resource2 = resource2;
    }
}
