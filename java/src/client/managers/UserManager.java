package client.managers;

import client.data.User;
import client.proxy.CredentialRequest;
import client.proxy.ServerProxy;

public class UserManager {
    
    
    public UserManager(){
        
    }
    
    /**
     * 
     * @param username
     * @param password
     * @pre
     * @post returns if the user was successfully created or not
     */
    private boolean createUser(String username, String password, String passwordValidate){
        boolean created = true;
       
        //may want to throw different exceptions instead of returning booleans
        if(!validateUsername(username) && !validatePassword(password)){
            created = false;
        }
        else if(!validatePasswordsMatch(password, passwordValidate)){
            created = false;
        }
        else {
        	CredentialRequest request = new CredentialRequest(username, password);
	        String cookie = ServerProxy.getSingleton().Register(request);
	        if (cookie == null) {
	        	created = false;
	        }
	        else if (cookie.length() == 0) {
	        	created = false;
	        }
	        //TODO: once we have a better concept of how a cookie works and what is valid, we should improve this logic.
        }
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
    private boolean authenticateUser(String username, String password){
        boolean authenticated = true;
        if (!validateUsername(username) || !validatePassword(password)) {
        	authenticated = false;
        }
        else {
        	CredentialRequest request = new CredentialRequest(username, password);
	        String cookie = ServerProxy.getSingleton().login(request);
	        if (cookie == null) {
	        	authenticated = false;
	        }
	        else if (cookie.length() == 0) {
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
    private boolean validateUsername(String username){
    	boolean isValid = true;
        if (username == null) {
        	isValid = false;
        }
        else if (username.length() < 3) {
        	isValid = false;
        }
        else if (username.length() > 7) {
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
    private boolean validatePassword(String password){
        boolean isValid = true;
        if (password == null) {
        	isValid = false;
        }
        else if (password.length() < 5) {
        	isValid = false;
        }
        else if (password.matches("(.*)[!a-zA-Z_-](.*)")) {
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
    private boolean validatePasswordsMatch(String password, String passwordValidate){
        boolean isValid = true;
        if (password == null || passwordValidate == null) {
        	isValid = false;
        }
        else if (!password.equals(passwordValidate)) {
    		isValid = false;
    	}
    	return isValid;
    }
}
