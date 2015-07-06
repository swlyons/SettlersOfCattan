package client.data;

import shared.locations.HexLocation;

public class Port {

    private String resource;
    private HexLocation location;
    private int ratio;
    
    public Port(){}
    
        public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
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
