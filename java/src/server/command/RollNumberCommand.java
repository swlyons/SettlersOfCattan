/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;
import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.model.RollNumber;
/**
 *
 * @author Samuel
 */
public class RollNumberCommand implements Command{
    RollNumber rollNumber;
    
    public RollNumberCommand(RollNumber rollNumber){
        this.rollNumber = rollNumber;
    }
    
    @Override
    public boolean execute() {
        AllOfOurInformation.getSingleton().getGames().get(rollNumber.getGameId()).rollDice(rollNumber.getNumber());
        AllOfOurInformation.getSingleton().getGames().get(rollNumber.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(rollNumber.getGameId()).getGame().getVersion()+1);
        return true;
    }
}
