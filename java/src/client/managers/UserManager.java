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
<<<<<<< HEAD
    public boolean createUser(String username, String password, String passwordValidate){
        boolean created = false;
=======
    private boolean createUser(String username, String password, String passwordValidate){
        boolean created = true;
>>>>>>> 7071a9788250be0deba93f8906181d9d67b2a583
       
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
<<<<<<< HEAD
    public boolean authenticateUser(String username, String password){
        boolean authenticated = false;
        //if username is null return false
        
        //use the DAO to query for the credentials
=======
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
>>>>>>> 7071a9788250be0deba93f8906181d9d67b2a583
        
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
<<<<<<< HEAD
    public boolean validateUsername(String username){
        boolean isValid = false;
        //if username is null return false
        
        //get list of users from the game
        
=======
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
>>>>>>> 7071a9788250be0deba93f8906181d9d67b2a583
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
<<<<<<< HEAD
    public boolean validatePassword(String password){
        boolean isValid = false;
        
        
=======
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
>>>>>>> 7071a9788250be0deba93f8906181d9d67b2a583
        return isValid;
    }
    /**
     * 
     * @param password
     * @param passwordValidate
     * @pre
     * @post returns if password is exactly the same as passwordValidate
     */
<<<<<<< HEAD
    public boolean validatePasswordsMatch(String password, String passwordValidate){
       boolean isValid = false;


       return isValid;
=======
    private boolean validatePasswordsMatch(String password, String passwordValidate){
        boolean isValid = true;
        if (password == null || passwordValidate == null) {
        	isValid = false;
        }
        else if (!password.equals(passwordValidate)) {
    		isValid = false;
    	}
    	return isValid;
>>>>>>> 7071a9788250be0deba93f8906181d9d67b2a583
    }
}
