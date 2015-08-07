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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.command.Agent;
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
    private Agent agent;
    private String plugin;

    private static final String JAVA_DIRECTORY = System.getProperty("user.dir").replace("dist", "");
    private static final String DATABASE_DIRECTORY = JAVA_DIRECTORY + "database";
    private static final String PLUGINS_DIRECTORY = JAVA_DIRECTORY + "plugins" + File.separator;
    private URLClassLoader child;

    public static AllOfOurInformation getSingleton() {
        if (allInfo == null) {
            allInfo = new AllOfOurInformation();
            users = new ArrayList<>();
            games = new ArrayList<>();

        }
        return allInfo;
    }

    public AllOfOurInformation() {
        this.child = null;
    }

    public int getCurrentGameId() {
        return currentGameId;
    }

    public void setCurrentGameId(int currentGameId) {
        this.currentGameId = currentGameId;
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    public Database getDatabase() {
        return db;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public List<GameManager> getGames() {
        return games;
    }

    public void addUserToDatabase(User user) {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                db.getUsers().add(user);
                db.endTransaction(true);
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //users are absolutely useless in our model
        }
    }

    public void addCommandToDatabase(Command command, int gameId) {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                db.getCommands().add(command, gameId);
                db.endTransaction(true);
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            addtoSer(gameId, command, true);
        }
    }

    public void clearCommandsFromDatabase(int gameId) {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                db.getCommands().delete(gameId);
                db.endTransaction(true);
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                PrintWriter writer = new PrintWriter(new File(DATABASE_DIRECTORY + File.separator + gameId + ".ser"));
                writer.print("");
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addGameToDatabase(GameManager gameManager, int gameId) {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                db.getGames().add(gameManager, gameId);
                db.endTransaction(true);
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            addtoSer(gameId, gameManager, true);
        }
    }

    public void updateGameInDatabase(GameManager gameManager, int id) {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                db.getGames().update(gameManager, id);
                db.endTransaction(true);
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            addtoSer(id, gameManager, false);
        }
    }

    public ArrayList<GameManager> getGamesFromDatabase() {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                ArrayList<GameManager> gamesManagers = db.getGames().getAllGames();
                db.endTransaction(true);
                return gamesManagers;
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            ArrayList<GameManager> gameManagers = new ArrayList<>();
            getFilesForFolder(new File(DATABASE_DIRECTORY), "GameManager", (ArrayList<GameManager>) gameManagers);
            return gameManagers;
        }

    }

    public ArrayList<User> getUsersFromDatabase() {
        try {
            db.startTransaction();
            ArrayList<User> usersDb = db.getUsers().getAllUsers();
            db.endTransaction(true);
            return usersDb;
        } catch (ServerException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Command> getCommandsFromDatabase(int gameId) {
        try {
            db.startTransaction();
            ArrayList<Command> commands = db.getCommands().getAllCommands(gameId);
            db.getCommands().delete(gameId);
            db.endTransaction(true);
            return commands;
        } catch (ServerException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void addtoSer(int gameId, Object obj, boolean append) {
        try {
            byte [] byteClass = new byte[0];
            child = new URLClassLoader(new URL[]{new File(PLUGINS_DIRECTORY + plugin + ".jar").toURI().toURL()}, ClassLoader.getSystemClassLoader());
            Class ioFileUtilsClass = Class.forName("org.apache.commons.io.FileUtils", true, child);
            Class<?>[] parameters = new Class[]{File.class, byteClass.getClass(), Boolean.TYPE};
            Method writeByteArrayToFile = ioFileUtilsClass.getDeclaredMethod("writeByteArrayToFile", parameters);
            Object instance = ioFileUtilsClass.newInstance();
            System.out.println("All of Our Information Class Name: " + obj.getClass().getName());
            writeByteArrayToFile.invoke(instance, new File(DATABASE_DIRECTORY + File.separator + gameId + obj.getClass().getName() + ".ser"), convertToBytes(obj), append);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | IOException | InvocationTargetException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getFilesForFolder(final File folder, String name, ArrayList<GameManager> sers) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getFilesForFolder(fileEntry, name, sers);
            } else {
                if (fileEntry.getName().contains(name)) {
                    Class ioFilesClass = null;
                    try {
                        child = new URLClassLoader(new URL[]{new File(PLUGINS_DIRECTORY + plugin + ".jar").toURI().toURL()}, ClassLoader.getSystemClassLoader());
                        ioFilesClass = Class.forName("org.apache.commons.io.Files");
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Method toByteArray = null;
                    try {
                        toByteArray = ioFilesClass.getDeclaredMethod("toByteArray", File.class);
                    } catch (NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Object instance = null;
                    try {
                        instance = ioFilesClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException ex) {
                        Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    byte[] serData = null;
                    try {
                        serData = (byte[]) toByteArray.invoke(instance, new File(DATABASE_DIRECTORY + File.separator + fileEntry.getName() + ".ser"));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        sers.add((GameManager) convertFromBytes(serData));
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    private void setUsers(List<User> users) {
        this.users = users;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

}
