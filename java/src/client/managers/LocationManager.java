package client.managers;

import client.data.Edge;
import client.data.Location;
import client.data.Port;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.locations.VertexDirection;


public class LocationManager {

    private List<Location> settledLocations;
    private List<Location> unsettledLocations;
    private List<Edge> settledEdges;
    private List<Edge> unsettledEdges;
    private List<Port> ports;

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
            if (location.getNormalizedLocation().equals(locationToSettle)) {
                if (firstRound) {
                    location.getWhoCanBuild().add(playerId);
                }

                if (location.getCanBeSettled() && location.getWhoCanBuild().contains(playerId)) {
                    location.setCanBeSettled(false);
                    locationToMove = location;
                    location.setOwnerID(playerId);
                    location.getWhoCanBuild().clear();
                    location.getWhoCanBuild().add(playerId);

                    if (location.getNormalizedLocation().getDir() == VertexDirection.NE) {
                        VertexLocation unusableLocation1 = new VertexLocation(location.getNormalizedLocation().getHexLoc(), VertexDirection.NW);
                        VertexLocation unusableLocation2 = new VertexLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() + 1, location.getNormalizedLocation().getHexLoc().getY()), VertexDirection.NW);
                        VertexLocation unusableLocation3 = new VertexLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() + 1, location.getNormalizedLocation().getHexLoc().getY() - 1), VertexDirection.NW);
                        for (Location locationUnavailable : unsettledLocations) {
                            if (locationUnavailable.getNormalizedLocation().equals(unusableLocation1)
                                    || locationUnavailable.getNormalizedLocation().equals(unusableLocation2)
                                    || locationUnavailable.getNormalizedLocation().equals(unusableLocation3)) {
                                locationUnavailable.setCanBeSettled(false);
                            }
                        }

                        if (firstRound) {
                            EdgeLocation usableEdge1 = new EdgeLocation(location.getNormalizedLocation().getHexLoc(), EdgeDirection.N);
                            EdgeLocation usableEdge2 = new EdgeLocation(location.getNormalizedLocation().getHexLoc(), EdgeDirection.NE);
                            EdgeLocation usableEdge3 = new EdgeLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() + 1, location.getNormalizedLocation().getHexLoc().getY() - 1), EdgeDirection.NW);
                            for (Edge edge : unsettledEdges) {
                                if (edge.getEdgeLocation().equals(usableEdge1)
                                        || edge.getEdgeLocation().equals(usableEdge2)
                                        || edge.getEdgeLocation().equals(usableEdge3)) {
                                    edge.getWhoCanBuild().add(playerId);
                                }
                            }
                        }

                    }

                    if (location.getNormalizedLocation().getDir() == VertexDirection.NW) {
                        VertexLocation unusableLocation1 = new VertexLocation(location.getNormalizedLocation().getHexLoc(), VertexDirection.NE);
                        VertexLocation unusableLocation2 = new VertexLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() - 1, location.getNormalizedLocation().getHexLoc().getY()), VertexDirection.NE);
                        VertexLocation unusableLocation3 = new VertexLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() - 1, location.getNormalizedLocation().getHexLoc().getY() + 1), VertexDirection.NE);
                        for (Location locationUnavailable : unsettledLocations) {
                            if (locationUnavailable.getNormalizedLocation().equals(unusableLocation1)
                                    || locationUnavailable.getNormalizedLocation().equals(unusableLocation2)
                                    || locationUnavailable.getNormalizedLocation().equals(unusableLocation3)) {
                                locationUnavailable.setCanBeSettled(false);
                            }
                        }

                        if (firstRound) {
                            EdgeLocation usableEdge1 = new EdgeLocation(location.getNormalizedLocation().getHexLoc(), EdgeDirection.N);
                            EdgeLocation usableEdge2 = new EdgeLocation(location.getNormalizedLocation().getHexLoc(), EdgeDirection.NW);
                            EdgeLocation usableEdge3 = new EdgeLocation(new HexLocation(location.getNormalizedLocation().getHexLoc().getX() - 1, location.getNormalizedLocation().getHexLoc().getY()), EdgeDirection.NE);
                            for (Edge edge : unsettledEdges) {
                                if (edge.getEdgeLocation().equals(usableEdge1)
                                        || edge.getEdgeLocation().equals(usableEdge2)
                                        || edge.getEdgeLocation().equals(usableEdge3)) {
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
            if (edgeLocationToSettle.equals(edge.getEdgeLocation())) {
                if (edge.getWhoCanBuild().contains(playerId)) {
                    edge.setOwnerId(playerId);
                    edgeToMove = edge;
                    edge.getWhoCanBuild().clear();

                    if (edge.getEdgeLocation().getDir() == EdgeDirection.N) {
                        VertexLocation settleable1 = new VertexLocation(edge.getEdgeLocation().getHexLoc(), VertexDirection.NE);
                        VertexLocation settleable2 = new VertexLocation(edge.getEdgeLocation().getHexLoc(), VertexDirection.NE);
                        for (Location location : unsettledLocations) {
                            if (location.getNormalizedLocation().equals(settleable1)
                                    || location.getNormalizedLocation().equals(settleable2)) {
                                location.getWhoCanBuild().add(playerId);
                            }
                        }

                        EdgeLocation availableEdge1 = new EdgeLocation(edge.getEdgeLocation().getHexLoc(), EdgeDirection.NE);
                        EdgeLocation availableEdge2 = new EdgeLocation(edge.getEdgeLocation().getHexLoc(), EdgeDirection.NW);
                        EdgeLocation availableEdge3 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY() - 1), EdgeDirection.NW);
                        EdgeLocation availableEdge4 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY()), EdgeDirection.NE);

                        for (Edge addToEdge : unsettledEdges) {
                            if (addToEdge.getEdgeLocation().equals(availableEdge1)
                                    || addToEdge.getEdgeLocation().equals(availableEdge2)
                                    || addToEdge.getEdgeLocation().equals(availableEdge3)
                                    || addToEdge.getEdgeLocation().equals(availableEdge4)) {
                                addToEdge.getWhoCanBuild().add(playerId);
                            }
                        }
                    }

                    if (edge.getEdgeLocation().getDir() == EdgeDirection.NE) {
                        VertexLocation settleable1 = new VertexLocation(edge.getEdgeLocation().getHexLoc(), VertexDirection.NE);
                        VertexLocation settleable2 = new VertexLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY()), VertexDirection.NW);
                        for (Location location : unsettledLocations) {
                            if (location.getNormalizedLocation().equals(settleable1)
                                    || location.getNormalizedLocation().equals(settleable2)) {
                                location.getWhoCanBuild().add(playerId);
                            }
                        }

                        EdgeLocation availableEdge1 = new EdgeLocation(edge.getEdgeLocation().getHexLoc(), EdgeDirection.N);
                        EdgeLocation availableEdge2 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY() - 1), EdgeDirection.NW);
                        EdgeLocation availableEdge3 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY()), EdgeDirection.NW);
                        EdgeLocation availableEdge4 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() + 1, edge.getEdgeLocation().getHexLoc().getY()), EdgeDirection.N);

                        for (Edge addToEdge : unsettledEdges) {
                            if (addToEdge.getEdgeLocation().equals(availableEdge1)
                                    || addToEdge.getEdgeLocation().equals(availableEdge2)
                                    || addToEdge.getEdgeLocation().equals(availableEdge3)
                                    || addToEdge.getEdgeLocation().equals(availableEdge4)) {
                                addToEdge.getWhoCanBuild().add(playerId);
                            }
                        }
                    }

                    if (edge.getEdgeLocation().getDir() == EdgeDirection.NW) {
                        VertexLocation settleable1 = new VertexLocation(edge.getEdgeLocation().getHexLoc(), VertexDirection.NW);
                        VertexLocation settleable2 = new VertexLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY() + 1), VertexDirection.NE);
                        for (Location location : unsettledLocations) {
                            if (location.getNormalizedLocation().equals(settleable1)
                                    || location.getNormalizedLocation().equals(settleable2)) {
                                location.getWhoCanBuild().add(playerId);
                            }
                        }

                        EdgeLocation availableEdge1 = new EdgeLocation(edge.getEdgeLocation().getHexLoc(), EdgeDirection.N);
                        EdgeLocation availableEdge2 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY()), EdgeDirection.NE);
                        EdgeLocation availableEdge3 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY() + 1), EdgeDirection.NE);
                        EdgeLocation availableEdge4 = new EdgeLocation(new HexLocation(edge.getEdgeLocation().getHexLoc().getX() - 1, edge.getEdgeLocation().getHexLoc().getY() + 1), EdgeDirection.N);

                        for (Edge addToEdge : unsettledEdges) {
                            if (addToEdge.getEdgeLocation().equals(availableEdge1)
                                    || addToEdge.getEdgeLocation().equals(availableEdge2)
                                    || addToEdge.getEdgeLocation().equals(availableEdge3)
                                    || addToEdge.getEdgeLocation().equals(availableEdge4)) {
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
            if (settledLocation.getNormalizedLocation().equals(locationToUpgrade)) {
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
    public List<Integer> awardTerrainResource(HexLocation triggeredHex) {
        List<Integer> playerReceivesOneResource = new ArrayList<>();
        for (Location location : settledLocations) {
            List<HexLocation> hexesAroundVertex = getHexLocationsAroundVertexLocation(location.getNormalizedLocation());

            for (HexLocation hexLocation : hexesAroundVertex) {
                if (hexLocation.equals(triggeredHex)) {
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

    public List<HexLocation> getHexLocationsAroundVertexLocation(VertexLocation vertex) {
        List<HexLocation> hexes = new ArrayList<>();

        hexes.add(vertex.getHexLoc());
        hexes.add(new HexLocation(vertex.getHexLoc().getX(), vertex.getHexLoc().getY() - 1));

        if (VertexDirection.NE == vertex.getDir()) {
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
    public List<Port> getPortsPlayerHas(int playerId) {
        Set<Port> portsAPlayerHas = new HashSet<Port>();
        ArrayList<VertexLocation> settlementsPlayerHas = new ArrayList<VertexLocation>();

        for (Location location : settledLocations) {
            if (location.getOwnerID() == playerId) {
                settlementsPlayerHas.add(location.getNormalizedLocation());
            }
        }

        for (VertexLocation settlementPlayerHas : settlementsPlayerHas) {
            List<HexLocation> neighboringHexes = getHexLocationsAroundVertexLocation(settlementPlayerHas.getNormalizedLocation());

            for (Port port : ports) {
                for (HexLocation hexLocation : neighboringHexes) {
                    if (port.getLocation().equals(hexLocation)) {
                        portsAPlayerHas.add(port);
                    }
                }
            }

        }

        List<Port> portsPlayerHas = new ArrayList<Port>();
        portsPlayerHas.addAll(portsAPlayerHas);
        return portsPlayerHas;
    }

    public List<Location> getSettledLocations() {
        return settledLocations;
    }

    public List<Location> getUnsettledLocations() {
        return unsettledLocations;
    }

    public void setSettledLocations(List<Location> settledLocationsNew) {
        settledLocations = settledLocationsNew;
    }

    public void setUnsettledLocations(List<Location> unsettledLocationsNew) {
        unsettledLocations = unsettledLocationsNew;
    }

    public List<Edge> getSettledEdges() {
        return settledEdges;
    }

    public List<Edge> getUnsettledEdges() {
        return unsettledEdges;
    }

    public void setSettledEdges(List<Edge> settledEdgesNew) {
        settledEdges = settledEdgesNew;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> portsNew) {
        ports = portsNew;
    }

    public void setUnsettledEdges(List<Edge> unsettledEdgesNew) {
        unsettledEdges = unsettledEdgesNew;
    }

}
