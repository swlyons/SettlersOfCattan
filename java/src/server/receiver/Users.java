/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.receiver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import server.main.ServerException;
import shared.data.User;

/**
 *
 * @author ddennis
 */
public class Users {

    private Database db;
    private String plugin;

    public Users(Database db, String plugin) {
        this.db = db;
        this.plugin = plugin;
    }

    /**
     * Adds the specified user to the Users table
     *
     * @param user User to be added
     * @throws server.main.ServerException
     */
    public void add(User user) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            ResultSet keyRS = null;
            try {
                String query = "INSERT into users (id, username, password)"
                        + " values (?, ?, ?)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, user.getId());
                stmt.setString(2, user.getUsername().toLowerCase());
                stmt.setString(3, user.getPassword());
                if (stmt.executeUpdate() == 1) {
                    Statement keyStmt = db.getConnection().createStatement();
                    keyRS = keyStmt.executeQuery("SELECT last_INSERT_rowid()");
                    keyRS.next();
                    int id = keyRS.getInt(1);
                    user.setId(id);
                } else {
                    throw new ServerException("Could not INSERT user");
                }
            } catch (SQLException e) {
                throw new ServerException("Could not INSERT user", e);
            } finally {
                Database.safeClose(stmt);
                Database.safeClose(keyRS);
            }
        }
    }

    /**
     * Clears all users FROM the Users table used by ant clear-db
     *
     *
     * @throws ServerException
     */
    public void clear() throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            try {
                String query = "DROP TABLE IF EXISTS users";
                stmt = db.getConnection().prepareStatement(query);
                stmt.execute();
            } catch (SQLException e) {
                throw new ServerException("Could not drop users table", e);
            } finally {
                Database.safeClose(stmt);
            }
            try {
                String query = "CREATE TABLE users(id INTEGER PRIMARY KEY, username TEXT NOT NULL, "
                        + "password TEXT NOT NULL)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.execute();
            } catch (SQLException e) {
                throw new ServerException("Could not create users table", e);
            } finally {
                Database.safeClose(stmt);
            }
        }

    }
}
