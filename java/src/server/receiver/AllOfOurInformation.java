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
import java.util.logging.Level;
import java.util.logging.Logger;
import server.command.Command;
import server.main.ServerException;
import shared.data.GameInfo;

/**
 *
 * @author Samuel
 */
public class AllOfOurInformation {

    public static AllOfOurInformation allInfo = null;
    private static List<User> users;
    private static List<GameManager> games;
    private Database db;
    private int currentGameId;
    
    public static AllOfOurInformation getSingleton() {
        if (allInfo == null) {
            allInfo = new AllOfOurInformation();
            users = new ArrayList<User>();
            games = new ArrayList<GameManager>();
        }
        return allInfo;
    }

    public int getCurrentGameId() {
        return currentGameId;
    }

    public void setCurrentGameId(int currentGameId) {
        this.currentGameId = currentGameId;
    }
    
    public void setDatabase(Database db){
        this.db = db;
    }
    public Database getDatabase(){
        return db;
    }
    public List<GameManager> getGames() {
        return games;
    }

    public void addUserToDatabase(User user) {
        try {
            db.startTransaction();
            db.getUsers().add(user);
            db.endTransaction(true);
        } catch (ServerException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCommandToDatabase(Command command) {
        try {
            db.startTransaction();
            db.getCommands().add(command);
            db.endTransaction(true);
        } catch (ServerException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void clearCommandsFromDatabase(int gameId){
        try {
            db.startTransaction();
            db.getCommands().delete(gameId);
            db.endTransaction(true);
        } catch (ServerException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void addGameToDatabase(GameInfo game) {
        try {
            db.startTransaction();
            db.getGames().add(game);
            db.endTransaction(true);
        } catch (ServerException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updateGameInDatabase(GameInfo game) {
        try {
            db.startTransaction();
            db.getGames().update(game);
            db.endTransaction(true);
        } catch (ServerException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<User> getUsers() {
        return users;
    }

    private void setUsers(List<User> users) {
        this.users = users;
    }
}
