/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.receiver;

import java.util.ArrayList;
import java.util.List;
import shared.data.User;
import client.managers.GameManager;

/**
 *
 * @author Samuel
 */
public class AllOfOurInformation {
    
    public static AllOfOurInformation allInfo = null;
    private static List<User> users;
    private static List<GameManager> games;
    
    public static AllOfOurInformation getSingleton() {
        if (allInfo == null) {
            allInfo = new AllOfOurInformation();
            users = new ArrayList<User>();
            games = new ArrayList<GameManager>();
        }
        return allInfo;
    }
    
    public List<GameManager> getGames(){
        return games;
    }
    
    public List<User> getUsers(){
        return users;
    }
    private void setUsers(List<User> users){
        this.users=users;
    }
}
