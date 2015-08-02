package client.discard;

import shared.definitions.*;
import client.base.*;
import client.misc.*;
import shared.data.ResourceList;
import shared.model.DiscardCards;
import client.communication.ClientFascade;
import client.communication.ClientCommunicator;
import client.managers.GameManager;

/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController {

    private IWaitView waitView;
    private int amountToDiscard;

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

        ResourceList resources2 = gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();
        
        boolean moreThanZero;
        for (ResourceType resource : ResourceType.values()) {
            moreThanZero = false;
            switch (resource) {
                case brick:
                    if (0 < resources2.getBrick()) {
                        moreThanZero = true;
                    }
                    break;
                case ore:
                    if (0 < resources2.getOre()) {
                        moreThanZero = true;
                    }
                    break;
                case sheep:
                    if (0 < resources2.getSheep()) {
                        moreThanZero = true;
                    }
                    break;
                case wheat:
                    if (0 < resources2.getWheat()) {
                        moreThanZero = true;
                    }
                    break;
                case wood:
                    if (0 < resources2.getWood()) {
                        moreThanZero = true;
                    }
                    break;
                default:
                    break;
            }
            getDiscardView().setResourceAmountChangeEnabled(resource, moreThanZero, false);
            getDiscardView().setResourceDiscardAmount(resource, 0);
        }

        amountToDiscard = resources2.getTotalResources() / 2;
        getDiscardView().setStateMessage("0/" + amountToDiscard);
        getDiscardView().setDiscardButtonEnabled(false);
    }

    /**
     * DiscardController constructor
     *
     * @param view View displayed to let the user select cards to discard
     * @param waitView View displayed to notify the user that they are waiting
     * for other players to discard
     */
    public DiscardController(IDiscardView view, IWaitView waitView) {

        super(view);
        this.waitView = waitView;

        resources = new ResourceList();
    }

    public IDiscardView getDiscardView() {

        return (IDiscardView) super.getView();
    }

    public IWaitView getWaitView() {
        return waitView;
    }

    private ResourceList resources;

    @Override
    public void increaseAmount(ResourceType resource) {

        resources.add(resource, 1);
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        ResourceList resources2 = gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();

        boolean increaseAble;
        increaseAble = false;
        switch (resource) {
            case brick:
                if (0 < resources2.getBrick() - resources.getBrick()) {
                    increaseAble = true;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getBrick());
                break;
            case ore:
                if (0 < resources2.getOre() - resources.getOre()) {
                    increaseAble = true;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getOre());
                break;
            case sheep:
                if (0 < resources2.getSheep() - resources.getSheep()) {
                    increaseAble = true;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getSheep());
                break;
            case wheat:
                if (0 < resources2.getWheat() - resources.getWheat()) {
                    increaseAble = true;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getWheat());
                break;
            case wood:
                if (0 < resources2.getWood() - resources.getWood()) {
                    increaseAble = true;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getWood());
                break;
            default:
                break;
        }
        getDiscardView().setResourceAmountChangeEnabled(resource, increaseAble, true);
        calculateIfAbleToDiscard();

    }

    @Override
    public void decreaseAmount(ResourceType resource) {
        boolean decreaseAble = true;
        switch (resource) {
            case brick:
                resources.removeBrick(1);
                if (resources.getBrick() == 0) {
                    decreaseAble = false;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getBrick());
                break;
            case ore:
                resources.removeOre(1);
                if (resources.getOre() == 0) {
                    decreaseAble = false;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getOre());
                break;
            case sheep:
                resources.removeSheep(1);
                if (resources.getSheep() == 0) {
                    decreaseAble = false;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getSheep());
                break;
            case wheat:
                resources.removeWheat(1);
                if (resources.getWheat() == 0) {
                    decreaseAble = false;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getWheat());
                break;
            case wood:
                resources.removeWood(1);
                if (resources.getWood() == 0) {
                    decreaseAble = false;
                }
                getDiscardView().setResourceDiscardAmount(resource, resources.getWood());
                break;
            default:
                break;
        }

        getDiscardView().setResourceAmountChangeEnabled(resource, true, decreaseAble);
        calculateIfAbleToDiscard();
    }

    @Override
    public void discard() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        gm.getResourceManager().playerDiscardsHalfCards(playerIndex, resources);
        DiscardCards cards = new DiscardCards();
        cards.setPlayerIndex(playerIndex);
        cards.setType("discardCards");
        cards.setDiscardedCards(resources);
        try {
            ClientFascade.getSingleton().discardCards(cards);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resources = new ResourceList();
            getDiscardView().closeModal();
        }
    }

    private void calculateIfAbleToDiscard() {
        if (resources.getTotalResources() == amountToDiscard) {
            getDiscardView().setDiscardButtonEnabled(true);
            getDiscardView().setStateMessage("Discard " + resources.getTotalResources() + " Resources");
        } else {
            getDiscardView().setDiscardButtonEnabled(false);
            getDiscardView().setStateMessage(resources.getTotalResources() + "/" + amountToDiscard);
        }
    }
}
