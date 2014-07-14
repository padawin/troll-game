package troll.model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Connection {

	protected java.sql.Connection _connection;
	protected Statement _stmt;

	public Connection() {
		Properties prop = new Properties();
		InputStream input = null;
		String dbHost, dbName, dbUser, dbPasswd;

		try {

			input = new FileInputStream("../config/db.ini");

			// load a properties file
			prop.load(input);

			dbHost = prop.getProperty("dbhost");
			dbName = prop.getProperty("dbname");
			dbUser = prop.getProperty("dbuser");
			dbPasswd = prop.getProperty("dbpasswd");

			boolean coReussite = connexionBd(dbHost, dbName, dbUser, dbPasswd);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean connexionBd(String host, String name, String user, String pwd) {
		boolean conOk;
		try {
			String url = "jdbc:postgresql://" + host + "/" + name;
			this._connection = DriverManager.getConnection(url, user, pwd);
			this._stmt = this._connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			conOk = true;
		} catch (SQLException E) {
			 System.err.println("SQLException: " + E.getMessage());
			 System.err.println("SQLState:     " + E.getSQLState());
			conOk = false;
		}
		return (conOk);
	}

	public java.sql.Connection getConnection() {
		return this._connection;
	}

	public java.sql.Statement getStatement() {
		return this._stmt;
	}

	public void close() {
		try {
			this._connection.close();
		} catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}
}
