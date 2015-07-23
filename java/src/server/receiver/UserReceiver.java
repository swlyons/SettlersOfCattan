/*
 * This will house receiver for user/* (see swagger).
 */
package server.receiver;

import java.util.ArrayList;
import shared.data.User;

/**
 *
 * @author ddennis
 */
public class UserReceiver {

    private boolean success;
    private User user;
    private ArrayList<User> users;

    public UserReceiver() {
        users = new ArrayList<>();
    }

    public void login() {
        setSuccess(loginSuccessful());
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
            users.add(user);
            setSuccess(true);
        } else {
            setSuccess(false);
        }
    }

    private boolean userAlreadyExists() {
        for (User existingUser : users) {
            if (user.getUsername().equals(existingUser.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private boolean loginSuccessful() {

        for (User existingUser : users) {
            if (user.getUsername().equals(existingUser.getUsername()) && user.getPassword().equals(existingUser.getPassword())) {
                return true;
            }
        }
        return false;
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
