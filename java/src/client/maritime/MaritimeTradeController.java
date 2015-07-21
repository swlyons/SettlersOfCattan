package client.maritime;

import shared.definitions.*;
import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.proxy.MaritimeTrade;
import client.managers.GameManager;
import client.data.Port;
import client.data.ResourceList;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController {

	private IMaritimeTradeOverlay tradeOverlay;
	
        private List<ResourceType> resourceTypes;
        private ResourceType getResource;
        private ResourceType giveResource;
        private boolean hasTradeForThreePort;
        
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);

		setTradeOverlay(tradeOverlay);
	}
	
        public void initFromModel(){
            GameManager gm = ClientCommunicator.getSingleton().getGameManager();
            Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
            Integer playerIndex = 4;
            for(int i=0;i<gm.getGame().getPlayers().size();i++){
                if(gm.getGame().getPlayers().get(i).getPlayerID()==playerId){
                    playerIndex=i;
                    break;
                }
            }
            
            if(playerIndex==gm.getGame().getTurnTracker().getCurrentTurn()){
                List<Port> ports = gm.getLocationManager().getPortsPlayerHas(playerIndex); 
                resourceTypes = new ArrayList<ResourceType>();
                ResourceList allResourcesPlayerHas = gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();
                hasTradeForThreePort = false;
                for(Port port : ports){
                    if(port.getRatio()==2){
                        switch(port.getResource()){
                            case brick:
                                if(2<=allResourcesPlayerHas.getBrick()){
                                    resourceTypes.add(port.getResource());
                                }
                            case ore:
                                if(2<=allResourcesPlayerHas.getOre()){
                                    resourceTypes.add(port.getResource());
                                }
                            case sheep:
                                if(2<=allResourcesPlayerHas.getSheep()){
                                    resourceTypes.add(port.getResource());
                                }
                            case wheat:
                                if(2<=allResourcesPlayerHas.getWheat()){
                                    resourceTypes.add(port.getResource());
                                }
                            case wood:
                                if(2<=allResourcesPlayerHas.getWood()){
                                    resourceTypes.add(port.getResource());
                                }
                            default:
                                break;
                        }                        
                    }else{
                        hasTradeForThreePort = true;
                    }                    
                }
                
                int tradeValue = 4;
                if(hasTradeForThreePort){
                    tradeValue = 3;
                }
                ArrayList<ResourceType> available = new ArrayList<ResourceType>();
                for(ResourceType resourceType : ResourceType.values()){
                    if(resourceTypes.contains(resourceType)){
                        available.add(resourceType);
                        continue;
                    }
                    switch(resourceType){
                            case brick:
                                if(tradeValue<=allResourcesPlayerHas.getBrick()){
                                    available.add(resourceType);
                                }
                            break;
                            case ore:
                                if(tradeValue<=allResourcesPlayerHas.getOre()){
                                    available.add(resourceType);
                                }
                            break;
                            case sheep:
                                if(tradeValue<=allResourcesPlayerHas.getSheep()){
                                    available.add(resourceType);
                                }
                            break;
                            case wheat:
                                if(tradeValue<=allResourcesPlayerHas.getWheat()){
                                    available.add(resourceType);
                                }
                            break;
                            case wood:
                                if(tradeValue<=allResourcesPlayerHas.getWood()){
                                    available.add(resourceType);
                                }
                            default:
                                break;
                        }
                }
                
                ResourceType[] resourcesEnabled = new ResourceType[available.size()];
                available.toArray(resourcesEnabled);
                getTradeOverlay().showGiveOptions(resourcesEnabled);
                if(available.size()>0){
                    getTradeOverlay().setStateMessage("Choose what to give up");
                }else{
                    getTradeOverlay().setStateMessage("You don't have enough resources");
                }
            }else{
                getTradeOverlay().setStateMessage("not your turn");
                ResourceType[] empty = new ResourceType[0];
                getTradeOverlay().showGiveOptions(empty);
            }
            getTradeOverlay().setTradeEnabled(false);
        }
        
	public IMaritimeTradeView getTradeView() {
		
		return (IMaritimeTradeView)super.getView();
	}
	
	public IMaritimeTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IMaritimeTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	@Override
	public void startTrade() {
		getTradeOverlay().showModal();
		initFromModel();
	}

	@Override
	public void makeTrade() {
                try{
                    GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                    Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
                    Integer playerIndex = 4;
                    for(int i=0;i<gm.getGame().getPlayers().size();i++){
                        if(gm.getGame().getPlayers().get(i).getPlayerID()==playerId){
                            playerIndex=i;
                            break;
                        }
                    }
                    
                    int amount = 4;
                    
                    if(hasTradeForThreePort){
                        amount = 3;
                    }
                    
                    if(resourceTypes.contains(giveResource)){
                        amount = 2;
                    }
                    
                    ResourceList giveItems = new ResourceList();
                    ResourceList getItem = new ResourceList();

                    switch(giveResource){
                        case wood:
                            giveItems.setWood(amount);
                            break;
                        case brick:
                            giveItems.setBrick(amount);
                            break;
                        case sheep:
                            giveItems.setSheep(amount);
                            break;
                        case ore:
                            giveItems.setOre(amount);
                            break;
                        case wheat:
                            giveItems.setWheat(amount);
                            break;
                        default:
                            break;                            
                    }
                    
                    switch(getResource){
                        case wood:
                            getItem.setWood(1);
                            break;
                        case brick:
                            getItem.setBrick(1);
                            break;
                        case sheep:
                            getItem.setSheep(1);
                            break;
                        case ore:
                            getItem.setOre(1);
                            break;
                        case wheat:
                            getItem.setWheat(1);
                            break;
                        default:
                            break;                            
                    }
                    
                    
                    gm.getResourceManager().transferResourceCard(playerIndex, 4, giveItems);
                    gm.getResourceManager().transferResourceCard(4, playerIndex, getItem);
                    
                    MaritimeTrade trade = new MaritimeTrade();
                    trade.setOutputResource(getResource);
                    trade.setInputResource(giveResource);
                    trade.setRatio(amount);
                    trade.setPlayerIndex(playerIndex);
                    trade.setType("maritimeTrade");                    
                    ClientCommunicatorFascadeSettlersOfCatan.getSingleton().maritimeTrade(trade);

                }catch(Exception e){
                	e.printStackTrace();
                }finally{
                    getTradeOverlay().reset();
                    getTradeOverlay().closeModal();
                }
	}

	@Override
	public void cancelTrade() {
            
		getTradeOverlay().closeModal();
	}

	@Override
	public void setGetResource(ResourceType resource) {
            getResource = resource;            
            getTradeOverlay().selectGetOption(resource, 1);
            getTradeOverlay().setStateMessage("Trade!");
            getTradeOverlay().setTradeEnabled(true);
	}

	@Override
	public void setGiveResource(ResourceType resource) {
            giveResource = resource;
            
            int amount = 4;
            if(hasTradeForThreePort){
                amount = 3;
            }
            if(resourceTypes.contains(resource)){
                amount = 2;
            }
            getTradeOverlay().selectGiveOption(resource, amount);
            getTradeOverlay().setStateMessage("Choose what to get");

            GameManager gm = ClientCommunicator.getSingleton().getGameManager();
            ResourceList mainBank = gm.getResourceManager().getGameBanks().get(4).getResourcesCards();
            List<ResourceType> resourcesInBank = new ArrayList<ResourceType>();
            
            if(0<mainBank.getBrick()){
                resourcesInBank.add(ResourceType.brick);
            }
            if(0<mainBank.getWood()){
                resourcesInBank.add(ResourceType.wood);
            }
            if(0<mainBank.getSheep()){
                resourcesInBank.add(ResourceType.sheep);
            }
            if(0<mainBank.getOre()){
                resourcesInBank.add(ResourceType.ore);
            }
            if(0<mainBank.getWheat()){
                resourcesInBank.add(ResourceType.wheat);
            }
            
            ResourceType[] allResources = new ResourceType[resourcesInBank.size()];          
            resourcesInBank.toArray(allResources);
            getTradeOverlay().showGetOptions(allResources);
	}

	@Override
	public void unsetGetValue() {
            getResource = null;
            getTradeOverlay().hideGetOptions();
            GameManager gm = ClientCommunicator.getSingleton().getGameManager();
            ResourceList mainBank = gm.getResourceManager().getGameBanks().get(4).getResourcesCards();
            List<ResourceType> resourcesInBank = new ArrayList<ResourceType>();
            
            if(0<mainBank.getBrick()){
                resourcesInBank.add(ResourceType.brick);
            }
            if(0<mainBank.getWood()){
                resourcesInBank.add(ResourceType.wood);
            }
            if(0<mainBank.getSheep()){
                resourcesInBank.add(ResourceType.sheep);
            }
            if(0<mainBank.getOre()){
                resourcesInBank.add(ResourceType.ore);
            }
            if(0<mainBank.getWheat()){
                resourcesInBank.add(ResourceType.wheat);
            }
            
            ResourceType[] allResources = new ResourceType[resourcesInBank.size()];          
            resourcesInBank.toArray(allResources);
            getTradeOverlay().showGetOptions(allResources);
            getTradeOverlay().setTradeEnabled(false);
            getTradeOverlay().setStateMessage("Choose what to get");

	}

	@Override
	public void unsetGiveValue() {
            giveResource = null;
            getTradeOverlay().hideGiveOptions();
            initFromModel();
	}

}