/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.command.Command;
import shared.model.FinishMove;
import server.receiver.AllOfOurInformation;
import shared.data.TurnTracker;

/**
 *
 * @author Samuel
 */
public class FinishTurnCommand implements Command {

    FinishMove finishMove;

    public FinishTurnCommand(FinishMove finishMove) {
        this.finishMove = finishMove;
    }

    @Override
    public boolean execute() {
        try {
            int currentTurn = AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().getCurrentTurn();
            String status = AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().getStatus();
            currentTurn++;
            if (status.equals("Playing")) {
                if (4 <= currentTurn) {
                    currentTurn = 0;
                }
                AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setStatus("Rolling");
            } else {
                if (status.equals("FirstRound")) {
                    if (4 <= currentTurn) {
                        currentTurn = 3;
                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setStatus("SecondRound");
                    }
                } else {
                    if (status.equals("SecondRound")) {
                        currentTurn -= 2;
                        if (currentTurn < 0) {
                            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setStatus("Rolling");
                        }
                    } else {
                        return false;
                    }
                }
            }

            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).endTurn();
            while (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getPlayers().get(1).getId() < -1) {
                if (status.equals("SecondRound")) {
                    currentTurn--;
                } else {
                    currentTurn++;
                    if (currentTurn == 4) {
                        currentTurn = 0;
                    }
                }
            }
            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setCurrentTurn(currentTurn);
            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
