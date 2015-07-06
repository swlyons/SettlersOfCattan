/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.data;

import shared.locations.EdgeLocation;

/**
 *
 * @author ddennis
 */
public class EdgeValue {
    
    private int owner;
    private EdgeLocation location;

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public EdgeLocation getLocation() {
        return location;
    }

    public void setLocation(EdgeLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "{" + "owner : " + owner + ", location :" + location + '}';
    }
    
    
}
