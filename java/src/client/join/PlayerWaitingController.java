package client.join;

import client.base.*;
import client.proxy.AddAIRequest;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.managers.GameManager;
import java.util.ArrayList;

/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	public PlayerWaitingController(IPlayerWaitingView view) {

		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
            try{
                ArrayList<String> aiTypes = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listAITypesInGame();
                String[] aiTypes2 = new String[aiTypes.size()];
                aiTypes.toArray(aiTypes2);
                getView().setAIChoices(aiTypes2);
            }catch(Exception e){
            } finally{
                getView().showModal();
            }
        }

	@Override
	public void addAI() {
                AddAIRequest add = new AddAIRequest(getView().getSelectedAI());
                System.out.println(add);
                try{
                    String responseBody = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().addAIToGame(add);
                    GameManager gm = ClientCommunicator.getSingleton().getGameManager();
                    gm.initializeGame(responseBody);
                    if(gm.getGame().getPlayers().size()==4){
                        getView().closeModal();
                    }
                } catch (Exception e){
                
                }
                
        }

}

