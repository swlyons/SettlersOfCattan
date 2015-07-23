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
public class LoginCommand implements Command{
    private UserReceiver user;

    public LoginCommand(UserReceiver user) {
        this.user = user;
    }
    
    @Override
    public void execute() {
        user.login();
    }  
}
