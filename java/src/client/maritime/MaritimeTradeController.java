package client.maritime;

import shared.definitions.*;
import client.base.*;
import client.communication.ClientCommunicator;
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
                List<ResourceType> resourceTypes = new ArrayList<ResourceType>();
                ResourceList allResourcesPlayerHas = gm.getResourceManager().getGameBanks().get(playerIndex).getResourcesCards();
                boolean hasTradeForThreePort = false;
                for(Port port : ports){
                    if(port.getRatio()==2){
                        switch(port.getResource()){
                            case brick:
                            case ore:
                            case sheep:
                            case wheat:
                            case wood:
                            default:
                                break;
                        }
                        resourceTypes.add(port.getResource());
                    }else{
                        hasTradeForThreePort = true;
                    }
                }
                
                
                
                
            }else{
                getTradeOverlay().setStateMessage("not your turn");
                getTradeOverlay().setTradeEnabled(false);
                ResourceType[] empty = new ResourceType[0];
                getTradeOverlay().showGetOptions(empty);
            }
            
            
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
		initFromModel();
		getTradeOverlay().showModal();
	}

	@Override
	public void makeTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void setGetResource(ResourceType resource) {

	}

	@Override
	public void setGiveResource(ResourceType resource) {

	}

	@Override
	public void unsetGetValue() {

	}

	@Override
	public void unsetGiveValue() {

	}

}

