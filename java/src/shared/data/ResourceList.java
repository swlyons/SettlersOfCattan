package shared.data;

import shared.definitions.ResourceType;
import java.util.Date;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ddennis
 */
public class ResourceList {

    private Integer brick;
    private Integer ore;
    private Integer sheep;
    private Integer wheat;
    private Integer wood;

    public ResourceList() {
        brick = 0;
        ore = 0;
        sheep = 0;
        wheat = 0;
        wood = 0;
    }

    public ResourceList(boolean isMainBankInitializer) {
        if (isMainBankInitializer) {
            brick = 19;
            ore = 19;
            sheep = 19;
            wheat = 19;
            wood = 19;
        } else {
            brick = 0;
            ore = 0;
            sheep = 0;
            wheat = 0;
            wood = 0;
        }
    }

    public ResourceList(ResourceType type) {
        brick = 0;
        ore = 0;
        sheep = 0;
        wheat = 0;
        wood = 0;

        switch (type) {
            case brick:
                brick++;
                break;
            case ore:
                ore++;
                break;
            case sheep:
                sheep++;
                break;
            case wheat:
                wheat++;
                break;
            case wood:
                wood++;
                break;
            default:
            // Should never occur
        }
    }

    public ResourceList(Integer brick, Integer ore, Integer sheep, Integer wheat, Integer wood) {
        this.brick = brick;
        this.ore = ore;
        this.sheep = sheep;
        this.wheat = wheat;
        this.wood = wood;
    }
    
    public ResourceList getRandomResourceAvailable(){
        Integer total = getTotalResources();
        if(total<=0){
            return null;
        }
        Random r = new Random();
        r.setSeed((new Date()).getTime());
        
        Integer resourceNumber = r.nextInt(total);
        
        resourceNumber-=brick;
        if(resourceNumber<0){
            return new ResourceList(1,0,0,0,0);
        }
        
        resourceNumber-=ore;
        if(resourceNumber<0){
            return new ResourceList(0,1,0,0,0);
        }
        
        resourceNumber-=sheep;
        if(resourceNumber<0){
            return new ResourceList(0,0,1,0,0);
        }
        
        resourceNumber-=wheat;
        if(resourceNumber<0){
            return new ResourceList(0,0,0,1,0);
        }
            return new ResourceList(0,0,0,0,1);
        
    }

    public Integer getBrick() {
        return brick;
    }

    public void setBrick(Integer brick) {
        this.brick = brick;
    }

    public void addBrick(Integer amount) {
        this.brick += amount;
    }

    public void removeBrick(Integer amount) {
        this.brick -= amount;
    }

    public Integer getOre() {
        return ore;
    }

    public void setOre(Integer ore) {
        this.ore = ore;
    }

    public void addOre(Integer amount) {
        this.ore += amount;
    }

    public void removeOre(Integer amount) {
        this.ore -= amount;
    }

    public Integer getSheep() {
        return sheep;
    }

    public void setSheep(Integer sheep) {
        this.sheep = sheep;
    }

    public void addSheep(Integer amount) {
        this.sheep += amount;
    }

    public void removeSheep(Integer amount) {
        this.sheep -= amount;
    }

    public Integer getWheat() {
        return wheat;
    }

    public void setWheat(Integer wheat) {
        this.wheat = wheat;
    }

    public void addWheat(Integer amount) {
        this.wheat += amount;
    }

    public void removeWheat(Integer amount) {
        this.wheat -= amount;
    }

    public Integer getWood() {
        return wood;
    }

    public void setWood(Integer wood) {
        this.wood = wood;
    }

    public void addWood(Integer amount) {
        this.wood += amount;
    }

    public void removeWood(Integer amount) {
        this.wood -= amount;
    }

    public void add(ResourceType type, Integer amount) {
        switch (type) {
            case brick:
                this.brick += amount;
                break;
            case ore:
                this.ore += amount;
                break;
            case sheep:
                this.sheep += amount;
                break;
            case wheat:
                this.wheat += amount;
                break;
            case wood:
                this.wood += amount;
                break;
            default:
                return;
        }
    }

    public boolean hasCardsAvailable(ResourceList request) {
        return request.getBrick() <= this.getBrick() && request.getOre() <= this.getOre()
                && request.getSheep() <= this.getSheep() && request.getWheat() <= this.getWheat()
                && request.getWheat() <= this.getWheat();
    }

    /**
     *
     * @pre none
     *
     * @post returns a sum of all the resources in the current resource list
     *
     */
    public Integer getTotalResources() {
        return (brick + ore + sheep + wood + wheat);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + brick;
        result = prime * result + ore;
        result = prime * result + sheep;
        result = prime * result + wheat;
        result = prime * result + wood;
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
        ResourceList other = (ResourceList) obj;
        if (brick != other.getBrick()) {
            return false;
        }
        if (ore != other.getOre()) {
            return false;
        }
        if (sheep != other.getSheep()) {
            return false;
        }
        if (wheat != other.getWheat()) {
            return false;
        }
        if (wood != other.getWood()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{" + "\"brick\" : " + brick + 
                ", \"ore\" : " + ore + 
                ", \"sheep\" : " + sheep + 
                ", \"wheat\" : " + wheat + 
                ", \"wood\" : " + wood + "}";
    }

}
