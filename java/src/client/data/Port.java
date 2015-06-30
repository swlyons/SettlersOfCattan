package client.data;

public class Port {

    private String resourceType;
    private HexLocation location;
    private int tradeRatio;
    
    public Port(){}
    
        public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }

    public int getTradeRatio() {
        return tradeRatio;
    }

    public void setTradeRatio(int tradeRatio) {
        this.tradeRatio = tradeRatio;
    }

}
