package shared.data;

import java.io.Serializable;
import java.util.ArrayList;

import shared.definitions.HexType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

public class Hex implements Serializable {

    private HexLocation location;
    private ArrayList<EdgeLocation> edgeLocations;
    private ResourceType resource;
    private HexType type;
    private boolean hasRobber;
    private int number;

    public Hex(HexLocation hexStart, int rollValueStart, ResourceType resourceTypeStart) {
        location = hexStart;
        number = rollValueStart;
        resource = resourceTypeStart;
//		switch (resourceTypeStart) {
//		case brick:
//			System.out.println("Made a Brick");
//			type = HexType.brick;
//			break;
//		case ore:
//			System.out.println("Made a Ore");
//			type = HexType.ore;
//			break;
//		case sheep:
//			System.out.println("Made a Sheep");
//			type = HexType.sheep;
//			break;
//		case wheat:
//			System.out.println("Made a Wheat");
//			type = HexType.wheat;
//			break;
//		case wood:
//			System.out.println("Made a Wood");
//			type = HexType.wood;
//			break;
//		default:
//			// do nothing
//		}

        hasRobber = false;
    }

    public ArrayList<EdgeLocation> getEdgeLocations() {
        return edgeLocations;
    }

    public void setEdgeLocations(ArrayList<EdgeLocation> edgeLocations) {
        this.edgeLocations = edgeLocations;
    }

    public boolean getHasRobber() {
        return hasRobber;
    }

    public void setHasRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }

    public ResourceType getResource() {
        return resource;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

    public HexType getType() {
        return type;
    }

    public void setType(HexType type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        if (resource == null) {
            return "{" + "\"location\" : " + location.toString() + ", \"resource\" : null, \"number\" : " + number + "}";
        } else {
            return "{" + "\"location\" : " + location.toString() + ", \"resource\" : \"" + resource.toString().toLowerCase() + "\", \"number\" : " + number + "}";

        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((edgeLocations == null) ? 0 : edgeLocations.hashCode());
        result = prime * result + (hasRobber ? 1231 : 1237);
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + number;
        result = prime * result + ((resource == null) ? 0 : resource.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Hex other = (Hex) obj;

        if (!location.equals(other.location)) {
            return false;
        }
        return true;
    }
}
