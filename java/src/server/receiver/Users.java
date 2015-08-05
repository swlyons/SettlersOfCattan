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
                String query = "INSERT into users (username, password, gameid)"
                        + " values (?, ?, ?)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, user.getUsername().toLowerCase());
                stmt.setString(2, user.getPassword());
                /*TODO Add a game ID for a user */
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
    }

    /**
     * Updates the specified user in the Users table
     *
     * @param user User to be updated
     * @throws server.main.ServerException
     */
    public void update(User user) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            try {
                String query = "UPDATE users set username = ?, password = ?, gameid = ?, "
                        + "WHERE id = ?";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, user.getUsername().toLowerCase());
                stmt.setString(2, user.getPassword());
                stmt.setInt(3, user.getGameId());
                stmt.setInt(4, user.getId());
                if (stmt.executeUpdate() != 1) {
                    throw new ServerException("Could not UPDATE user");
                }
            } catch (SQLException e) {
                throw new ServerException("Could not UPDATE user", e);
            } finally {
                Database.safeClose(stmt);
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
                        + "password TEXT NOT NULL, gameid INTEGER NOT NULL)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.execute();
            } catch (SQLException e) {
                throw new ServerException("Could not create users table", e);
            } finally {
                Database.safeClose(stmt);
            }
        }

    }

    /**
     * Validates a the specified user/password combination
     *
     * @param name username for the user that needs to be validated
     * @param pass password for the user that needs to be validated
     * @return User Object if found, null if not
     * @throws ServerException
     */
    public User validateUser(String name, String pass) throws ServerException {
        User result = null;
        if (plugin.equals("sql")) {

            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String query = "SELECT id, username, password, gameid "
                        + "FROM users "
                        + "WHERE username = ? and password = ? limit 1";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, name.toLowerCase());
                stmt.setString(2, pass);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String username = rs.getString(2);
                    String password = rs.getString(3);
                    int gameId = rs.getInt(4);

                    result = new User(username, password);
                    result.setId(id);
                    result.setGameId(gameId);

                }
            } catch (SQLException e) {
                throw new ServerException(e.getMessage(), e);
            } finally {
                Database.safeClose(rs);
                Database.safeClose(stmt);
            }
        } else {

        }
        return result;

    }
}
