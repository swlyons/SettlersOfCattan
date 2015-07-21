/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.data;

import shared.locations.EdgeDirection;

/**
 *
 * @author Samuel
 */
public class XYEdgeLocation {

    public XYEdgeLocation() {
    }

    private int x;
    private int y;
    private EdgeDirection direction;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public EdgeDirection getDirection() {
        return direction;
    }

    public void setDirection(EdgeDirection direction) {
        this.direction = direction;
    }
}
