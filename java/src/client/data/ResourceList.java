package client.data;
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

    public int getOre() {
        return ore;
    }

    public void setOre(int ore) {
        this.ore = ore;
    }

    public int getSheep() {
        return sheep;
    }

    public void setSheep(int sheep) {
        this.sheep = sheep;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
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
