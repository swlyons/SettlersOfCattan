package client.managers;

import client.ClientException;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.data.User;
import client.proxy.IServer;
import client.proxy.MockServer;
import client.proxy.ServerProxy;

public class UserManager {

	private ClientCommunicatorFascadeSettlersOfCatan server = ClientCommunicatorFascadeSettlersOfCatan.getSingleton();

    /**
     *
     * @param username
     * @param password
     * @pre
     * @post returns if the user was successfully created or not
     */
    public boolean createUser(String username, String password, String passwordValidate) {
        //may want to throw different exceptions instead of returning booleans
        if (!validateUsername(username) || !validatePassword(password)) {
            return false;
        } else if (!validatePasswordsMatch(password, passwordValidate)) {
            return false;
        } else {
			try {
	            User credentials = new User(username, password);
	            boolean success = server.register(credentials);
	            if (success) {
	            	success = server.login(credentials);
	            }
	            return success;
			} catch (ClientException e) {
				return false;
			}
        }
    }

    /**
     *
     * @param username
     * @param password
     * @pre
     * @post returns if the username and password combination exists in the
     * database
     */
    public boolean authenticateUser(String username, String password) {
        if (!validateUsername(username) || !validatePassword(password)) {
            return false;
        } else {
        	try {
	            User credentials = new User(username, password);
	            boolean success = server.login(credentials);
	            return success;
			} catch (ClientException e) {
				return false;
			}
        }
    }

    /**
     *
     * @param username
     * @pre
     * @post returns if username is valid according to the game specifications:
     *
     * Registering a new user should reject the registration if the username is
     * fewer than 3 or greater than seven characters
     */
    private boolean validateUsername(String username) {
        boolean isValid = true;
        if (username == null) {
            isValid = false;
        } else if (username.length() < 3) {
            isValid = false;
        } else if (username.length() > 7) {
            isValid = false;
        }
        return isValid;
    }

    /**
     *
     * @param password
     * @pre
     * @post returns if password is valid according to the game specifications:
     *
     * if the password is less than 5 characters long or is not made of allowed
     * characters (alphanumerics, underscores, hyphens, or if the password
     * verification entry doesn’t match the original.
     */
    private boolean validatePassword(String password) {
        boolean isValid = true;
        if (password == null) {
            isValid = false;
        } else if (password.length() < 5) {
            isValid = false;
        } else if (password.contains("[!a-zA-Z_-]")) {
            isValid = false;
        }
        return isValid;
    }

    /**
     *
     * @param password
     * @param passwordValidate
     * @pre
     * @post returns if password is exactly the same as passwordValidate
     */
    public boolean validatePasswordsMatch(String password, String passwordValidate) {
        boolean isValid = true;
        if (password == null || passwordValidate == null) {
            isValid = false;
        } else if (!password.equals(passwordValidate)) {
            isValid = false;
        }
        return isValid;
    }
}
