/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.data;

import shared.locations.EdgeLocation;
import client.data.XYEdgeLocation;

/**
 *
 * @author ddennis
 */
public class EdgeValue {

    private int owner;
    private EdgeLocation location2;
    private XYEdgeLocation location;

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public XYEdgeLocation getLocation() {
        return location;
    }

    public void setLocation(XYEdgeLocation location) {
        this.location = location;
    }

    public EdgeLocation getLocation2() {
        return location2;
    }

    public void setLocation2(EdgeLocation location2) {
        this.location2 = location2;
    }

    @Override
    public String toString() {
        return "{" + "owner : " + owner + ", location :" + location + '}';
    }

}
