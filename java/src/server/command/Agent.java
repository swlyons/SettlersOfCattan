/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import server.receiver.AllOfOurInformation;
import server.receiver.Database;
import shared.data.GameInfo;
import shared.data.User;

/**
 * This will house the list of commands that have been executed
 *
 * @author ddennis
 */
public class Agent {

    private final ArrayList commandQueue;
    private int deltas = 5;

    public Agent() {
        commandQueue = new ArrayList();
    }

    public int getDeltas() {
        return deltas;
    }

    public void setDeltas(int deltas) {
        this.deltas = deltas;
    }

    public boolean sendCommand(Command command) {
        Map<Integer, ArrayList> commandMap = new HashMap<>();
        
        boolean success = command.execute();
        if (success) {
            int currentGameId = AllOfOurInformation.getSingleton().getCurrentGameId();
            //update the game info every delta commands
            if (commandMap.get(currentGameId).size() == (deltas - 1)) {
                /* add logic to update the game state blob the present game*/
                System.out.println("Setting new CheckPoint");
                
                GameInfo presentGame = AllOfOurInformation.getSingleton().getGames().get(currentGameId).getGame();
                AllOfOurInformation.getSingleton().updateGameInDatabase(presentGame);
                
                //clear the command database
                AllOfOurInformation.getSingleton().clearCommandsFromDatabase(currentGameId);
                commandQueue.clear();
                commandMap.put(currentGameId, commandQueue);
            }

            if (command instanceof RegisterCommand) {
                //skip the register commands (for queue)   
                //add the user to the database
                 User user = ((RegisterCommand)command).getUser();
                 AllOfOurInformation.getSingleton().addUserToDatabase(user);
            } 
            else if(command instanceof CreateGameCommand){
                //skip Create Game Commands
                
            }else {
                //save command to the Database
                AllOfOurInformation.getSingleton().addCommandToDatabase(command);
                commandQueue.add(command);
                commandMap.put(currentGameId, commandQueue);
            }
            System.out.println("Command Queue Size: " + commandMap.get(currentGameId).size());
        }
        return success;
    }

    public ArrayList getCommandQueue() {
        return commandQueue;
    }
}
