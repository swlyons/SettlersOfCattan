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
public class Games {

    private Database db;
    private String plugin;
    
    public Games(Database db, String plugin) {
        this.db = db;
        this.plugin = plugin;
    }
}
