package client.data;

import shared.definitions.ResourceType;

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
    
    private int brick;
    private int ore;
    private int sheep;
    private int wheat;
    private int wood;

    public ResourceList() {
    	brick = 0;
    	ore = 0;
    	sheep = 0;
    	wheat = 0;
    	wood = 0;
    }
    
    public ResourceList(ResourceType type) {
    	brick = 0;
    	ore = 0;
    	sheep = 0;
    	wheat = 0;
    	wood = 0;
    	
    	switch(type){
    	case BRICK:
    		brick++;
    		break;
    	case ORE:
    		ore++;
    		break;
    	case SHEEP:
    		sheep++;
    		break;
    	case WHEAT:
    		wheat++;
    		break;
    	case WOOD:
    		wood++;
    		break;
   		default:
   			//Should never occur
    	}
    }
    
    public ResourceList(int brick, int ore, int sheep, int wheat, int wood) {
        this.brick = brick;
        this.ore = ore;
        this.sheep = sheep;
        this.wheat = wheat;
        this.wood = wood;
    }

    public int getBrick() {
        return brick;
    }

    public void setBrick(int brick) {
        this.brick = brick;
    }
    
    public void addBrick(int amount) {
    	this.brick += amount;
    }

    public void removeBrick(int amount) {
    	this.brick -= amount;
    }

    public int getOre() {
        return ore;
    }

    public void setOre(int ore) {
        this.ore = ore;
    }

    public void addOre(int amount) {
    	this.ore += amount;
    }

    public void removeOre(int amount) {
    	this.ore -= amount;
    }

    public int getSheep() {
        return sheep;
    }

    public void setSheep(int sheep) {
        this.sheep = sheep;
    }

    public void addSheep(int amount) {
    	this.sheep += amount;
    }

    public void removeSheep(int amount) {
    	this.sheep -= amount;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public void addWheat(int amount) {
    	this.wheat += amount;
    }

    public void removeWheat(int amount) {
    	this.wheat -= amount;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }
    
    public void addWood(int amount) {
    	this.wood += amount;
    }

    public void removeWood(int amount) {
    	this.wood -= amount;
    }

    public boolean hasCardsAvailable(ResourceList request) {	
    	return request.getBrick() <= this.getBrick() && request.getOre() <= this.getOre() && request.getSheep() <= this.getSheep() 
    			&& request.getWheat() <= this.getWheat() && request.getWheat() <= this.getWheat();
    }
    
    /**
     * 
     * @pre none
     * 
     * @post returns a sum of all the resources in the current resource list
     * 
     */
    public int getTotalResources(){
        return (brick + ore + sheep + wood + wheat);
    }
    @Override
    public String toString() {
        return "{" + "brick : " + brick + ", ore : " + ore + ", sheep : " + sheep + ", wheat : " + wheat + ", wood : " + wood + '}';
    }
    
    
}
