package client.managers;

import client.data.*;
import java.util.ArrayList;

public class LocationManager {

    private ArrayList<Location> settledLocations;
    private ArrayList<Location> unsettledLocations;
    private ArrayList<Edge> settledEdges;
    private ArrayList<Edge> unsettledEdges;
    private ArrayList<Port> ports;

    public LocationManager() {
        settledLocations = new ArrayList<Location>();
        unsettledLocations = new ArrayList<Location>();
        settledEdges = new ArrayList<Edge>();
        unsettledEdges = new ArrayList<Edge>();
        ports = new ArrayList<Port>();
    }

    /**
     *
     * @param locationToSettle = the location the current player wants to build
     * @pre the current player had enough resources to build and a settlement to
     * build with
     * @post returns true if the VertexLocation was found in a Location of unsettledLocations and
     * the boolean canBeSettled of that Location is true, returns false
     * otherwise
     */
    public boolean settleLocation(VertexLocation locationToSettle) {
        return false;
    }

    /**
     *
     * @param edgeLocationToSettle = the edge the current player wants to build a road on
     * @pre the current player had enough resources to build and a road
     * @post returns true if the EdgeLocation was found in an Edge in unsettledEdges and
     * the player's id is found in the whoCanBuild array. Otherwise returns false
     */
    public boolean settleEdge(EdgeLocation edgeLocationToSettle) {
        return false;
    }

    /**
     *
     * @param locationToUpgrade = the location the current player wants to
     * upgrade the settlement
     * @pre the current player had enough resources to upgrade to a city and a
     * city to build with
     * @post returns true if the VertexLocation is in a Location of settledLocations, if the
     * Location ownerID matches the player, and if the Location's city boolean
     * is false. Otherwise returns false.
     */
    public boolean upgradeToCity(VertexLocation locationToUpgrade) {
        return false;
    }
    
    /**
     * 
     * @pre the player is going to maritime trade
     * @post returns the list of Ports that the player has a settlement or city on
     */
    public ArrayList<Port> getPortsPlayerHas() {
        return null;
    }

    public ArrayList<Location> getSettledLocations() {
        return settledLocations;
    }

    public ArrayList<Location> getUnsettledLocations() {
        return unsettledLocations;
    }

    public void setSettledLocations(ArrayList<Location> settledLocationsNew) {
        settledLocations = settledLocationsNew;
    }

    public void setUnsettledLocations(ArrayList<Location> unsettledLocationsNew) {
        unsettledLocations = unsettledLocationsNew;
    }

    public ArrayList<Edge> getSettledEdges() {
        return settledEdges;
    }

    public ArrayList<Edge> getUnsettledEdges() {
        return unsettledEdges;
    }

    public void setSettledEdges(ArrayList<Edge> settledEdgesNew) {
        settledEdges = settledEdgesNew;
    }

    public ArrayList<Port> getPorts() {
        return ports;
    }

    public void setPorts(ArrayList<Port> portsNew) {
        ports = portsNew;
    }

    public void setUnsettledEdges(ArrayList<Edge> unsettledEdgesNew) {
        unsettledEdges = unsettledEdgesNew;
    }


}
