package client.map;

import java.util.*;

import shared.definitions.*;
import shared.locations.*;
import client.base.*;
import client.data.*;
import client.managers.GameManager;
import client.proxy.BuildRoad;
import client.proxy.BuildSettlement;
import client.proxy.FinishMove;
import client.proxy.RobPlayer;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {

	private IRobView robView;
	private boolean isFree = false;
	private boolean endTurn = false;
	
	public MapController(IMapView view, IRobView robView) {

		super(view);

		setRobView(robView);

		// initFromModel();
	}

	public IMapView getView() {

		return (IMapView) super.getView();
	}

	private IRobView getRobView() {
		return robView;
	}

	private void setRobView(IRobView robView) {
		this.robView = robView;
	}

	@Override
	public void initFromModel() {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();

		for (int i = 0; i < gm.getMapManager().getHexList().size(); i++) {
			Hex h = gm.getMapManager().getHexList().get(i);
			if (h.getResource() == ResourceType.wood)
				getView().addHex(h.getLocation(), HexType.wood);
			else if (h.getResource() == ResourceType.brick)
				getView().addHex(h.getLocation(), HexType.brick);
			else if (h.getResource() == ResourceType.sheep)
				getView().addHex(h.getLocation(), HexType.sheep);
			else if (h.getResource() == ResourceType.ore)
				getView().addHex(h.getLocation(), HexType.ore);
			else if (h.getResource() == ResourceType.wheat)
				getView().addHex(h.getLocation(), HexType.wheat);
			else
				getView().addHex(h.getLocation(), HexType.desert);

			if ((h.getNumber() >= 2 && h.getNumber() <= 6) || (h.getNumber() >= 8 && h.getNumber() <= 12))
				getView().addNumber(h.getLocation(), h.getNumber());
		}

		for (Edge e : gm.getLocationManager().getSettledEdges()) {
			getView().placeRoad(e.getEdgeLocation(),
					CatanColor.valueOf(gm.getGame().getPlayers().get(e.getOwnerId()).getColor().toUpperCase()));
		}

		for (Location l : gm.getLocationManager().getSettledLocations()) {
			if (l.getIsCity()) {
				getView().placeCity(l.getNormalizedLocation(),
						CatanColor.valueOf(gm.getGame().getPlayers().get(l.getOwnerID()).getColor().toUpperCase()));
			} else {
				getView().placeSettlement(l.getNormalizedLocation(),
						CatanColor.valueOf(gm.getGame().getPlayers().get(l.getOwnerID()).getColor().toUpperCase()));
			}
		}
                

		getView().placeRobber(gm.getMapManager().getRobberLocation());

		// Water tiles are hard coded, sine they never change, ever.
		getView().addHex(new HexLocation(0, 3), HexType.water);
		getView().addHex(new HexLocation(1, 2), HexType.water);
		getView().addHex(new HexLocation(2, 1), HexType.water);
		getView().addHex(new HexLocation(3, 0), HexType.water);
		getView().addHex(new HexLocation(3, -1), HexType.water);
		getView().addHex(new HexLocation(3, -2), HexType.water);
		getView().addHex(new HexLocation(3, -3), HexType.water);
		getView().addHex(new HexLocation(2, -3), HexType.water);
		getView().addHex(new HexLocation(1, -3), HexType.water);
		getView().addHex(new HexLocation(0, -3), HexType.water);
		getView().addHex(new HexLocation(-1, -2), HexType.water);
		getView().addHex(new HexLocation(-2, -1), HexType.water);
		getView().addHex(new HexLocation(-3, 0), HexType.water);
		getView().addHex(new HexLocation(-3, 1), HexType.water);
		getView().addHex(new HexLocation(-3, 2), HexType.water);
		getView().addHex(new HexLocation(-3, 3), HexType.water);
		getView().addHex(new HexLocation(-2, 3), HexType.water);
		getView().addHex(new HexLocation(-1, 3), HexType.water);

		for (Port p : gm.getGame().getMap().getPorts()) {
			HexLocation location = p.getLocation();
			PortType type = PortType.THREE;
			if (p.getResource() != null) {
				type = PortType.valueOf(p.getResource().toString().toUpperCase());
			}

			// Fancy logic determines edge direction by location
			EdgeDirection direction = EdgeDirection.N;
			if (location.getX() > 1) {
				if (location.getY() > -2)
					direction = EdgeDirection.NW;
				else
					direction = EdgeDirection.SW;
			} else if (location.getX() < -1) {
				if (location.getY() > 1)
					direction = EdgeDirection.NE;
				else
					direction = EdgeDirection.SE;
			} else if (location.getY() < 0)
				direction = EdgeDirection.S;

			getView().addPort(new EdgeLocation(location, direction), type);
		}

	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		return gm.canPlaceRoad(edgeLoc);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		return gm.canPlaceSettlement(vertLoc);
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		return gm.canPlaceCity(vertLoc);
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		return gm.canPlaceRobber(hexLoc);
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
		if (isFree || gm.getLocationManager().getSettledLocations().size() < 9) {
			if (gm.placeFreeRoad(edgeLoc)) {
				CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
				getView().placeRoad(edgeLoc, color);
				BuildRoad br = new BuildRoad();
				br.setFree(true);
				br.setPlayerIndex(currentPlayer);
                                XYEdgeLocation edgeSpot = new XYEdgeLocation();
                                edgeSpot.setDirection(edgeLoc.getDir());
                                edgeSpot.setX(edgeLoc.getHexLoc().getX());
                                edgeSpot.setY(edgeLoc.getHexLoc().getY());
				br.setRoadLocation(edgeSpot);
				br.setType("buildRoad");
				try {
					ClientCommunicatorFascadeSettlersOfCatan.getSingleton().buildRoad(br);
					setEndTurn(true);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} else {
			if (gm.buildRoad(edgeLoc)) {
				CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
				getView().placeRoad(edgeLoc, color);
			}
		}

	}

	public void placeSettlement(VertexLocation vertLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                SettlementLocation settle = new SettlementLocation();
                settle.setDirection(vertLoc.getDir());
                settle.setX(vertLoc.getHexLoc().getX());
                settle.setY(vertLoc.getHexLoc().getY());
		Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
		BuildSettlement build = new BuildSettlement();

		if (isFree || gm.getLocationManager().getSettledLocations().size() < 8) {
			if (gm.getLocationManager().getSettledLocations().size() < 4) {
				gm.placeFirstSettlement(vertLoc);
			} else {
				gm.placeSecondSettlement(vertLoc);
			}
			CatanColor color = CatanColor
					.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());

			build.setFree(true);
			build.setType("buildSettlement");
			build.setPlayerIndex(currentPlayer);
			build.setVertexLocation(settle);
			try {
				ClientCommunicatorFascadeSettlersOfCatan.getSingleton().buildSettlement(build);
				getView().placeSettlement(vertLoc, color);
				startMove(PieceType.ROAD, true, false);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			if (gm.buildStructure(PieceType.SETTLEMENT, vertLoc)) {
				CatanColor color = CatanColor
						.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
				build.setFree(true);
				build.setType("buildSettlement");
				build.setPlayerIndex(currentPlayer);
				build.setVertexLocation(settle);
				try {
					ClientCommunicatorFascadeSettlersOfCatan.getSingleton().buildSettlement(build);
					getView().placeSettlement(vertLoc, color);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

	}

	public void placeCity(VertexLocation vertLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
		if (gm.buildStructure(PieceType.CITY, vertLoc)) {
			CatanColor color = CatanColor
					.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
			getView().placeCity(vertLoc, color);
		}
	}

	public void placeRobber(HexLocation hexLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		if (gm.getMapManager().moveRobber(hexLoc)) {
			getView().placeRobber(hexLoc);
			getRobView().showModal();
		}
	}

	/*
	 * This method is called when the user requests to place a piece on the map
	 * (road, city, or settlement)
	 * 
	 * @param pieceType The type of piece to be placed
	 * 
	 * @param isFree true if the piece should not cost the player resources,
	 * false otherwise. Set to true during initial setup and when a road
	 * building card is played.
	 * 
	 * @param allowDisconnected true if the piece can be disconnected, false
	 * otherwise. Set to true only during initial setup.
	 * 
	 * void startMove(PieceType pieceType, boolean isFree, boolean
	 * allowDisconnected);
	 */

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		this.isFree = isFree;
		Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
		CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor().toUpperCase());
		getView().startDrop(pieceType, color, !allowDisconnected);
	}

	public void cancelMove() {
		// This shouldn't need to do anything. The view closes the map overlay
		// modal, and the way we built this controller, no resources have been
		// taken yet.
	}

	public void playSoldierCard() {
		getRobView().showModal();
		// I don't think this needs to do any more than this. This should just
		// show the robView overlay, which then uses the "CanPlaceRobber" and
		// "robPlayer" methods
	}

	public void playRoadBuildingCard() {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		this.isFree = true;
		int currentPlayerIndex = gm.getGame().getTurnTracker().getCurrentTurn();
		if (gm.getResourceManager().getGameBanks().get(currentPlayerIndex).getRoads() > 0)
			getView().startDrop(PieceType.ROAD,
					CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayerIndex).getColor()), false);
		if (gm.getResourceManager().getGameBanks().get(currentPlayerIndex).getRoads() > 0)
			getView().startDrop(PieceType.ROAD,
					CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayerIndex).getColor()), false);
	}

	public void robPlayer(RobPlayerInfo victim) {
		// Some communication may need to happen to transfer resources between
		// the two players.
		// The RobPlayer class doesn't request a resource, so the resource
		// deciding must happen server side, or on the next poll request
		int index = victim.getPlayerIndex();
		RobPlayer rp = new RobPlayer(index);

		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		HexLocation loc = gm.getMapManager().getRobberLocation();
		rp.setLocation(loc);

		try {
			ClientCommunicatorFascadeSettlersOfCatan.getSingleton().robPlayer(rp);
		} catch (Exception e) {

		}
	}


	public boolean isEndTurn() {
		return endTurn;
	}
	

	public void setEndTurn(boolean endTurn) {
		this.endTurn = endTurn;
	}
}
