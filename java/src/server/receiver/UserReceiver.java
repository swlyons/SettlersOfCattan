/*
 * This will house receiver for user/* (see swagger).
 */
package server.receiver;

import java.util.ArrayList;
import shared.data.User;
import server.receiver.AllOfOurInformation;
/**
 *
 * @author ddennis
 */
public class UserReceiver {

    private boolean success;
    private User user;
    
    public UserReceiver() {        
    }

    public void login() {
//        setSuccess(loginSuccessful());
    }

    public void register() {
        int userNameLength = user.getUsername().length();
        int passwordLength = user.getPassword().length();
        if (!userAlreadyExists() && ((userNameLength > 2) && (userNameLength < 8)) && (passwordLength > 4)) {
            //check that the password contains the correct type of characters
            for (char c : user.getPassword().toCharArray()) {
                if (!Character.isLetterOrDigit(c)
                        && c != '_' && c != '-') {
                    setSuccess(false);
                    return;
                }
            }
            AllOfOurInformation.getSingleton().getUsers().add(user);
            setSuccess(true);
        } else {
            setSuccess(false);
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

    private int  loginSuccessful() {
        for (int i=0;i<AllOfOurInformation.getSingleton().getUsers().size();i++) {
            if (user.getUsername().equals(AllOfOurInformation.getSingleton().getUsers().get(i).getUsername()) && user.getPassword().equals(AllOfOurInformation.getSingleton().getUsers().get(i).getPassword())) {
                return i;
            }
        }
        return -1;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    

}
