package client.managers;

import client.data.User;

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
    public boolean createUser(String username, String password, String passwordValidate){
        boolean created = false;
       
        //may want to throw different exceptions instead of returning booleans
        if(validateUsername(username)){
            created = true;
        }
        if(validatePassword(password)){
            created = true;
        }
        if(validatePasswordsMatch(password, passwordValidate)){
            created = true;
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
    public boolean authenticateUser(String username, String password){
        boolean authenticated = false;
        //if username is null return false
        
        //use the DAO to query for the credentials
        
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
    public boolean validateUsername(String username){
        boolean isValid = false;
        //if username is null return false
        
        //get list of users from the game
        
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
    public boolean validatePassword(String password){
        boolean isValid = false;
        
        
        return isValid;
    }
    /**
     * 
     * @param password
     * @param passwordValidate
     * @pre
     * @post returns if password is exactly the same as passwordValidate
     */
    public boolean validatePasswordsMatch(String password, String passwordValidate){
       boolean isValid = false;


       return isValid;
    }
}
