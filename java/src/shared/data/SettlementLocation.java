/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.data;

import shared.locations.VertexDirection;

/**
 *
 * @author Samuel
 */
public class SettlementLocation {

    public void SettlementLocation() {

    }

    private int x;
    private int y;
    private VertexDirection direction;

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

    public VertexDirection getDirection() {
        return direction;
    }

    public void setDirection(VertexDirection direction) {
        this.direction = direction;
    }
    
    @Override
    public String toString() {
        return "{" + "\"x\":" + x + ", \"y\":" + y +",\"direction\":\""+direction.toString().toUpperCase()+"\"}";
    }


}
