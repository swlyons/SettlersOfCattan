package server.receiver;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.main.ServerException;

/**
 * Represents the structure of the SQL database
 */
public class Database {

    private static final String DIST_DIRECTORY = System.getProperty("user.dir");
    private static final String PLUGINS_DIRECTORY = DIST_DIRECTORY + File.separator + ".." + File.separator + "plugins" + File.separator;
    private static final String DATABASE_DIRECTORY = "database";
    private static final String DATABASE_FILE = "catan.db";
    private static final String DATABASE_URL = "jdbc:sqlite:"
            + DATABASE_DIRECTORY + File.separator + DATABASE_FILE;
    private static URLClassLoader child;

    public static void initialize(String plugin) throws ServerException {
        try {
            try {
                child = new URLClassLoader(new URL[]{new File(PLUGINS_DIRECTORY + plugin + ".jar").toURI().toURL()}, Database.class.getClassLoader());
            } catch (MalformedURLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (plugin.equals("sql")) {
                //load the JDBC driver
                Class.forName("org.sqlite.JDBC", true, child);
            } else if (plugin.equals("blob")) {
                /* TODO: Change this to a text file jar */
                
            }
            
        } catch (ClassNotFoundException e) {
            throw new ServerException("Could not load database driver", e);
        }
    }

    private Connection connection;

    /**
     * Creates new instances of all the tables in the database
     */
    public Database() {
        connection = null;
        /*Create new Instances of User and Games classes here */

    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Starts a transaction (enforces foreign keys with each transaction)
     *
     * @throws ServerException
     */
    public void startTransaction() throws ServerException {
        try {
            assert (connection == null);
            Class sqlLiteConfigClass = Class.forName("org.sqlite.SQLiteConfig", true, child);
            Method enforceForeignKeysMethod = sqlLiteConfigClass.getDeclaredMethod("enforceForeignKeys");
            Method toPropertiesMethod = sqlLiteConfigClass.getDeclaredMethod("toProperties");
            Object instance = sqlLiteConfigClass.newInstance();

            enforceForeignKeysMethod.invoke(instance, true);
            connection = DriverManager.getConnection(DATABASE_URL,
                    ((Properties) toPropertiesMethod.invoke(instance)));
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new ServerException(
                    "Could not connect to database. Make sure " + DATABASE_FILE
                    + " is available in ./" + DATABASE_DIRECTORY, e);
        }
    }

    public boolean inTransaction() {
        return (connection != null);
    }

    public void endTransaction(boolean commit) {
        if (connection != null) {
            try {
                if (commit) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            } catch (SQLException e) {
                System.out.println("Could not end transaction");
                e.printStackTrace();
            } finally {
                safeClose(connection);
                connection = null;
            }
        }
    }

    public static void safeClose(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ...
            }
        }
    }

    public static void safeClose(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // ...
            }
        }
    }

    public static void safeClose(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // ...
            }
        }
    }

    public static void safeClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ...
            }
        }
    }

}
