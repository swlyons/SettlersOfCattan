package client.domestic;

import shared.definitions.*;
import client.base.*;
import client.communication.ClientCommunicator;
import client.managers.GameManager;
import client.data.PlayerInfo;
import client.misc.*;

/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController {

	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;

        private boolean addedPlayers;
        
	/**
	 * DomesticTradeController constructor
	 * 
	 * @param tradeView Domestic trade view (i.e., view that contains the "Domestic Trade" button)
	 * @param tradeOverlay Domestic trade overlay (i.e., view that lets the user propose a domestic trade)
	 * @param waitOverlay Wait overlay used to notify the user they are waiting for another player to accept a trade
	 * @param acceptOverlay Accept trade overlay which lets the user accept or reject a proposed trade
	 */
	public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
									IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

		super(tradeView);
		addedPlayers=false;
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
	}
	
	public IDomesticTradeView getTradeView() {
		
		return (IDomesticTradeView)super.getView();
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

        
        public void initFromModel(){
            GameManager gm = ClientCommunicator.getSingleton().getGameManager();
            Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
            Integer playerIndex = 4;
            for(int i=0;i<gm.getGame().getPlayers().size();i++){
                if(gm.getGame().getPlayers().get(i).getId()==playerId){
                    playerIndex=i;
                    break;
                }
            }
            
            if(playerIndex==gm.getGame().getTurnTracker().getCurrentTurn()){

            }else{
                getTradeOverlay().setStateMessage("not your turn");
            }
            
            getTradeOverlay().setTradeEnabled(false);
        }
                
	@Override
	public void startTrade() {
                if(!addedPlayers){
                    PlayerInfo[] players = new PlayerInfo[4];
                    GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                    for(int i=0;i<4;i++){
                        PlayerInfo player = new PlayerInfo();
                        player.setName(gm.getGame().getPlayers().get(i).getName());
                        player.setColor(gm.getGame().getPlayers().get(i).getColor());
                        players[i] = player;                        
                    }
                    getTradeOverlay().setPlayers(players);
                    addedPlayers = true;
                }
		getTradeOverlay().showModal();
                initFromModel();
        }

	@Override
	public void decreaseResourceAmount(ResourceType resource) {

	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {

	}

	@Override
	public void sendTradeOffer() {

		getTradeOverlay().closeModal();
//		getWaitOverlay().showModal();
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {

	}

	@Override
	public void setResourceToReceive(ResourceType resource) {

	}

	@Override
	public void setResourceToSend(ResourceType resource) {

	}

	@Override
	public void unsetResource(ResourceType resource) {

	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {

		getAcceptOverlay().closeModal();
	}

}

