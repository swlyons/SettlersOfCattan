/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.receiver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.main.ServerException;
import server.command.Command;
import shared.data.User;

/**
 *
 * @author ddennis
 */
public class Commands {

    private Database db;
    private String plugin;

    public Commands(Database db, String plugin) {
        this.db = db;
        this.plugin = plugin;
    }

    /**
     * Adds the specified command to the Commands table
     *
     * @param command Command to be added
     * @throws server.main.ServerException
     */
    public void add(Command command, int gameId) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            ResultSet keyRS = null;
            try {
                String query = "INSERT into commands (gameid, command)"
                        + " values (?, ?)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, gameId);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(command);
                byte[] commandAsBytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(commandAsBytes);

                stmt.setBinaryStream(2, bais, commandAsBytes.length);
                if (stmt.executeUpdate() == 1) {
                    Statement keyStmt = db.getConnection().createStatement();
                    keyRS = keyStmt.executeQuery("SELECT last_INSERT_rowid()");
                    keyRS.next();
                } else {
                    throw new ServerException("Could not INSERT command");
                }
            } catch (SQLException e) {
                throw new ServerException("Could not INSERT command", e);
            } catch (IOException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                Database.safeClose(stmt);
                Database.safeClose(keyRS);
            }
        }
    }

    /**
     * Gets all the rows from the Commands table
     *
     * @return returns an ArrayList of all the commands
     */
    public ArrayList<Command> getAllCommands(int gameId) throws ServerException {
        ArrayList<Command> result = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT command "
                    + "FROM commands WHERE gameId = ?";
            stmt = db.getConnection().prepareStatement(query);
            stmt.setInt(1, gameId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                byte[] commandBytes = (byte[]) rs.getObject(1);
                ByteArrayInputStream bais = new ByteArrayInputStream(commandBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Command command = (Command) ois.readObject();
                result.add(command);
            }
        } catch (SQLException e) {
            throw new ServerException(e.getMessage(), e);
        } catch (IOException ex) {
            Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Database.safeClose(rs);
            Database.safeClose(stmt);
        }

        return result;
    }

    /**
     * Deletes all commands FROM the Commands table with the specified game id
     * used by Agent.java
     *
     *
     * @param gameId
     * @throws ServerException
     */
    public void delete(int gameId) throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            try {
                String query = "DELETE FROM commands "
                        + "WHERE gameid = ?";
                stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, gameId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new ServerException("Could not DELETE commands", e);
            } finally {
                Database.safeClose(stmt);
            }
        }

    }

    /**
     * Clears all commands FROM the Commands table used by ant clear-db and
     * Agent.java
     *
     *
     * @throws ServerException
     */
    public void clear() throws ServerException {
        if (plugin.equals("sql")) {
            PreparedStatement stmt = null;
            try {
                String query = "DROP TABLE IF EXISTS commands";
                stmt = db.getConnection().prepareStatement(query);
                stmt.execute();
            } catch (SQLException e) {
                throw new ServerException("Could not drop commands table", e);
            } finally {
                Database.safeClose(stmt);
            }
            try {
                String query = "CREATE TABLE commands(gameid INTEGER NOT NULL, command BLOB NOT NULL)";
                stmt = db.getConnection().prepareStatement(query);
                stmt.execute();
            } catch (SQLException e) {
                throw new ServerException("Could not create commands table", e);
            } finally {
                Database.safeClose(stmt);
            }
        }

    }
}
