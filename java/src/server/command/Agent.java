/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import client.managers.GameManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import server.receiver.AllOfOurInformation;
import shared.data.User;

/**
 * This will house the list of commands that have been executed
 *
 * @author ddennis
 */
public class Agent {

    private int deltas = 5;
    private Map<Integer, ArrayList<Command>> commandQueue;

    public Agent() {
        commandQueue = new HashMap<>();
        AllOfOurInformation.getSingleton().setAgent(this);
    }

    public int getDeltas() {
        return deltas;
    }

    public void setDeltas(int deltas) {
        this.deltas = deltas;
    }

    public boolean sendCommand(Command command) {

        boolean success = command.execute();
        System.out.println("Agent.java Successful: " + success);
        if (success) {
            int currentGameId = AllOfOurInformation.getSingleton().getCurrentGameId();
            //update the game info every delta commands
            String status = "";
            if (!AllOfOurInformation.getSingleton().getGames().isEmpty()) {
                status = AllOfOurInformation.getSingleton().getGames().get(currentGameId).getGame().getTurnTracker().getStatus();
            }
            if (!commandQueue.isEmpty()  && commandQueue.get(currentGameId) != null) {
                //update for the first and second rounds as well
                if ((commandQueue.get(currentGameId).size() == deltas) || status.contains("Round")) {
                    /* add logic to update the game state blob the present game*/
                    GameManager presentGameManager = AllOfOurInformation.getSingleton().getGames().get(currentGameId);
                    AllOfOurInformation.getSingleton().updateGameInDatabase(presentGameManager, currentGameId);
                    //clear the command database/blob
                    AllOfOurInformation.getSingleton().clearCommandsFromDatabase(currentGameId);
                    commandQueue.get(currentGameId).clear();
                }
            }

            if (command instanceof RegisterCommand) {
                //skip the register commands (for queue)   
                //add the user to the database/blob
                User user = ((RegisterCommand) command).getUser();
                AllOfOurInformation.getSingleton().addUserToDatabase(currentGameId, user);
            } else if (command instanceof CreateGameCommand) {
                //skip Create Game Commands(for queue)
                //clear the command quueue for each new game
            } else {
                //save command to the Database/blob
                AllOfOurInformation.getSingleton().addCommandToDatabase(command, currentGameId);
                if (commandQueue.isEmpty()) {
                    commandQueue.put(currentGameId, new ArrayList<>());
                }
                commandQueue.get(currentGameId).add(command);
            }
        }
        return success;
    }

    public Map<Integer, ArrayList<Command>> getCommandQueue() {
        return commandQueue;
    }

}
