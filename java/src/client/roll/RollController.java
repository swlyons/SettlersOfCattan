package client.roll;

import java.util.Random;

import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import client.managers.GameManager;
import java.util.Date;
import shared.model.RollNumber;

/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController {

    private IRollResultView resultView;
    private boolean clickedOk;

    /**
     * RollController constructor
     *
     * @param view Roll view
     * @param resultView Roll result view
     */
    public RollController(IRollView view, IRollResultView resultView) {

        super(view);

        setResultView(resultView);
        clickedOk = false;
    }

    public IRollResultView getResultView() {
        return resultView;
    }

    public void setResultView(IRollResultView resultView) {
        this.resultView = resultView;
    }

    public IRollView getRollView() {
        return (IRollView) getView();
    }

    @Override
    public void rollDice() {
        GameManager gm = ClientCommunicator.getSingleton().getGameManager();
        Random r = new Random();
        r.setSeed((new Date()).getTime());
        int twoD6 = r.nextInt(6) + r.nextInt(6) + 2;
        Integer playerId = ClientCommunicator.getSingleton().getPlayerId();
        Integer playerIndex = 4;
        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
            if (gm.getGame().getPlayers().get(i).getPlayerID() == playerId) {
                playerIndex = i;
                break;
            }
        }
        try {
            RollNumber number = new RollNumber();
            number.setNumber(twoD6);
            number.setPlayerIndex(playerIndex);
            number.setType("rollNumber");
            ClientFascade.getSingleton().rollNumber(number);
            gm.rollDice(twoD6);
            getResultView().setRollValue(twoD6);
            getResultView().showModal();
        } catch (Exception e) {
            System.out.println("Had a hard time rolling");
        }
    }

    public void setClickedOk(boolean ok) {
        clickedOk = ok;
    }

    public boolean getClickedOk() {
        return clickedOk;
    }

}
