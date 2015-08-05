package shared.data;

import java.io.Serializable;
import shared.definitions.ResourceType;
import shared.locations.HexLocation;

public class Port implements Serializable {

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
        if (ratio == 2) {
            return "{" + "\"resource\" : \"" + resource.toString().toLowerCase() + "\", \"location\" : " + location.toString() + ", \"ratio\" : " + ratio + ",\"direction\":\"N\"}";
        } else {
            return "{" + "\"location\" : " + location.toString() + ", \"ratio\" : " + ratio + ",\"direction\":\"N\"}";
        }

    }

}
