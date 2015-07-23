/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.receiver.UserReceiver;

/**
 *
 * @author ddennis
 */
public class RegisterCommand implements Command{
   private final UserReceiver user;

    public RegisterCommand(UserReceiver user) {
        this.user = user;
    }
    
    @Override
    public void execute() {
        user.register();
    }   
}
