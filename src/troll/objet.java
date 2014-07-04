package troll;

import java.sql.*;

public class objet {

	// attributs
	private String idObjet;
	private String typeObjet;
	private int positionX;
	private int positionY;
	private int duree;
	// private float poids;

	conn connection = new conn();
	Connection conn = connection.Connection();
	Statement stmt = connection.Statement();
	CallableStatement proc;

	// constructeur
	public objet(String id, String type, String carac, int bonus,
			int tailleEchiquier) {
		idObjet = id;
		typeObjet = type;
		if (typeObjet.equals("potion")) {
			duree = (int) Math.floor(Math.random() * (3)) + 2;
		} else {
			duree = -1;
		}
		try {
			proc = conn.prepareCall("{call init_objet(?,?,?,?,?,?)}");
			proc.setString(1, id);
			proc.setString(2, type);
			positionX = (int) Math.floor(Math.random() * (tailleEchiquier)) + 1;
			positionY = (int) Math.floor(Math.random() * (tailleEchiquier)) + 1;
			proc.setInt(3, positionX);
			proc.setInt(4, positionY);
			proc.setString(5, carac);
			proc.setInt(6, bonus);
			proc.executeUpdate();
			proc.close();
			connection.close();
		} catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}

	public int getValeur() {
		int valeur = 0;
		try {
			proc = conn.prepareCall("{? = call get_valeur_bonus_objet(?)}");
			proc.registerOutParameter(1, java.sql.Types.INTEGER);
			proc.setString(2, idObjet);
			proc.executeUpdate();
			valeur = proc.getInt(1);
			proc.close();
		} catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
		return (valeur);
	}

	public String getType() {
		return (typeObjet);
	}

	public String idObjet() {
		return (idObjet);
	}

	public int positionX() {
		return (positionX);
	}

	public int positionY() {
		return (positionY);
	}

	public void decrementerTourPopo() {
		duree = duree - 1;
	}

	public void supprimerTourPopo() {
		duree = 0;
	}

	public int duree() {
		return (duree);
	}

	public String typeObjet() {
		return (typeObjet);
	}

}
