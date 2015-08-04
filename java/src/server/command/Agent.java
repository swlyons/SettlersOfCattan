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
        commandQueue.add(command);
        return(command.execute());
    }

    public ArrayList getCommandQueue() {
        return commandQueue;
    }
}
