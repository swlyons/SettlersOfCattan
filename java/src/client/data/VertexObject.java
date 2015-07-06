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
public class VertexObject {
    private int owner;
    private EdgeLocation direction;

    public VertexObject(int owner, EdgeLocation direction) {
        this.owner = owner;
        this.direction = direction;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public EdgeLocation getDirection() {
        return direction;
    }

    public void setDirection(EdgeLocation direction) {
        this.direction = direction;
    }
    
    
}
