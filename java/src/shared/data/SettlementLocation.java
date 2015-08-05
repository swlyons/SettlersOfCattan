/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.data;

import java.io.Serializable;
import shared.locations.VertexDirection;

/**
 *
 * @author Samuel
 */
public class SettlementLocation implements Serializable {

    public void SettlementLocation() {

    }

    private Integer x;
    private Integer y;
    private VertexDirection direction;

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

    public VertexDirection getDirection() {
        return direction;
    }

    public void setDirection(VertexDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "{" + "\"x\":" + x + ", \"y\":" + y + ",\"direction\":\"" + direction.toString().toUpperCase() + "\"}";
    }

}
