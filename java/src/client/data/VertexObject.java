/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.data;

import shared.locations.VertexLocation;

/**
 *
 * @author ddennis
 */
public class VertexObject {
    private int owner;
    private VertexLocation location;

    public VertexObject(int owner, VertexLocation location) {
        this.owner = owner;
        this.location = location;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public VertexLocation getDirection() {
        return location;
    }

    public void setDirection(VertexLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "{" + "owner : " + owner + ", location : " + location + '}';
    }
    
    
}
