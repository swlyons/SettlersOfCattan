package shared.locations;

/**
 * Represents the location of a vertex on a hex map
 */
public class VertexLocation {

    private HexLocation location;
    private VertexDirection direction;
    
    public VertexLocation(HexLocation hexLoc, VertexDirection dir) {
        setHexLoc(hexLoc);
        setDir(dir);
    }

    public HexLocation getHexLoc() {
        return location;
    }

    private void setHexLoc(HexLocation hexLoc) {
        if (hexLoc == null) {
            throw new IllegalArgumentException("hexLoc cannot be null");
        }
        location = hexLoc;
    }

    public VertexDirection getDir() {
        return direction;
    }

    private void setDir(VertexDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "{location : " + location + ", direction : " + direction + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        VertexLocation other = (VertexLocation) obj;
        other = other.getNormalizedLocation();
        if (direction != other.direction) {
            return false;
        }
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a canonical (i.e., unique) value for this vertex location. Since
     * each vertex has three different locations on a map, this method converts
     * a vertex location to a single canonical form. This is useful for using
     * vertex locations as map keys.
     *
     * @return Normalized vertex location
     */
    public VertexLocation getNormalizedLocation() {

        // Return location that has direction NW or NE
        switch (direction) {
            case NW:
            case NE:
                return this;
            case W:
                return new VertexLocation(
                        location.getNeighborLoc(EdgeDirection.SW),
                        VertexDirection.NE);
            case SW:
                return new VertexLocation(
                        location.getNeighborLoc(EdgeDirection.S),
                        VertexDirection.NW);
            case SE:
                return new VertexLocation(
                        location.getNeighborLoc(EdgeDirection.S),
                        VertexDirection.NE);
            case E:
                return new VertexLocation(
                        location.getNeighborLoc(EdgeDirection.SE),
                        VertexDirection.NW);
            default:
                assert false;
                return null;
        }
    }
}
