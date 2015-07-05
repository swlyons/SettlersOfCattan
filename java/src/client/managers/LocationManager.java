package client.managers;

import client.data.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
     * @param playerId
     * @param firstRound
     * @pre the current player had enough resources to build and a settlement to
     * build with
     * @post returns true if the VertexLocation was found in a Location of
     * unsettledLocations and the boolean canBeSettled of that Location is true,
     * returns false otherwise
     */
    public boolean settleLocation(VertexLocation locationToSettle, int playerId, boolean firstRound) {
        Location locationToMove = null;
        for (Location location : unsettledLocations) {
            if (location.getNormalizedLocation().equivalent(locationToSettle)) {
                if (firstRound) {
                    location.getWhoCanBuild().add(playerId);
                }

                if (location.getCanBeSettled() && location.getWhoCanBuild().contains(playerId)) {
                    location.setCanBeSettled(false);
                    locationToMove = location;
                    location.setOwnerID(playerId);
                    location.getWhoCanBuild().clear();
                    location.getWhoCanBuild().add(playerId);

                    if (location.getNormalizedLocation().getDir() == VertexDirection.NorthEast) {
                        VertexLocation unusableLocation1 = new VertexLocation(location.getNormalizedLocation().getHexLoc(), VertexDirection.NorthWest);
                        VertexLocation unusableLocation2 = new VertexLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() + 1, location.getNormalizedLocation().getHexLoc().getY()), VertexDirection.NorthWest);
                        VertexLocation unusableLocation3 = new VertexLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() + 1, location.getNormalizedLocation().getHexLoc().getY() - 1), VertexDirection.NorthWest);
                        for (Location locationUnavailable : unsettledLocations) {
                            if (locationUnavailable.getNormalizedLocation().equivalent(unusableLocation1)
                                    || locationUnavailable.getNormalizedLocation().equivalent(unusableLocation2)
                                    || locationUnavailable.getNormalizedLocation().equivalent(unusableLocation3)) {
                                locationUnavailable.setCanBeSettled(false);
                            }
                        }

                        if (firstRound) {
                            EdgeLocation usableEdge1 = new EdgeLocation(location.getNormalizedLocation().getHexLoc(), EdgeDirection.North);
                            EdgeLocation usableEdge2 = new EdgeLocation(location.getNormalizedLocation().getHexLoc(), EdgeDirection.NorthEast);
                            EdgeLocation usableEdge3 = new EdgeLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() + 1, location.getNormalizedLocation().getHexLoc().getY() - 1), EdgeDirection.NorthWest);
                            for (Edge edge : unsettledEdges) {
                                if (edge.getEdgeLocation().equivalent(usableEdge1)
                                        || edge.getEdgeLocation().equivalent(usableEdge2)
                                        || edge.getEdgeLocation().equivalent(usableEdge3)) {
                                    edge.getWhoCanBuild().add(playerId);
                                }
                            }
                        }

                    }

                    if (location.getNormalizedLocation().getDir() == VertexDirection.NorthWest) {
                        VertexLocation unusableLocation1 = new VertexLocation(location.getNormalizedLocation().getHexLoc(), VertexDirection.NorthEast);
                        VertexLocation unusableLocation2 = new VertexLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() - 1, location.getNormalizedLocation().getHexLoc().getY()), VertexDirection.NorthEast);
                        VertexLocation unusableLocation3 = new VertexLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() - 1, location.getNormalizedLocation().getHexLoc().getY() + 1), VertexDirection.NorthEast);
                        for (Location locationUnavailable : unsettledLocations) {
                            if (locationUnavailable.getNormalizedLocation().equivalent(unusableLocation1)
                                    || locationUnavailable.getNormalizedLocation().equivalent(unusableLocation2)
                                    || locationUnavailable.getNormalizedLocation().equivalent(unusableLocation3)) {
                                locationUnavailable.setCanBeSettled(false);
                            }
                        }

                        if (firstRound) {
                            EdgeLocation usableEdge1 = new EdgeLocation(location.getNormalizedLocation().getHexLoc(), EdgeDirection.North);
                            EdgeLocation usableEdge2 = new EdgeLocation(location.getNormalizedLocation().getHexLoc(), EdgeDirection.NorthWest);
                            EdgeLocation usableEdge3 = new EdgeLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() - 1, location.getNormalizedLocation().getHexLoc().getY()), EdgeDirection.NorthEast);
                            for (Edge edge : unsettledEdges) {
                                if (edge.getEdgeLocation().equivalent(usableEdge1)
                                        || edge.getEdgeLocation().equivalent(usableEdge2)
                                        || edge.getEdgeLocation().equivalent(usableEdge3)) {
                                    edge.getWhoCanBuild().add(playerId);
                                }
                            }
                        }
                    }

                    break;
                    
                } else {
                    return false;
                }
            }
        }

        if (locationToMove != null) {
            unsettledLocations.remove(locationToMove);
            settledLocations.add(locationToMove);
            return true;
        }
        return false;
    }

    /**
     *
     * @param edgeLocationToSettle = the edge the current player wants to build
     * a road on
     * @pre the current player had enough resources to build and a road
     * @post returns true if the EdgeLocation was found in an Edge in
     * unsettledEdges and the player's id is found in the whoCanBuild array.
     * Otherwise returns false
     */
    public boolean settleEdge(EdgeLocation edgeLocationToSettle, int playerId) {
        Edge edgeToMove = null;
        for (Edge edge : unsettledEdges) {
            if (edgeLocationToSettle.equivalent(edge.getEdgeLocation())) {
                if (edge.getWhoCanBuild().contains(playerId)) {
                    edge.setOwnerId(playerId);
                    edgeToMove = edge;
                    edge.getWhoCanBuild().clear();

                    if (edge.getEdgeLocation().getDir() == EdgeDirection.North) {
                        VertexLocation settleable1 = new VertexLocation(edge.getEdgeLocation().getHexLoc(), VertexDirection.NorthEast);
                        VertexLocation settleable2 = new VertexLocation(edge.getEdgeLocation().getHexLoc(), VertexDirection.NorthWest);
                        for (Location location : unsettledLocations) {
                            if (location.getNormalizedLocation().equivalent(settleable1)
                                    || location.getNormalizedLocation().equivalent(settleable2)) {
                                location.getWhoCanBuild().add(playerId);
                            }
                        }

                        EdgeLocation availableEdge1 = new EdgeLocation(edge.getEdgeLocation().getHexLoc(), EdgeDirection.NorthEast);
                        EdgeLocation availableEdge2 = new EdgeLocation(edge.getEdgeLocation().getHexLoc(), EdgeDirection.NorthWest);
                        EdgeLocation availableEdge3 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY() - 1), EdgeDirection.NorthWest);
                        EdgeLocation availableEdge4 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY()), EdgeDirection.NorthEast);

                        for (Edge addToEdge : unsettledEdges) {
                            if (addToEdge.getEdgeLocation().equivalent(availableEdge1)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge2)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge3)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge4)) {
                                addToEdge.getWhoCanBuild().add(playerId);
                            }
                        }
                    }

                    if (edge.getEdgeLocation().getDir() == EdgeDirection.NorthEast) {
                        VertexLocation settleable1 = new VertexLocation(edge.getEdgeLocation().getHexLoc(), VertexDirection.NorthEast);
                        VertexLocation settleable2 = new VertexLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY()), VertexDirection.NorthWest);
                        for (Location location : unsettledLocations) {
                            if (location.getNormalizedLocation().equivalent(settleable1)
                                    || location.getNormalizedLocation().equivalent(settleable2)) {
                                location.getWhoCanBuild().add(playerId);
                            }
                        }

                        EdgeLocation availableEdge1 = new EdgeLocation(edge.getEdgeLocation().getHexLoc(), EdgeDirection.North);
                        EdgeLocation availableEdge2 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY() - 1), EdgeDirection.NorthWest);
                        EdgeLocation availableEdge3 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY()), EdgeDirection.NorthWest);
                        EdgeLocation availableEdge4 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY()), EdgeDirection.North);

                        for (Edge addToEdge : unsettledEdges) {
                            if (addToEdge.getEdgeLocation().equivalent(availableEdge1)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge2)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge3)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge4)) {
                                addToEdge.getWhoCanBuild().add(playerId);
                            }
                        }
                    }

                    if (edge.getEdgeLocation().getDir() == EdgeDirection.NorthWest) {
                        VertexLocation settleable1 = new VertexLocation(edge.getEdgeLocation().getHexLoc(), VertexDirection.NorthWest);
                        VertexLocation settleable2 = new VertexLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY() + 1), VertexDirection.NorthEast);
                        for (Location location : unsettledLocations) {
                            if (location.getNormalizedLocation().equivalent(settleable1)
                                    || location.getNormalizedLocation().equivalent(settleable2)) {
                                location.getWhoCanBuild().add(playerId);
                            }
                        }

                        EdgeLocation availableEdge1 = new EdgeLocation(edge.getEdgeLocation().getHexLoc(), EdgeDirection.North);
                        EdgeLocation availableEdge2 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY()), EdgeDirection.NorthEast);
                        EdgeLocation availableEdge3 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY() + 1), EdgeDirection.NorthEast);
                        EdgeLocation availableEdge4 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY() + 1), EdgeDirection.North);

                        for (Edge addToEdge : unsettledEdges) {
                            if (addToEdge.getEdgeLocation().equivalent(availableEdge1)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge2)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge3)
                                    || addToEdge.getEdgeLocation().equivalent(availableEdge4)) {
                                addToEdge.getWhoCanBuild().add(playerId);
                            }
                        }
                    }

                    break;
                } else {
                    return false;
                }
            }
        }

        if(edgeToMove != null){
            unsettledEdges.remove(edgeToMove);
            settledEdges.add(edgeToMove);
            return true;
        }
                
        return false;
    }

    /**
     *
     * @param locationToUpgrade = the location the current player wants to
     * upgrade the settlement
     * @pre the current player had enough resources to upgrade to a city and a
     * city to build with
     * @post returns true if the VertexLocation is in a Location of
     * settledLocations, if the Location ownerID matches the player, and if the
     * Location's city boolean is false. Otherwise returns false.
     */
    public boolean upgradeToCity(VertexLocation locationToUpgrade) {
        for (Location settledLocation : settledLocations) {
            if (settledLocation.getNormalizedLocation().equivalent(locationToUpgrade)) {
                if (settledLocation.getIsCity()) {
                    return false;
                } else {
                    settledLocation.setIsCity(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param triggeredHex = the hex to reward the possible settlements and/or
     * cities
     * @pre The triggeredHex's rollValue had just been rolled
     * @post Cycles through all neighboring vertexLocations, seeing if any are
     * settled, and award the ownerID of those that are with the appropriate
     * resources
     */
    public ArrayList<Integer> awardTerrainResource(HexLocation triggeredHex) {
        ArrayList<Integer> playerReceivesOneResource = new ArrayList<Integer>();
        for (Location location : settledLocations) {
            ArrayList<HexLocation> hexesAroundVertex = getHexLocationsAroundVertexLocation(location.getNormalizedLocation());

            for (HexLocation hexLocation : hexesAroundVertex) {
                if (hexLocation.equivalent(triggeredHex)) {
                    //this is where the player gets added to a list to receive a resource
                    playerReceivesOneResource.add(location.getOwnerID());
                    if (location.getIsCity()) {
                        playerReceivesOneResource.add(location.getOwnerID());
                    }
                    break;
                }
            }

        }

        return playerReceivesOneResource;
    }

    public ArrayList<HexLocation> getHexLocationsAroundVertexLocation(VertexLocation vertex) {
        ArrayList<HexLocation> hexes = new ArrayList<HexLocation>();

        hexes.add(vertex.getHexLoc());
        hexes.add(new HexLocation(vertex.getHexLoc().getX(), vertex.getHexLoc().getY() - 1));

        if (VertexDirection.NorthEast == vertex.getDir()) {
            hexes.add(new HexLocation(vertex.getHexLoc().getX() + 1, vertex.getHexLoc().getY() - 1));
        } else {
            //case of it's NorthWest
            hexes.add(new HexLocation(vertex.getHexLoc().getX() - 1, vertex.getHexLoc().getY()));
        }

        return hexes;
    }

    /**
     *
     * @pre the player is going to maritime trade
     * @post returns the list of Ports that the player has a settlement or city
     * on
     */
    public ArrayList<Port> getPortsPlayerHas(int playerId) {
        Set<Port> portsAPlayerHas = new HashSet<Port>();
        ArrayList<VertexLocation> settlementsPlayerHas = new ArrayList<VertexLocation>();

        for (Location location : settledLocations) {
            if (location.getOwnerID() == playerId) {
                settlementsPlayerHas.add(location.getNormalizedLocation());
            }
        }

        for (VertexLocation settlementPlayerHas : settlementsPlayerHas) {
            ArrayList<HexLocation> neighboringHexes = getHexLocationsAroundVertexLocation(settlementPlayerHas.getNormalizedLocation());

            for (Port port : ports) {
                for (HexLocation hexLocation : neighboringHexes) {
                    if (port.getLocation().equivalent(hexLocation)) {
                        portsAPlayerHas.add(port);
                    }
                }
            }

        }

        ArrayList<Port> portsPlayerHas = new ArrayList<Port>();
        portsPlayerHas.addAll(portsAPlayerHas);
        return portsPlayerHas;
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
