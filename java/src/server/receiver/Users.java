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
import java.util.ArrayList;
import server.main.ServerException;
import shared.data.User;

/**
 *
 * @author ddennis
 */
public class Users {

    private Database db;

    public Users(Database db) {
        this.db = db;
    }

    /**
     * Adds the specified user to the Users table
     *
     * @param user User to be added
     * @throws server.main.ServerException
     */
    public void add(User user) throws ServerException {
        PreparedStatement stmt = null;
        ResultSet keyRS = null;
        try {
            String query = "INSERT into users (username, password, gameid)"
                    + " values (?, ?, ? )";
            stmt = db.getConnection().prepareStatement(query);
            stmt.setString(1, user.getUsername().toLowerCase());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getGameId());
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

    /**
     * Gets all the rows from the Users table
     *
     * @return returns an ArrayList of all the users
     */
    public ArrayList<User> getAllUsers() throws ServerException {
        ArrayList<User> result = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT gameid, username, password "
                    + "FROM users";
            stmt = db.getConnection().prepareStatement(query);

            rs = stmt.executeQuery();
            while (rs.next()) {
                int gameId = rs.getInt(1);
                String username = rs.getString(2);
                String password = rs.getString(3);
                User user = new User(username, password);
                user.setGameId(gameId);
                result.add(user);
            }
        } catch (SQLException e) {
            throw new ServerException(e.getMessage(), e);
        } finally {
            Database.safeClose(rs);
            Database.safeClose(stmt);
        }

        return result;
    }

    /**
     * Clears all users FROM the Users table used by ant clear-db
     *
     *
     * @throws ServerException
     */
    public void clear() throws ServerException {
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
            String query = "CREATE TABLE users(gameid INTEGER NOT NULL, username TEXT NOT NULL, "
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
