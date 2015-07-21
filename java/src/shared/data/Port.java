package shared.data;

import shared.definitions.ResourceType;
import shared.locations.HexLocation;

public class Port {

    private int ratio;
    private ResourceType resource;
    private HexLocation location;

    public Port(int ratio, ResourceType resource, HexLocation location) {
        this.ratio = ratio;
        this.resource = resource;
        this.location = location;
    }

    public ResourceType getResource() {
        return resource;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "{" + "resource : " + resource + ", location : " + location + ", ratio : " + ratio + '}';
    }

}
