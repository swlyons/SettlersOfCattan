package client.discard;

import shared.definitions.*;
import client.base.*;
import client.misc.*;
import client.data.ResourceList;
import client.proxy.DiscardCards;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.communication.ClientCommunicator;
import client.managers.GameManager;

/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController {

	private IWaitView waitView;
	
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		this.waitView = waitView;
                
		resources = new ResourceList();
                GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
                Integer playerIndex = 4;
                for(int i=0;i<gm.getGame().getPlayers().size();i++){
                    if(gm.getGame().getPlayers().get(i).getPlayerID()==playerId){
                        playerIndex=i;
                        break;
                    }
                }
                
                ResourceList resources2 = gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();
                    
                boolean moreThanZero;
                for(ResourceType resource : ResourceType.values()){
                    moreThanZero = false;
                    switch(resource){
                    case ResourceType.brick:
                        if(0<resources2.getBrick()){
                            moreThanZero = true;
                        }
                        break;
                    case ResourceType.ore:
                        if(0<resources2.getOre()){
                            moreThanZero = true;
                        }
                        break;
                    case ResourceType.sheep:
                        if(0<resources2.getSheep()){
                            moreThanZero = true;
                        }
                        break;
                    case ResourceType.wheat:
                        if(0<resources2.getWheat()){
                            moreThanZero = true;
                        }
                        break;
                    case ResourceType.wood:
                        if(0<resources2.getWood()){
                            moreThanZero = true;
                        }
                        break;    
                        default:
                            break;
                    }
                    getDiscardView().setResourceAmountChangeEnabled(resource, moreThanZero, false);
                }                
	}

	public IDiscardView getDiscardView() {
                
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {
		return waitView;
	}
        
        private ResourceList resources;

	@Override
	public void increaseAmount(ResourceType resource) {
            resources.add(resource, 1);
            ResourceList resources2 = gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();
                    
            boolean moreThanZero;
            moreThanZero = false;
            switch(resource){
            case ResourceType.brick:
                if(0<resources2.getBrick()-resources.getBrick()){
                    moreThanZero = true;
                }
                break;
            case ResourceType.ore:
                if(0<resources2.getOre()-resources.getOre()){
                    moreThanZero = true;
                }
                break;
            case ResourceType.sheep:
                if(0<resources2.getSheep()-resources.getSheep()){
                    moreThanZero = true;
                }
                break;
            case ResourceType.wheat:
                if(0<resources2.getWheat()-resources.getWheat()){
                    moreThanZero = true;
                }
                break;
            case ResourceType.wood:
                if(0<resources2.getWood()-resources.getWood()){
                    moreThanZero = true;
                }
                break;    
                default:
                    break;
            }
            getDiscardView().setResourceAmountChangeEnabled(resource, moreThanZero, false);
                
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
            boolean decreaseAble = true;
            switch(resource){
                case ResourceType.brick:
                    resources.removeBrick(1);
                    if(resources.getBrick()==0){
                        decreaseAble = false;
                    }
                    break;
                case ResourceType.ore:
                    resources.removeOre(1);
                    if(resources.getBrick()==0){
                        decreaseAble = false;
                    }
                    break;
                case ResourceType.sheep:
                    resources.removeSheep(1);
                    if(resources.getBrick()==0){
                        decreaseAble = false;
                    }
                    break;
                case ResourceType.wheat:
                    resources.removeWheat(1);
                    if(resources.getBrick()==0){
                        decreaseAble = false;
                    }
                    break;
                case ResourceType.wood:
                    resources.removeWood(1);
                    if(resources.getBrick()==0){
                        decreaseAble = false;
                    }
                    break;    
                    default:
                        break;
                }
                
                    getDiscardView().setResourceAmountChangeEnabled(resource, true, decreaseAble);
	}

	@Override
	public void discard() {
		GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
                Integer playerIndex = 4;
                for(int i=0;i<gm.getGame().getPlayers().size();i++){
                    if(gm.getGame().getPlayers().get(i).getPlayerID()==playerId){
                        playerIndex=i;
                        break;
                    }
                }                
                gm.getResourceManager().playerDiscardsHalfCards(playerIndex, resources);
                DiscardCards cards = new DiscardCards();
                cards.setDiscardedCards(resources);
                try{
                    ClientCommunicatorFascadeSettlersOfCatan.getSingleton().discardCards(cards);
                }catch(Exception e){}
                finally{
                    resources = new ResourceList();
                    getDiscardView().closeModal();
            }
        }

}

