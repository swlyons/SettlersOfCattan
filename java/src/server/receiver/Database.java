package server.receiver;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

import server.main.ServerException;

/** Represents the structure of the SQL database */
public class Database {

	private static final String DATABASE_DIRECTORY = "database";
	private static final String DATABASE_FILE = "catan.db";
	private static final String DATABASE_URL = "jdbc:sqlite:"
			+ DATABASE_DIRECTORY + File.separator + DATABASE_FILE;

	public static void initialize() throws ServerException {
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
                    e.printStackTrace();
			//throw new ServerException("Could not load database driver", e);
		}
	}

	private Connection connection;

	/** Creates new instances of all the tables in the database */
	public Database() {
		connection = null;
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
			SQLiteConfig config = new SQLiteConfig();
			config.enforceForeignKeys(true);
			connection = DriverManager.getConnection(DATABASE_URL,
					config.toProperties());
			connection.setAutoCommit(false);
		} catch (SQLException e) {
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
