package shared.locations;

public enum VertexDirection
{
	W, NW, NE, E, SE, SW;
	
	private VertexDirection opposite;
	
	static
	{
		W.opposite = E;
		NW.opposite = SE;
		NE.opposite = SW;
		E.opposite = E;
		SE.opposite = NW;
		SW.opposite = NE;
	}
	
	public VertexDirection getOppositeDirection()
	{
		return opposite;
	}
}

