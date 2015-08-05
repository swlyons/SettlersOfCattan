package shared.model;

import shared.definitions.ResourceType;

/**
 * contains information on making a maritime trade
 *
 * @author Aaron
 *
 */
public class MaritimeTrade extends Command {

    private Integer ratio;
    private ResourceType inputResource;
    private ResourceType outputResource;
    private Integer gameId;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

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

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }
}
