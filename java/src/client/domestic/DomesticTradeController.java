package client.domestic;

import shared.definitions.*;
import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import client.managers.GameManager;
import shared.data.PlayerInfo;
import shared.data.ResourceList;
import shared.model.OfferTrade;
import shared.model.AcceptTrade;
import client.misc.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController {

    private IDomesticTradeOverlay tradeOverlay;
    private IWaitView waitOverlay;
    private IAcceptTradeOverlay acceptOverlay;

    private ResourceList resourcesPlayerHas;
    private ResourceList offer;
    private boolean addedPlayers;
    private boolean sendingBrick;
    private boolean sendingSheep;
    private boolean sendingOre;
    private boolean sendingWheat;
    private boolean sendingWood;
    private boolean waitingForOffer;
    private int tradeWithIndex;

    /**
     * DomesticTradeController constructor
     *
     * @param tradeView Domestic trade view (i.e., view that contains the
     * "Domestic Trade" button)
     * @param tradeOverlay Domestic trade overlay (i.e., view that lets the user
     * propose a domestic trade)
     * @param waitOverlay Wait overlay used to notify the user they are waiting
     * for another player to accept a trade
     * @param acceptOverlay Accept trade overlay which lets the user accept or
     * reject a proposed trade
     */
    public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
            IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

        super(tradeView);
        addedPlayers = false;
        waitingForOffer = false;
        setTradeOverlay(tradeOverlay);
        setWaitOverlay(waitOverlay);
        setAcceptOverlay(acceptOverlay);
    }

    public IDomesticTradeView getTradeView() {

        return (IDomesticTradeView) super.getView();
    }

    public IDomesticTradeOverlay getTradeOverlay() {
        return tradeOverlay;
    }

    public void setTradeOverlay(IDomesticTradeOverlay tradeOverlay) {
        this.tradeOverlay = tradeOverlay;
    }

    public IWaitView getWaitOverlay() {
        return waitOverlay;
    }

    public void setWaitOverlay(IWaitView waitView) {
        this.waitOverlay = waitView;
    }

    public IAcceptTradeOverlay getAcceptOverlay() {
        return acceptOverlay;
    }

    public void setAcceptOverlay(IAcceptTradeOverlay acceptOverlay) {
        this.acceptOverlay = acceptOverlay;
    }

    public void initFromModel() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }

        if (playerIndex == gm.getGame().getTurnTracker().getCurrentTurn()) {
            getTradeOverlay().setResourceSelectionEnabled(true);
            getTradeOverlay().setPlayerSelectionEnabled(true);
            getTradeOverlay().setStateMessage("set the trade you want to make");
        } else {
            getTradeOverlay().setResourceSelectionEnabled(false);
            getTradeOverlay().setPlayerSelectionEnabled(false);
            getTradeOverlay().setStateMessage("not your turn");
        }

        getTradeOverlay().setTradeEnabled(false);
        setResourcesPlayerHas();
        offer = new ResourceList();
        sendingBrick = false;
        sendingSheep = false;
        sendingOre = false;
        sendingWheat = false;
        sendingWood = false;
        tradeWithIndex = -1;
        unsetResource(ResourceType.brick);
        unsetResource(ResourceType.ore);
        unsetResource(ResourceType.sheep);
        unsetResource(ResourceType.wheat);
        unsetResource(ResourceType.wood);

    }

    @Override
    public void startTrade() {
        getTradeOverlay().showModal();
        initFromModel();
        if (!addedPlayers) {
            GameManager gm = ClientCommunicator.getSingleton().getGameManager();
            Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
            int playerIndex = 4;
            for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
                if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                    playerIndex = i;
                    break;
                }
            }
            List<PlayerInfo> playerss = gm.getGame().getPlayers();
            playerss.remove(playerIndex);
            PlayerInfo[] players = new PlayerInfo[playerss.size()];
            playerss.toArray(players);
            getTradeOverlay().setPlayers(players);
            addedPlayers = true;
        }
    }

    @Override
    public void decreaseResourceAmount(ResourceType resource) {
        int amount = -1;
        switch (resource) {
            case brick:
                offer.setBrick(offer.getBrick() - 1);
                amount = offer.getBrick();
                break;
            case sheep:
                offer.setSheep(offer.getSheep() - 1);
                amount = offer.getSheep();
                break;
            case ore:
                offer.setOre(offer.getOre() - 1);
                amount = offer.getOre();
                break;
            case wheat:
                offer.setWheat(offer.getWheat() - 1);
                amount = offer.getWheat();
                break;
            case wood:
                offer.setWood(offer.getWood() - 1);
                amount = offer.getWood();
                break;
            default:
                break;
        }

        getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
        if (amount <= 0) {
            getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
        }
        getTradeOverlay().setResourceAmount(resource, Integer.toString(amount));
        calculateIfAbleToTrade();
    }

    @Override
    public void increaseResourceAmount(ResourceType resource) {
        int amount = -1;
        boolean disableIncrease = false;
        switch (resource) {
            case brick:
                offer.setBrick(offer.getBrick() + 1);
                amount = offer.getBrick();
                if (sendingBrick && offer.getBrick() == resourcesPlayerHas.getBrick()) {
                    disableIncrease = true;
                }
                break;
            case sheep:
                offer.setSheep(offer.getSheep() + 1);
                amount = offer.getSheep();
                if (sendingSheep && offer.getSheep() == resourcesPlayerHas.getSheep()) {
                    disableIncrease = true;
                }
                break;
            case ore:
                offer.setOre(offer.getOre() + 1);
                amount = offer.getOre();
                if (sendingOre && offer.getOre() == resourcesPlayerHas.getOre()) {
                    disableIncrease = true;
                }
                break;
            case wheat:
                offer.setWheat(offer.getWheat() + 1);
                amount = offer.getWheat();
                if (sendingWheat && offer.getWheat() == resourcesPlayerHas.getWheat()) {
                    disableIncrease = true;
                }
                break;
            case wood:
                offer.setWood(offer.getWood() + 1);
                amount = offer.getWood();
                if (sendingWood && offer.getWood() == resourcesPlayerHas.getWood()) {
                    disableIncrease = true;
                }
                break;
            default:
                break;
        }

        getTradeOverlay().setResourceAmountChangeEnabled(resource, !disableIncrease, true);
        getTradeOverlay().setResourceAmount(resource, Integer.toString(amount));
        calculateIfAbleToTrade();
    }

    @Override
    public void sendTradeOffer() {
        if (!sendingBrick) {
            offer.setBrick(0 - offer.getBrick());
        }
        if (!sendingOre) {
            offer.setOre(0 - offer.getOre());
        }
        if (!sendingSheep) {
            offer.setSheep(0 - offer.getSheep());
        }
        if (!sendingWheat) {
            offer.setWheat(0 - offer.getWheat());
        }
        if (!sendingWood) {
            offer.setWood(0 - offer.getWood());
        }

        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }

        OfferTrade trade = new OfferTrade();
        trade.setPlayerIndex(playerIndex);
        trade.setType("offerTrade");
        trade.setOffer(offer);
        trade.setReceiver(tradeWithIndex);

        try {
            ClientFascade.getSingleton().offerTrade(trade);
            getWaitOverlay().showModal();
            getWaitOverlay().setMessage("Waiting for Trade to Go Through");
            waitingForOffer = true;
        } catch (Exception e) {
            e.printStackTrace();
            getTradeOverlay().reset();
            getTradeOverlay().closeModal();
        }
    }

    public boolean getWaitingForOffer() {
        return waitingForOffer;
    }

    public void setWaitingForOffer(boolean waitingForOffer) {
        this.waitingForOffer = waitingForOffer;
    }

    @Override
    public void setPlayerToTradeWith(int playerIndex) {
        tradeWithIndex = playerIndex;
        calculateIfAbleToTrade();
    }

    private void calculateIfAbleToTrade() {
        if (tradeWithIndex == -1) {
            getTradeOverlay().setStateMessage("set the trade you want to make");
            getTradeOverlay().setTradeEnabled(false);
        } else {
            int sending = 0;
            int receiving = 0;

            if (sendingBrick) {
                sending += offer.getBrick();
            } else {
                receiving += offer.getBrick();
            }
            if (sendingOre) {
                sending += offer.getOre();
            } else {
                receiving += offer.getOre();
            }
            if (sendingSheep) {
                sending += offer.getSheep();
            } else {
                receiving += offer.getSheep();
            }
            if (sendingWheat) {
                sending += offer.getWheat();
            } else {
                receiving += offer.getWheat();
            }
            if (sendingWood) {
                sending += offer.getWood();
            } else {
                receiving += offer.getWood();
            }

            if (sending == 0 || receiving == 0) {
                getTradeOverlay().setStateMessage("set the trade you want to make");
                getTradeOverlay().setTradeEnabled(false);
            } else {
                getTradeOverlay().setStateMessage("Trade!");
                getTradeOverlay().setTradeEnabled(true);
            }
        }
    }

    @Override
    public void setResourceToReceive(ResourceType resource) {
        switch (resource) {
            case brick:
                offer.setBrick(0);
                sendingBrick = false;
                break;
            case sheep:
                offer.setSheep(0);
                sendingSheep = false;
                break;
            case ore:
                offer.setOre(0);
                sendingOre = false;
                break;
            case wheat:
                offer.setWheat(0);
                sendingWheat = false;
                break;
            case wood:
                offer.setWood(0);
                sendingWood = false;
                break;
            default:
                break;
        }

        getTradeOverlay().setResourceAmount(resource, "0");
        getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
        calculateIfAbleToTrade();
    }

    @Override
    public void setResourceToSend(ResourceType resource) {
        boolean hasResource = true;
        switch (resource) {
            case brick:
                offer.setBrick(0);
                sendingBrick = true;
                hasResource = resourcesPlayerHas.getBrick() != 0;
                break;
            case sheep:
                offer.setSheep(0);
                sendingSheep = true;
                hasResource = resourcesPlayerHas.getSheep() != 0;
                break;
            case ore:
                offer.setOre(0);
                hasResource = resourcesPlayerHas.getOre() != 0;
                sendingOre = true;
                break;
            case wheat:
                offer.setWheat(0);
                hasResource = resourcesPlayerHas.getWheat() != 0;
                sendingWheat = true;
                break;
            case wood:
                offer.setWood(0);
                sendingWood = true;
                hasResource = resourcesPlayerHas.getWood() != 0;
                break;
            default:
                break;
        }
        getTradeOverlay().setResourceAmount(resource, "0");
        getTradeOverlay().setResourceAmountChangeEnabled(resource, hasResource, false);
        calculateIfAbleToTrade();
    }

    @Override
    public void unsetResource(ResourceType resource) {
        switch (resource) {
            case brick:
                offer.setBrick(0);
                break;
            case sheep:
                offer.setSheep(0);
                break;
            case ore:
                offer.setOre(0);
                break;
            case wheat:
                offer.setWheat(0);
                break;
            case wood:
                offer.setWood(0);
                break;
            default:
                break;
        }
        getTradeOverlay().setResourceAmount(resource, "");
        getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
        calculateIfAbleToTrade();
    }

    @Override
    public void cancelTrade() {

        getTradeOverlay().closeModal();
    }

    @Override
    public void acceptTrade(boolean willAccept) {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        if (willAccept) {
            ResourceList theNewOffer = gm.getGame().getTradeOffer().getOffer();
            ResourceList myCards = gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();
            gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards().setBrick(myCards.getBrick() + theNewOffer.getBrick());
            gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards().setOre(myCards.getOre() + theNewOffer.getOre());
            gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards().setSheep(myCards.getSheep() + theNewOffer.getSheep());
            gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards().setWheat(myCards.getWheat() + theNewOffer.getWheat());
            gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards().setWood(myCards.getWood() + theNewOffer.getWood());
        }
        AcceptTrade acceptTrade = new AcceptTrade();
        acceptTrade.setPlayerIndex(playerIndex);
        acceptTrade.setType("acceptTrade");
        acceptTrade.setWillAccept(willAccept);
        try {
            ClientFascade.getSingleton().acceptTrade(acceptTrade);
        } catch (Exception e) {
        } finally {
            getAcceptOverlay().closeModal();
        }
    }

    public void setResourcesPlayerHas() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        resourcesPlayerHas = gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();
    }
}
