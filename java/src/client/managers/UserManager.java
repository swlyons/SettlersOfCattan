package client.managers;

import client.proxy.CredentialRequest;
import client.proxy.IServer;
import client.proxy.MockServer;
import client.proxy.ServerProxy;

public class UserManager {

	private IServer server = ServerProxy.getSingleton();
	
    public UserManager() {

    }
    
    public UserManager(boolean testing) {
    	if (testing) {
    		server = new MockServer();
    	}
    }

    /**
     *
     * @param username
     * @param password
     * @pre
     * @post returns if the user was successfully created or not
     */
    public boolean createUser(String username, String password, String passwordValidate) {
    	System.out.println("start "+username+" : "+password+" , "+passwordValidate);
        boolean created = true;

        //may want to throw different exceptions instead of returning booleans
        if (!validateUsername(username) || !validatePassword(password)) {
        	System.out.println("failed validation");
            created = false;
        } else if (!validatePasswordsMatch(password, passwordValidate)) {
        	System.out.println("mismatched validation");
            created = false;
        } else {
            CredentialRequest request = new CredentialRequest(username, password);
            String cookie = server.Register(request);
            if (cookie == null) {
            	System.out.println("bad response");
                created = false;
            } else if (cookie.length() == 0 || cookie.contains("Failed")) {
            	System.out.println("bad response");
                created = false;
            }
            //TODO: once we have a better concept of how a cookie works and what is valid, we should improve this logic.
        }
        System.out.println("end");
        return created;
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
        boolean authenticated = true;
        if (!validateUsername(username) || !validatePassword(password)) {
            authenticated = false;
        } else {
            CredentialRequest request = new CredentialRequest(username, password);
            String cookie = server.login(request);
            if (cookie == null) {
                authenticated = false;
            } else if (cookie.length() == 0 || cookie.contains("Failed")) {
                authenticated = false;
            }
            //TODO: once we have a better concept of how a cookie works and what is valid, we should improve this logic.
        }
        return authenticated;
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
    private boolean validatePasswordsMatch(String password, String passwordValidate) {
        boolean isValid = true;
        if (password == null || passwordValidate == null) {
            isValid = false;
        } else if (!password.equals(passwordValidate)) {
            isValid = false;
        }
        return isValid;
    }
}
