/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.receiver;

/**
 *
 * @author ddennis
 */
public class ClearDatabases {
    public ClearDatabases(){
        
    }
    public static void main(String[] args) throws Exception {

        Database db = new Database("sql");

        Database.initialize();
        db.startTransaction();

        // clear out the database
        db.getGames().clear();
        db.getUsers().clear();
        
        db.endTransaction(true);
       

        //no need to start or end transactions for a blob
        db = new Database("blob");

        // clear out the database
        db.getGames().clear();
        db.getUsers().clear();
        
        
    }

}
