/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.receiver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
     * @param game Game to be added
     * @throws server.main.ServerException
     */
    public void add(GameInfo game) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            ResultSet keyRS = null;
            try {
                String query = "INSERT into games (id, title, gameinfo)"
                        + " values (?,?,?)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, game.getId());
                stmt.setString(2, game.getTitle());
                stmt.setString(3, game.toString());
                if (stmt.executeUpdate() == 1) {
                    Statement keyStmt = db.getConnection().createStatement();
                    keyRS = keyStmt.executeQuery("SELECT last_INSERT_rowid()");
                    keyRS.next();
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
     * Gets all the rows from the Games table
     *
     * @return returns an ArrayList of all the games
     */
    public ArrayList<GameInfo> getAllGames() throws ServerException {
        ArrayList<GameInfo> result = new ArrayList<>();
        Gson model = new GsonBuilder().create();
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT id, title, gameinfo "
                    + "FROM games";
            stmt = db.getConnection().prepareStatement(query);

            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String gameInfo = rs.getString(3);
                GameInfo game = model.fromJson(gameInfo, GameInfo.class);
                game.setId(id);
                game.setTitle(title);
                result.add(game);
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
     * Updates the specified game in the Games table
     *
     * @param game Game to be updated
     * @throws server.main.ServerException
     */
    public void update(GameInfo game) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            try {
                String query = "UPDATE games set gameinfo = ? "
                        + "WHERE id = ?";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, game.toString());
                stmt.setInt(2, game.getId());
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
     * Clears all games FROM the Games table used by ant clear-db
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
                String query = "CREATE TABLE games(id INTEGER PRIMARY KEY, title TEXT NOT NULL, gameinfo BLOB NOT NULL)";
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
