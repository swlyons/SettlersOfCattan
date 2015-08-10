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
import java.io.FilenameFilter;
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
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
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

    public void addUserToDatabase(int gameId, User user) {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                db.getUsers().add(user);
                db.endTransaction(true);
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            addtoSer(gameId, user, false);
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
            if (!AllOfOurInformation.getSingleton().getGames().get(gameId).getGame().getTurnTracker().getStatus().contains("Round")) {
                addtoSer(gameId, command, false);
            }
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
            File directory = new File(DATABASE_DIRECTORY);
            for (File f : directory.listFiles()) {
                if (f.getName().startsWith(gameId + "_")) {
                    f.delete();
                }
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
            addtoSer(gameId, gameManager, false);
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
            ArrayList<Object> objects = new ArrayList<>();
            ArrayList<GameManager> gameManagers = new ArrayList<>();
            getFromSer(new File(DATABASE_DIRECTORY), "GameManager", objects);

            for (Object gameManager : objects) {
                gameManagers.add((GameManager) gameManager);
            }
            return gameManagers;
        }

    }

    public ArrayList<User> getUsersFromDatabase() {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                ArrayList<User> usersDb = db.getUsers().getAllUsers();
                db.endTransaction(true);
                return usersDb;
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<>();
            }
        } else {
            ArrayList<Object> objects = new ArrayList<>();
            ArrayList<User> usersDb = new ArrayList<>();
            getFromSer(new File(DATABASE_DIRECTORY), "User", objects);

            for (Object user : objects) {
                usersDb.add((User) user);
            }
            return usersDb;
        }
    }

    public ArrayList<Command> getCommandsFromDatabase(int gameId) {
        if (plugin.equals("sql")) {
            try {
                db.startTransaction();
                ArrayList<Command> commands = db.getCommands().getAllCommands(gameId);
                db.getCommands().delete(gameId);
                db.endTransaction(true);
                return commands;
            } catch (ServerException ex) {
                Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ArrayList<Object> objects = new ArrayList<>();
            ArrayList<Command> commandsDb = new ArrayList<>();
            getFromSer(new File(DATABASE_DIRECTORY), gameId + "_Command", objects);

            for (Object command : objects) {
                commandsDb.add((Command) command);
            }
            return commandsDb;
        }
        return new ArrayList<>();
    }

    private void addtoSer(int gameId, Object obj, boolean append) {
        try {
            byte[] byteClass = new byte[0];
            child = new URLClassLoader(new URL[]{new File(PLUGINS_DIRECTORY + plugin + ".jar").toURI().toURL()}, ClassLoader.getSystemClassLoader());
            Class ioFileUtilsClass = Class.forName("org.apache.commons.io.FileUtils", true, child);
            Class<?>[] parameters = new Class[]{File.class, Object.class, Boolean.TYPE};
            for (Method method : ioFileUtilsClass.getMethods()) {
                if (method.getName().equals("writeByteArrayToFile") && (method.getParameterCount() == 3)) {
                    parameters = method.getParameterTypes();
                    break;
                }
            }
            Method writeByteArrayToFile = ioFileUtilsClass.getDeclaredMethod("writeByteArrayToFile", parameters);
            Object instance = ioFileUtilsClass.newInstance();
            String fileName = "";
            if (obj.getClass().getSimpleName().contains("Command")) {
                //since many commands con be attached to one game the file name needs to be unique
                fileName = gameId + "_" + obj.getClass().getSimpleName() + java.util.UUID.randomUUID();
            } else {
                fileName = obj.getClass().getSimpleName() + "0000000000" + gameId;
            }
            writeByteArrayToFile.invoke(instance, new File(DATABASE_DIRECTORY + File.separator + fileName + ".ser"), convertToBytes(obj), append);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | IOException | InvocationTargetException ex) {
            Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getFromSer(final File folder, String name, ArrayList<Object> sers) {
        for (File fileEntry : folder.listFiles()) {

            if (fileEntry.isDirectory()) {
                getFromSer(fileEntry, name, sers);
            } else {

                if (fileEntry.getName().contains(name)) {
                    Class ioFilesClass = null;
                    try {
                        child = new URLClassLoader(new URL[]{new File(PLUGINS_DIRECTORY + plugin + ".jar").toURI().toURL()}, ClassLoader.getSystemClassLoader());
                        ioFilesClass = Class.forName("org.apache.commons.io.IOUtils", true, child);
                    } catch (ClassNotFoundException | MalformedURLException ex) {
                        Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Method toByteArray = null;
                    try {
                        toByteArray = ioFilesClass.getDeclaredMethod("toByteArray", URI.class);
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
                        serData = (byte[]) toByteArray.invoke(instance, fileEntry.getAbsoluteFile().toURI());
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(AllOfOurInformation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        sers.add(convertFromBytes(serData));
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
