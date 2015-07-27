/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import java.util.ArrayList;

/**
 * This will house the list of commands that have been executed
 *
 * @author ddennis
 */
public class Agent {

    private final ArrayList commandQueue;

    public Agent() {
        commandQueue = new ArrayList();
    }

    public boolean sendCommand(Command command) {
        commandQueue.add(command);
        System.out.println(commandQueue.size());
        return(command.execute());
    }

    public ArrayList getCommandQueue() {
        return commandQueue;
    }
}
