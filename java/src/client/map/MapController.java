package client.map;

import java.util.*;

import shared.definitions.*;
import shared.locations.*;
import client.base.*;
import client.data.*;
import client.managers.GameManager;
import client.proxy.RobPlayer;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {

	private IRobView robView;
	private boolean isFree = false;

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
					CatanColor.valueOf(gm.getGame().getPlayers().get(e.getOwnerId()).getColor()));
		}

		for (Location l : gm.getLocationManager().getSettledLocations()) {
			if (l.getIsCity()) {
				getView().placeCity(l.getNormalizedLocation(),
						CatanColor.valueOf(gm.getGame().getPlayers().get(l.getOwnerID()).getColor()));
			} else {
				getView().placeSettlement(l.getNormalizedLocation(),
						CatanColor.valueOf(gm.getGame().getPlayers().get(l.getOwnerID()).getColor()));
			}
		}

		getView().placeRobber(gm.getMapManager().getRobberLocation());
		// <temp>

		// Random rand = new Random();
		//
		// for (int x = 0; x <= 3; ++x) {
		//
		// int maxY = 3 - x;
		// for (int y = -3; y <= maxY; ++y) {
		// int r = rand.nextInt(HexType.values().length);
		// HexType hexType = HexType.values()[r];
		// HexLocation hexLoc = new HexLocation(x, y);
		// getView().addHex(hexLoc, hexType);
		// getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NW),
		// CatanColor.RED);
		// getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SW),
		// CatanColor.BLUE);
		// getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.S),
		// CatanColor.ORANGE);
		// getView().placeSettlement(new VertexLocation(hexLoc,
		// VertexDirection.NW), CatanColor.GREEN);
		// getView().placeCity(new VertexLocation(hexLoc, VertexDirection.NE),
		// CatanColor.PURPLE);
		// }
		//
		// if (x != 0) {
		// int minY = x - 3;
		// for (int y = minY; y <= 3; ++y) {
		// int r = rand.nextInt(HexType.values().length);
		// HexType hexType = HexType.values()[r];
		// HexLocation hexLoc = new HexLocation(-x, y);
		// getView().addHex(hexLoc, hexType);
		// getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NW),
		// CatanColor.RED);
		// getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SW),
		// CatanColor.BLUE);
		// getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.S),
		// CatanColor.ORANGE);
		// getView().placeSettlement(new VertexLocation(hexLoc,
		// VertexDirection.NW), CatanColor.GREEN);
		// getView().placeCity(new VertexLocation(hexLoc, VertexDirection.NE),
		// CatanColor.PURPLE);
		// }
		// }
		// }
		//
		// PortType portType = PortType.BRICK;
		// getView().addPort(new EdgeLocation(new HexLocation(0, 3),
		// EdgeDirection.N), portType);
		// getView().addPort(new EdgeLocation(new HexLocation(0, -3),
		// EdgeDirection.S), portType);
		// getView().addPort(new EdgeLocation(new HexLocation(-3, 3),
		// EdgeDirection.NE), portType);
		// getView().addPort(new EdgeLocation(new HexLocation(-3, 0),
		// EdgeDirection.SE), portType);
		// getView().addPort(new EdgeLocation(new HexLocation(3, -3),
		// EdgeDirection.SW), portType);
		// getView().addPort(new EdgeLocation(new HexLocation(3, 0),
		// EdgeDirection.NW), portType);
		//
		// getView().placeRobber(new HexLocation(0, 0));
		//
		// getView().addNumber(new HexLocation(-2, 0), 2);
		// getView().addNumber(new HexLocation(-2, 1), 3);
		// getView().addNumber(new HexLocation(-2, 2), 4);
		// getView().addNumber(new HexLocation(-1, 0), 5);
		// getView().addNumber(new HexLocation(-1, 1), 6);
		// getView().addNumber(new HexLocation(1, -1), 8);
		// getView().addNumber(new HexLocation(1, 0), 9);
		// getView().addNumber(new HexLocation(2, -2), 10);
		// getView().addNumber(new HexLocation(2, -1), 11);
		// getView().addNumber(new HexLocation(2, 0), 12);

		// </temp>
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
		if (isFree) {
			if (gm.placeFreeRoad(edgeLoc)) {
				CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor());
				getView().placeRoad(edgeLoc, color);
			}
		} else {
			if (gm.buildRoad(edgeLoc)) {
				CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor());
				getView().placeRoad(edgeLoc, color);
			}
		}

	}

	public void placeSettlement(VertexLocation vertLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
		if (isFree) {
			if (gm.getLocationManager().getSettledLocations().size() < 4) {
				gm.placeFirstSettlement(vertLoc);
				CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor());
				getView().placeSettlement(vertLoc, color);
			} else {
				if (gm.getLocationManager().getSettledLocations().size() < 8) {
					gm.placeSecondSettlement(vertLoc);
					CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor());
					getView().placeSettlement(vertLoc, color);
				}
			}
		} else {
			if (gm.buildStructure(PieceType.SETTLEMENT, vertLoc)) {
				CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor());
				getView().placeSettlement(vertLoc, color);
			}
		}
	}

	public void placeCity(VertexLocation vertLoc) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		Integer currentPlayer = gm.getGame().getTurnTracker().getCurrentTurn();
		if (gm.buildStructure(PieceType.CITY, vertLoc)) {
			CatanColor color = CatanColor.valueOf(gm.getGame().getPlayers().get(currentPlayer).getColor());
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
		getView().startDrop(pieceType, color, true);
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
}
