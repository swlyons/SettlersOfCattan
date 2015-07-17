package shared.locations;

/**
 * Represents the location of an edge on a hex map
 */
public class EdgeLocation
{
	
	private HexLocation location;
	private EdgeDirection direction;
	
	public EdgeLocation(HexLocation location, EdgeDirection direction)
	{
		setHexLoc(location);
		setDir(direction);
	}
	
	public HexLocation getHexLoc()
	{
		return location;
	}
	
	private void setHexLoc(HexLocation location)
	{
		if(location == null)
		{
			throw new IllegalArgumentException("hexLoc cannot be null");
		}
		this.location = location;
	}
	
	public EdgeDirection getDir()
	{
		return direction;
	}
	
	private void setDir(EdgeDirection dir)
	{
		this.direction = dir;
	}
	
	@Override
	public String toString()
	{
		return "EdgeLocation [hexLoc=" + location + ", dir=" + direction + "]";
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		EdgeLocation other = (EdgeLocation)obj;
                other = other.getNormalizedLocation();
		if(direction != other.direction)
			return false;
		if(location == null)
		{
			if(other.location != null)
				return false;
		}
		else if(!location.equals(other.location))
			return false;
		return true;
	}
	
	/**
	 * Returns a canonical (i.e., unique) value for this edge location. Since
	 * each edge has two different locations on a map, this method converts a
	 * hex location to a single canonical form. This is useful for using hex
	 * locations as map keys.
	 * 
	 * @return Normalized hex location
	 */
	public EdgeLocation getNormalizedLocation()
	{
		
		// Return an EdgeLocation that has direction NW, N, or NE
		
		switch (direction)
		{
			case NW:
			case N:
			case NE:
				return this;
			case SW:
			case S:
			case SE:
				return new EdgeLocation(location.getNeighborLoc(direction),
										direction.getOppositeDirection());
			default:
				assert false;
				return null;
		}
	}
}

