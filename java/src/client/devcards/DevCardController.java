package client.devcards;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import client.base.*;
import shared.model.BuyDevCard;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import client.managers.GameManager;
import shared.model.BuyDevCard;
import shared.model.Monopoly;
import shared.model.Monument;
import shared.model.Soldier;
import shared.model.Year_Of_Plenty;
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
     * @param soldierAction Action to be executed when the user plays a soldier
     * card. It calls "mapController.playSoldierCard()".
     * @param roadAction Action to be executed when the user plays a road
     * building card. It calls "mapController.playRoadBuildingCard()".
     */
    public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView,
            IAction soldierAction, IAction roadAction) {

        super(view);

        this.buyCardView = buyCardView;
        this.soldierAction = soldierAction;
        this.roadAction = roadAction;
    }

    public IPlayDevCardView getPlayCardView() {
        return (IPlayDevCardView) super.getView();
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
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        BuyDevCard card = new BuyDevCard(playerIndex);
        try {
            ClientFascade.getSingleton().buyDevCard(card);
            gm.buyDevCard();
        } catch (Exception e) {
            e.printStackTrace();
            //Shouldn't happen
        } finally {
            getBuyCardView().closeModal();
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
        boolean playedDevCard = ClientCommunicator.getSingleton().getGameManager().getGame().getPlayers().get(playerIndex).isPlayedDevCard();
        boolean canBuild = gm.canUseMonopoly() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId) && !playedDevCard;
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
        Integer playerIndex = -1;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }

        Monopoly m = new Monopoly(gm.getPlayerIndex(playerId));
        m.setResource(resource);
        try {
            ClientFascade.getSingleton().monopoly(m);
            gm.useMonopoly(resource);
            if (getPlayCardView().isModalShowing())
            	getPlayCardView().closeModal();
        } catch (Exception e) {
//            e.printStackTrace();
            //Shouldn't happen
        }
    }

    private boolean canPlayMonumentCard(Integer playerId, Integer playerIndex) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        boolean playedDevCard = ClientCommunicator.getSingleton().getGameManager().getGame().getPlayers().get(playerIndex).isPlayedDevCard();
        boolean canBuild = gm.canUseMonument() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId) && !playedDevCard;
        int cardsInHand = gm.getGame().getPlayers().get(playerIndex).getNewDevCards().getMonument()
                + gm.getGame().getPlayers().get(playerIndex).getOldDevCards().getMonument();
        getPlayCardView().setCardAmount(DevCardType.MONUMENT, cardsInHand);
        getPlayCardView().setCardEnabled(DevCardType.MONUMENT, canBuild);
        return canBuild;
    }

    @Override
    public void playMonumentCard() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = -1;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }

        Monument m = new Monument();
        m.setType("Monument");
        m.setPlayerIndex(gm.getPlayerIndex(playerId));
        try {
            ClientFascade.getSingleton().monument(m);
            gm.useMonument();
            if (getPlayCardView().isModalShowing())
            	getPlayCardView().closeModal();
        } catch (Exception e) {
//            e.printStackTrace();
            //Shouldn't happen
        }
    }

    private boolean canPlayRoadBuildCard(Integer playerId, Integer playerIndex) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        boolean playedDevCard = ClientCommunicator.getSingleton().getGameManager().getGame().getPlayers().get(playerIndex).isPlayedDevCard();
        boolean canBuild = gm.canUseRoadBuilding() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId) && !playedDevCard;
        if(gm.getGame().getPlayers().get(playerIndex).getRoads()<2){
            canBuild = false;
        }
        int cardsInHand = gm.getGame().getPlayers().get(playerIndex).getNewDevCards().getRoadBuilding()
                + gm.getGame().getPlayers().get(playerIndex).getOldDevCards().getRoadBuilding();
        getPlayCardView().setCardAmount(DevCardType.ROAD_BUILD, cardsInHand);
        getPlayCardView().setCardEnabled(DevCardType.ROAD_BUILD, canBuild);
        return canBuild;
    }

    @Override
    public void playRoadBuildCard() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
    	Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = -1;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        
        roadAction.execute();
        
        if (getPlayCardView().isModalShowing())
        	getPlayCardView().closeModal();
    }

    private boolean canPlaySoldierCard(Integer playerId, Integer playerIndex) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        boolean playedDevCard = ClientCommunicator.getSingleton().getGameManager().getGame().getPlayers().get(playerIndex).isPlayedDevCard();
        boolean canBuild = gm.canUseSoldier() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId) && !playedDevCard;
        int cardsInHand = gm.getGame().getPlayers().get(playerIndex).getNewDevCards().getSoldier()
                + gm.getGame().getPlayers().get(playerIndex).getOldDevCards().getSoldier();
        getPlayCardView().setCardAmount(DevCardType.SOLDIER, cardsInHand);
        getPlayCardView().setCardEnabled(DevCardType.SOLDIER, canBuild);
        return canBuild;
    }

    @Override
    public void playSoldierCard() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
    	Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = -1;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        
        soldierAction.execute();
        
        if (getPlayCardView().isModalShowing())
        	getPlayCardView().closeModal();
    }

    private boolean canPlayYearOfPlentyCard(Integer playerId, Integer playerIndex) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        boolean playedDevCard = ClientCommunicator.getSingleton().getGameManager().getGame().getPlayers().get(playerIndex).isPlayedDevCard();
        boolean canBuild = gm.canUseYearOfPlenty() && gm.getGame().getTurnTracker().getCurrentTurn() == gm.getPlayerIndex(playerId) && !playedDevCard;
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
        Integer playerIndex = -1;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }

        Year_Of_Plenty y = new Year_Of_Plenty(gm.getPlayerIndex(playerId));
        y.setType("Year_of_Plenty");
        y.setPlayerIndex(playerId);
        y.setResource1(resource1);
        y.setResource2(resource2);
        try {
            ClientFascade.getSingleton().year_Of_Plenty(y);
            gm.useYearOfPlenty(resource1, resource2);
            if (getPlayCardView().isModalShowing())
            	getPlayCardView().closeModal();
        } catch (Exception e) {
            e.printStackTrace();
            //Shouldn't happen
        }
    }

}
