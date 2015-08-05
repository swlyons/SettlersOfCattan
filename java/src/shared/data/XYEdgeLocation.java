/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.data;

import java.io.Serializable;
import shared.locations.EdgeDirection;

/**
 *
 * @author Samuel
 */
public class XYEdgeLocation implements Serializable {

    public XYEdgeLocation() {
    }

    private Integer x;
    private Integer y;
    private EdgeDirection direction;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public EdgeDirection getDirection() {
        return direction;
    }

    public void setDirection(EdgeDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "{" + "\"x\":" + x + ", \"y\":" + y + ",\"direction\":\"" + direction.toString().toUpperCase() + "\"}";
    }
}
