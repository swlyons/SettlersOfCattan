/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.data;

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
    private List<EdgeValue> roads;
    private List<VertexObject> settlements;
    private List<VertexObject> cities;
    private int radius;
    private HexLocation robber;

    public Map() {
        hexes = new ArrayList<>();
        ports = new ArrayList<>();
        roads = new ArrayList<>();
        settlements = new ArrayList<>();
        cities = new ArrayList<>();
    }

    public ArrayList<Hex> getHexes() {
        return (ArrayList<Hex>) hexes;
    }

    public void setHexes(List<Hex> hexes) {
        this.hexes = hexes;
    }

    public ArrayList<Port> getPorts() {
        return (ArrayList<Port>) ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public ArrayList<EdgeValue> getRoads() {
        return (ArrayList<EdgeValue>) roads;
    }

    public void setRoads(List<EdgeValue> roads) {
        this.roads = roads;
    }

    public ArrayList<VertexObject> getSettlements() {
        return (ArrayList<VertexObject>) settlements;
    }

    public void setSettlements(List<VertexObject> settlements) {
        this.settlements = settlements;
    }

    public ArrayList<VertexObject> getCities() {
        return (ArrayList<VertexObject>) cities;
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

    @Override
    public String toString() {
        return "{" + "\"hexes\" : " + hexes.toString() + 
                ", \"ports\" : " + ports.toString() + 
                ", \"roads\" : " + roads.toString() + 
                ", \"settlements\" : " + settlements.toString() + 
                ", \"cities\" : " + cities.toString() + 
                ", \"radius\" : 3, \"robber\" : " + robber.toString() + 
                "}";
    }

}
