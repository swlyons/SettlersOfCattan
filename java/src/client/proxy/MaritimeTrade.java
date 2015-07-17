package client.proxy;

import shared.definitions.ResourceType;

/**
 * contains information on making a maritime trade
 *
 * @author Aaron
 *
 */
public class MaritimeTrade extends Command {

    private int ratio;
    private ResourceType inputResource;
    private ResourceType outputResource;
    
    public ResourceType getInputResource() {
        return inputResource;
    }

    public void setInputResource(ResourceType inputResource) {
        this.inputResource = inputResource;
    }

    public ResourceType getOutputResource() {
        return outputResource;
    }

    public void setOutputResource(ResourceType outputResource) {
        this.outputResource = outputResource;
    }
    
    public int getRatio(){
        return ratio;
    }
    
    public void setRatio(int ratio){
        this.ratio=ratio;
    }
}
