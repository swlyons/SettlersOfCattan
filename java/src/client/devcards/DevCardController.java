package client.devcards;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.managers.GameManager;
import client.proxy.BuyDevCard;
import client.proxy.Monopoly;
import client.proxy.Monument;
import client.proxy.Year_Of_Plenty;
import client.resources.ResourceBarElement;


/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	
	/**
	 * DevCardController constructor
	 * 
	 * @param view "Play dev card" view
	 * @param buyCardView "Buy dev card" view
	 * @param soldierAction Action to be executed when the user plays a soldier card.  It calls "mapController.playSoldierCard()".
	 * @param roadAction Action to be executed when the user plays a road building card.  It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView, 
								IAction soldierAction, IAction roadAction) {

		super(view);
		
		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView)super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return buyCardView;
	}

	@Override
	public void startBuyCard() {
		
		getBuyCardView().showModal();
	}

	@Override
	public void cancelBuyCard() {
		
		getBuyCardView().closeModal();
	}

	@Override
	public void buyCard() {		
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
		try {
			ClientCommunicatorFascadeSettlersOfCatan.getSingleton().buyDevCard(new BuyDevCard(gm.getPlayerIndex(playerId)));
			gm.buyDevCard();
			getBuyCardView().closeModal();			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//Shouldn't happen
		}
	}

	@Override
	public void startPlayCard() {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
		Integer playerIndex = -1;		
		for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
			if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
				playerIndex = i;
				break;
			}
		}
		
		getPlayCardView().showModal();
		canPlayMonopolyCard(playerId, playerIndex);
		canPlayMonumentCard(playerId, playerIndex);
		canPlayRoadBuildCard(playerId, playerIndex);
		canPlaySoldierCard(playerId, playerIndex);
		canPlayYearOfPlentyCard(playerId, playerIndex);
	}

	@Override
	public void cancelPlayCard() {
		getPlayCardView().closeModal();
	}

	private boolean canPlayMonopolyCard(Integer playerId, Integer playerIndex) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		boolean canBuild = gm.canUseMonopoly() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
		int cardsInHand = gm.getGame().getPlayers().get(playerIndex).getNewDevCards().getMonopoly()
				+ gm.getGame().getPlayers().get(playerIndex).getOldDevCards().getMonopoly();
		getPlayCardView().setCardAmount(DevCardType.MONOPOLY, cardsInHand);
		getPlayCardView().setCardEnabled(DevCardType.MONOPOLY, canBuild);
		return canBuild;
	}
	
	@Override
	public void playMonopolyCard(ResourceType resource) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		Integer playerId = ClientCommunicator.getSingleton().getPlayerId();

		Monopoly m = new Monopoly(gm.getPlayerIndex(playerId));
		m.setResource(resource);
		try {
			ClientCommunicatorFascadeSettlersOfCatan.getSingleton().monopoly(m);			
			gm.useMonopoly(resource);
			getPlayCardView().closeModal();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//Shouldn't happen
		}
	}
	
	private boolean canPlayMonumentCard(Integer playerId, Integer playerIndex) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		boolean canBuild = gm.canUseMonument() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
		int cardsInHand = gm.getGame().getPlayers().get(playerIndex).getNewDevCards().getMonument()
				+ gm.getGame().getPlayers().get(playerIndex).getOldDevCards().getMonument();
		getPlayCardView().setCardAmount(DevCardType.MONUMENT, cardsInHand);
		getPlayCardView().setCardEnabled(DevCardType.MONUMENT, canBuild);
		return canBuild;		
	}

	@Override
	public void playMonumentCard() {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();

		try {
			ClientCommunicatorFascadeSettlersOfCatan.getSingleton().monument(new Monument());			
			gm.useMonument();
			getPlayCardView().closeModal();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//Shouldn't happen
		}		
	}

	private boolean canPlayRoadBuildCard(Integer playerId, Integer playerIndex) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		boolean canBuild = gm.canUseRoadBuilding() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
		int cardsInHand = gm.getGame().getPlayers().get(playerIndex).getNewDevCards().getRoadBuilding()
				+ gm.getGame().getPlayers().get(playerIndex).getOldDevCards().getRoadBuilding();
		getPlayCardView().setCardAmount(DevCardType.ROAD_BUILD, cardsInHand);
		getPlayCardView().setCardEnabled(DevCardType.ROAD_BUILD, canBuild);
		return canBuild;
	}
	
	@Override
	public void playRoadBuildCard() {
		
		roadAction.execute();
	}

	private boolean canPlaySoldierCard(Integer playerId, Integer playerIndex) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		boolean canBuild = gm.canUseSoldier() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
		int cardsInHand = gm.getGame().getPlayers().get(playerIndex).getNewDevCards().getSoldier()
				+ gm.getGame().getPlayers().get(playerIndex).getOldDevCards().getSoldier();
		getPlayCardView().setCardAmount(DevCardType.SOLDIER, cardsInHand);
		getPlayCardView().setCardEnabled(DevCardType.SOLDIER, canBuild);
		return canBuild;
	}
	
	@Override
	public void playSoldierCard() {
		
		soldierAction.execute();
	}

	private boolean canPlayYearOfPlentyCard(Integer playerId, Integer playerIndex) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		boolean canBuild = gm.canUseYearOfPlenty() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId);
		int cardsInHand = gm.getGame().getPlayers().get(playerIndex).getNewDevCards().getYearOfPlenty()
				+ gm.getGame().getPlayers().get(playerIndex).getOldDevCards().getYearOfPlenty();
		getPlayCardView().setCardAmount(DevCardType.YEAR_OF_PLENTY, cardsInHand);
		getPlayCardView().setCardEnabled(DevCardType.YEAR_OF_PLENTY, canBuild);
		return canBuild;
	}
	
	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
		Integer playerId = ClientCommunicator.getSingleton().getPlayerId();

		Year_Of_Plenty y = new Year_Of_Plenty(gm.getPlayerIndex(playerId));
		y.setResource1(resource1);
		y.setResource2(resource2);
		try {
			ClientCommunicatorFascadeSettlersOfCatan.getSingleton().year_Of_Plenty(y);			
			gm.useYearOfPlenty(resource1, resource2);
			getPlayCardView().closeModal();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//Shouldn't happen
		}
	}

}

