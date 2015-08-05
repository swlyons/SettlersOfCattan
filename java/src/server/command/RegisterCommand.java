/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import shared.data.User;
import server.receiver.AllOfOurInformation;

/**
 *
 * @author ddennis
 */
public class RegisterCommand implements Command{
   private User user;
   public RegisterCommand(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
   
    @Override
    public boolean execute() {
        return(register());
    }   
    
    public boolean register() {
        int userNameLength = user.getUsername().length();
        int passwordLength = user.getPassword().length();
        if (!userAlreadyExists() && ((userNameLength > 2) && (userNameLength < 8)) && (passwordLength > 4)) {
            //check that the password contains the correct type of characters
            for (char c : user.getPassword().toCharArray()) {
                if (!Character.isLetterOrDigit(c)
                        && c != '_' && c != '-') {
                    return false;
                }
            }
            
            AllOfOurInformation.getSingleton().getUsers().add(user);
            return true;
        } else {
            return false;
        }
    }

    private boolean userAlreadyExists() {
        for (User existingUser : AllOfOurInformation.getSingleton().getUsers()) {
            if (user.getUsername().equals(existingUser.getUsername())) {
                return true;
            }
        }
        return false;
    }
    
}
