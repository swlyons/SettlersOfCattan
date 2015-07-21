/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.data;

import shared.locations.VertexLocation;
import shared.data.SettlementLocation;

/**
 *
 * @author ddennis
 */
public class VertexObject {

    private int owner;
    private VertexLocation direction;
    private SettlementLocation location;

    public VertexObject(int owner, VertexLocation direction) {
        this.owner = owner;
        this.direction = direction;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public VertexLocation getDirection() {
        return direction;
    }

    public void setDirection(VertexLocation direction) {
        this.direction = direction;
    }

    public SettlementLocation getLocation() {
        return location;
    }

    public void setLocation(SettlementLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "{" + "owner : " + owner + ", location : " + location + '}';
    }

}
