package client.join;

import client.base.*;
import shared.model.AddAIRequest;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import java.util.ArrayList;

/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

    private IAction playerAction;
    private final int MAX_PLAYERS = 4;

    public PlayerWaitingController(IPlayerWaitingView view) {

        super(view);
    }

    @Override
    public IPlayerWaitingView getView() {

        return (IPlayerWaitingView) super.getView();
    }

    @Override
    public void start() {
        getView().showModal();
        try {
            ArrayList<String> aiTypes = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listAITypesInGame();
            String[] aiTypes2 = new String[aiTypes.size()];
            aiTypes.toArray(aiTypes2);
            getView().setAIChoices(aiTypes2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAI() {
        AddAIRequest add = new AddAIRequest(getView().getSelectedAI());
        try {
            if (ClientCommunicatorFascadeSettlersOfCatan.getSingleton().addAIToGame(add)) {
                System.out.println("AddedAI");
                /**
                 * DO NOT REMOVE Added this to fix issue of multiple overlays
                 * added when AI player is added *
                 */
                getView().closeModal();
                /**/
            } else {
                System.out.println("FailedToAddAi");
            }
        } catch (Exception e) {
            System.out.println("Couldn't AddAi");
        }

    }

    @Override
    public void ready() {
        //start the game when there are four players
        if (getView().getPlayers().length == MAX_PLAYERS) {
            playerAction.execute();
        }
    }

    public IAction getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(IAction playerAction) {
        this.playerAction = playerAction;
    }

}
