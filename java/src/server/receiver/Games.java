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
import shared.data.GameInfo;

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

    /**
     * Adds the specified game to the Games table
     *
     * @param user Game to be added
     * @throws server.main.ServerException
     */
    public void add(GameInfo game) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            ResultSet keyRS = null;
            try {
                String query = "INSERT into games (gameinfo)"
                        + " values (?)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, game.toString());
                if (stmt.executeUpdate() == 1) {
                    Statement keyStmt = db.getConnection().createStatement();
                    keyRS = keyStmt.executeQuery("SELECT last_INSERT_rowid()");
                    keyRS.next();
                    int id = keyRS.getInt(1);
                    game.setId(id);
                } else {
                    throw new ServerException("Could not INSERT game");
                }
            } catch (SQLException e) {
                throw new ServerException("Could not INSERT game", e);
            } finally {
                Database.safeClose(stmt);
                Database.safeClose(keyRS);
            }
        }
    }

    /**
     * Updates the specified game in the Games table
     *
     * @param game Game to be updated
     * @throws server.main.ServerException
     */
    public void update(GameInfo game) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            try {
                String query = "UPDATE games set gameinfo = ?, "
                        + "WHERE id = ?";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, game.toString());
                stmt.setInt(4, game.getId());
                if (stmt.executeUpdate() != 1) {
                    throw new ServerException("Could not UPDATE game");
                }
            } catch (SQLException e) {
                throw new ServerException("Could not UPDATE game", e);
            } finally {
                Database.safeClose(stmt);
            }
        }
    }

    /**
     * Clears all users FROM the Users table used by
     *
     *
     * @throws ServerException
     */
    public void clear() throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            try {
                String query = "DROP TABLE IF EXISTS games";
                stmt = db.getConnection().prepareStatement(query);
                stmt.execute();
            } catch (SQLException e) {
                throw new ServerException("Could not drop games table", e);
            } finally {
                Database.safeClose(stmt);
            }
            try {
                String query = "CREATE TABLE users(id INTEGER PRIMARY KEY, gameinfo BLOB NOT NULL)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.execute();
            } catch (SQLException e) {
                throw new ServerException("Could not create games table", e);
            } finally {
                Database.safeClose(stmt);
            }
        }

    }

}
