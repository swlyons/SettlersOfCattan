/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.data;

import java.util.List;
import java.util.ArrayList;
import shared.locations.HexLocation;


/**
 *
 * @author ddennis
 */
public class Map {
    private List<Hex> hexes;
    private List<Port> ports;
    private List<VertexObject> roads;
    private List<VertexObject> settlements;
    private List <VertexObject> cities;
    private int radius;
    private HexLocation robber;

    public Map() {
    }

    public ArrayList<Hex> getHexes() {
        return (ArrayList)hexes;
    }

    public void setHexes(List<Hex> hexes) {
        this.hexes = hexes;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public ArrayList<VertexObject> getRoads() {
        return (ArrayList)roads;
    }

    public void setRoads(List<VertexObject> roads) {
        this.roads = roads;
    }

    public ArrayList<VertexObject> getSettlements() {
        return (ArrayList)settlements;
    }

    public void setSettlements(List<VertexObject> settlements) {
        this.settlements = settlements;
    }

    public ArrayList<VertexObject> getCities() {
        return (ArrayList)cities;
    }

    public void setCities(List<VertexObject> cities) {
        this.cities = cities;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public HexLocation getRobber() {
        return robber;
    }

    public void setRobber(HexLocation robber) {
        this.robber = robber;
    }
    
    
}
