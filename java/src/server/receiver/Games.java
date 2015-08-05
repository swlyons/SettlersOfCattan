/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.receiver;

import client.managers.GameManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.main.ServerException;

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
     * @param game GameManager to be added
     * @throws server.main.ServerException
     */
    public void add(GameManager gameManager, int gameId) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            ResultSet keyRS = null;
            try {
                String query = "INSERT into games (id, gameinfo)"
                        + " values (?,?)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, gameId);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(gameManager);
                byte[] gameManagerAsBytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(gameManagerAsBytes);

                stmt.setBinaryStream(2, bais, gameManagerAsBytes.length);
                if (stmt.executeUpdate() == 1) {
                    Statement keyStmt = db.getConnection().createStatement();
                    keyRS = keyStmt.executeQuery("SELECT last_INSERT_rowid()");
                    keyRS.next();
                } else {
                    throw new ServerException("Could not INSERT game");
                }
            } catch (SQLException e) {
                throw new ServerException("Could not INSERT game", e);
            } catch (IOException ex) {
                Logger.getLogger(Games.class.getName()).log(Level.SEVERE, null, ex);
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
    public ArrayList<GameManager> getAllGames() throws ServerException {
        ArrayList<GameManager> result = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT id, gameinfo "
                    + "FROM games";
            stmt = db.getConnection().prepareStatement(query);

            rs = stmt.executeQuery();
            while (rs.next()) {
                byte[] commandBytes = (byte[]) rs.getObject(2);
                ByteArrayInputStream bais = new ByteArrayInputStream(commandBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                GameManager gameManager = (GameManager) ois.readObject();
                result.add(gameManager);
            }
        } catch (SQLException e) {
            throw new ServerException(e.getMessage(), e);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Games.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Database.safeClose(rs);
            Database.safeClose(stmt);
        }

        return result;
    }

    /**
     * Updates the specified game in the Games table
     *
     * @param gameManager to be updated (cause we don't like to play games)
     * @param gameId
     * @throws server.main.ServerException
     */
    public void update(GameManager gameManager, int gameId) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            try {
                String query = "UPDATE games set gameinfo = ? "
                        + "WHERE id = ?";
                stmt = db.getConnection().prepareStatement(query);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(gameManager);
                byte[] gameManagerAsBytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(gameManagerAsBytes);

                stmt.setBinaryStream(1, bais, gameManagerAsBytes.length);
                stmt.setInt(2, gameId);
                if (stmt.executeUpdate() != 1) {
                    throw new ServerException("Could not UPDATE game");
                }
            } catch (SQLException e) {
                throw new ServerException("Could not UPDATE game", e);
            } catch (IOException ex) {
                Logger.getLogger(Games.class.getName()).log(Level.SEVERE, null, ex);
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
                String query = "CREATE TABLE games(id INTEGER PRIMARY KEY, gameinfo BLOB NOT NULL)";
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
