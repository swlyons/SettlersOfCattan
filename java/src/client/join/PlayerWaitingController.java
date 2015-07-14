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
                try{
                    if(ClientCommunicatorFascadeSettlersOfCatan.getSingleton().addAIToGame(add)){
                        System.out.println("AddedAI");
                    }else{
                        System.out.println("FailedToAddAi");
                    }
                } catch (Exception e){
                        System.out.println("Couldn't AddAi");
                }
                
        }

}

