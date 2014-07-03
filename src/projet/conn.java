package projet;

import java.sql.*;

public class conn {

	private Connection conn;
	private Statement stmt;

	public conn() {
		boolean coReussite = this
				.connexionBd("localhost/trolls", "troll", "Troll");
	}

	public boolean connexionBd(String adresse, String user, String mdp) {
		boolean conOk;
		try {
			String url = "jdbc:postgresql://" + adresse;
			conn = DriverManager.getConnection(url, user, mdp);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			conOk = true;
		} catch (SQLException E) {
			 System.err.println("SQLException: " + E.getMessage());
			 System.err.println("SQLState:     " + E.getSQLState());
			conOk = false;
		}
		return (conOk);
	}

	public Connection Connection() {
		return (conn);
	}

	public Statement Statement() {
		return (stmt);
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}
}
