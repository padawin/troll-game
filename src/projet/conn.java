package projet;

import java.sql.*;

public class conn {

	private Connection conn;
	private Statement stmt;

	public conn() {
		boolean coReussite = this
				.connexionBd("dbhost/dbname", "user", "password");
		while (!coReussite) {
			System.out.println("Base introuvable !");
			System.out
					.println("Veuillez indiquer les parametres de la BD à utiliser");
			System.out.print("Adresse : jdbc:postgresql://");
			String adresse = IO.lireChaine();
			System.out.print("Utilsateur : ");
			String user = IO.lireChaine();
			System.out.print("Mot de passe : ");
			String mdp = IO.lireChaine();
			coReussite = this.connexionBd(adresse, user, mdp);
		}
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
