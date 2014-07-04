package troll;

import java.sql.*;
import java.util.*;

import troll.util.IO;

public class troll {

	//attributs
	private int idTroll;
	private String nomTroll;
	private int att;
	private int deg;
	private int esq;
	private int vie;
	private int paRestants;
	private int positionX;
	private int positionY;
	private String valeur;

	Menu menu;

	conn connection = new conn();
	Connection conn = connection.Connection();
	Statement stmt = connection.Statement();
	CallableStatement proc;

	Hashtable<String, objet> tabPopoEnCours = new Hashtable<String, objet>();

	//methodes


	//constructeur
	public troll(int numtroll, int tailleEchiquier,Menu menu) {

		try {
			this.menu = menu;
			paRestants = 6;
			System.out.println();
			idTroll = numtroll;
			System.out.print("Nom du troll " +numtroll +": ");
			nomTroll = IO.readString();
			int points=40;
			//System.out.println("Vous allez maitenant definir les carac de votre troll (attaque, degats, esquive, vie)");
			boolean ok=false;
			while (ok==false) {
				System.out.println();
				System.out.print("Nombre de D3 d'attaque (Il reste "+points+" a repartir dans 4 carac) : ");
				att = IO.readInt();
				if (att>points-3) {
					System.out.println("Impossible de mettre autant de points dans 1 seule carac !!");
				}
				else {
					points=points-att;
					ok=true;
				}
			}
			ok=false;
			while (ok==false) {
				System.out.println();
				System.out.print("Nombre de D3 de degats (Il reste "+points+" a repartir dans 3 carac) : ");
				deg = IO.readInt();
				if (deg>points-2) {
					System.out.println("Impossible de mettre autant de points dans 1 seule carac !!");
				}
				else {
					points=points-deg;
					ok=true;
				}
			}
			ok=false;
			while (ok==false) {
				System.out.println();
				System.out.print("Nombre de D3 d'esquive (Il reste "+points+" a repartir dans 2 carac) : ");
				esq = IO.readInt();
				if (esq>points-1) {
					System.out.println("Impossible de mettre autant de points dans 1 seule carac !!");
				}
				else {
					points=points-esq;
					ok=true;
				}
			}
			System.out.println();
			System.out.println("Points de vie : "+points+"*"+"10 = "+points*10);
			vie = points*10;
			positionX = (int)Math.floor(Math.random() * (tailleEchiquier))+1;
			positionY = (int)Math.floor(Math.random() * (tailleEchiquier))+1;
			System.out.println("Il apparaitra en X="+positionX+" Y="+positionY);
			stmt.executeUpdate("delete from troll where idTroll="+idTroll);
			proc = conn.prepareCall("{call init_troll(?,?,?,?,?,?,?,?)}");
				proc.setInt(1,numtroll);
				proc.setString(2,nomTroll);
				proc.setInt(3,att);
				proc.setInt(4,deg);
				proc.setInt(5,esq);
				proc.setInt(6,vie);
				proc.setInt(7,positionX);
				proc.setInt(8,positionY);
				proc.executeUpdate();
				proc.close();
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
		valeur = ""+nomTroll.charAt(0);
	}

	public troll(int id, String nom, int att, int deg, int esq, int tailleEchiquier) {
		idTroll = id;
		nomTroll = nom;
		this.att = att;
		this.deg = deg;
		this.esq = esq;
		this.vie = 40-(att+deg+esq);
		paRestants = 6;
		positionX = (int)Math.floor(Math.random() * (tailleEchiquier))+1;
		positionY = (int)Math.floor(Math.random() * (tailleEchiquier))+1;
		valeur = ""+nomTroll.charAt(0);
	}

	public void deplacer(int tailleEchiquier) {

		boolean coordOk = false;
		System.out.println("Position actuelle : X="+positionX+" | Y="+positionY);
		System.out.println("Nouvelle position :");
		while (coordOk == false) {
			if (positionX-1<1) {
				System.out.print("X [1 - " + (positionX+1) +" ] = ");
			}
			else if (positionX+1>tailleEchiquier) {
				System.out.print("X [" + (positionX-1) + " - " + (tailleEchiquier) +" ] = ");
			}
			else {
				System.out.print("X [" + (positionX-1) + " - " + (positionX+1) +" ] = ");
			}
			int nouvPositionX = IO.readInt();
			if (nouvPositionX<1) {
				System.out.println("Impossible : coordonnee X < 1 !");
			}
			else if (nouvPositionX>tailleEchiquier) {
				System.out.println("Impossible : coordonnee X > "+tailleEchiquier+" !");
			}
			else if (nouvPositionX == positionX+1 || nouvPositionX == positionX-1 || nouvPositionX == positionX) {
				coordOk = true;
				positionX = nouvPositionX;
			}
			else {
				System.out.println("Impossible : Le déplacement est limité à 1 case");
			}
		}
		coordOk = false;
		while (coordOk == false) {
			if (positionY-1<1) {
				System.out.print("Y [1 - " + (positionY+1) +" ] = ");
			}
			else if (positionY+1>tailleEchiquier) {
				System.out.print("Y [" + (positionY-1) + " - " + (tailleEchiquier) +" ] = ");
			}
			else {
				System.out.print("Y [" + (positionY-1) + " - " + (positionY+1) +" ] = ");
			}
			int nouvPositionY = IO.readInt();
			if (nouvPositionY<1) {
				System.out.println("Impossible : coordonnee Y < 1 !");
			}
			else if (nouvPositionY>tailleEchiquier) {
				System.out.println("Impossible : coordonnee Y > "+tailleEchiquier+" !");
			}
			else if (nouvPositionY == positionY+1 || nouvPositionY == positionY-1 || nouvPositionY == positionY) {
				coordOk = true;
				positionY = nouvPositionY;
			}
			else {
				System.out.println("Impossible : Le déplacement est limité à 1 case");
			}
		}
		try {
			ResultSet rset = stmt.executeQuery("select count (*) from objet where positionX = "+this.positionX+" and positionY="+this.positionY);
			rset.first();
			int nbObjet=rset.getInt(1);
			if (nbObjet>0) {
				System.out.println();
				System.out.println("Vous marchez sur quelque chose...");
			}
			proc = conn.prepareCall("{? = call deplacer_troll(?,?,?)}");
			proc.registerOutParameter(1, Types.VARCHAR);
			proc.setInt(2,idTroll);
			proc.setInt(3,positionX);
			proc.setInt(4,positionY);
			proc.executeUpdate();
			String mess = proc.getString(1);
			proc.close();

			proc = conn.prepareCall("{? = call pa_restants(?)}");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setInt(2,idTroll);
			proc.executeUpdate();
			paRestants = proc.getInt(1);
			proc.close();
			if (mess != null) {
				System.out.println(mess);
			}
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}

	public boolean attaquer() {
		boolean reussite;
		boolean mort = false;
		int idTrollAdverse;
		int positionAdverseX=0;
		int positionAdverseY=0;
		if (idTroll == 1) {
			idTrollAdverse = 2;
		}
		else {
			idTrollAdverse = 1;
		}
		try {
			ResultSet rset = stmt.executeQuery("select positionX, positionY from troll where idTroll="+idTrollAdverse);
			rset.first();
				positionAdverseX = rset.getInt("positionX");
				positionAdverseY = rset.getInt("positionY");
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
		if (positionAdverseX==this.positionX && positionAdverseY==this.positionY) {
			reussite = loi_reussite();
			if (reussite) {
				int scoreAttaque = score_Des(att);
				System.out.println("Jet d'attaque : " + scoreAttaque);
				try {

					proc = conn.prepareCall("{? = call nb_des_esq(?)}");
					proc.registerOutParameter(1, Types.INTEGER);
					proc.setInt(2,idTrollAdverse);
					proc.executeUpdate();
					int esqAdverse = proc.getInt(1);
					proc.close();
					int scoreEsquiveAdversaire = score_Des(esqAdverse);
					System.out.println("Jet d'esquive de l'adversaire : " + scoreEsquiveAdversaire);
					if (scoreEsquiveAdversaire < scoreAttaque) {
						System.out.println("Esquive de l'adversaire ratée... PAF !!");
						int scoreDegats = score_Des(deg);
						System.out.print("L'adversaire perd " + scoreDegats + " pts de vie, il lui en reste donc ");

						proc = conn.prepareCall("{? = call decrementer_vie(?,?)}");
						proc.registerOutParameter(1, Types.BOOLEAN);
						proc.setInt(2,idTrollAdverse);
						proc.setInt(3,scoreDegats);
						proc.executeUpdate();
						mort = proc.getBoolean(1);
						proc.close();

						ResultSet rset = stmt.executeQuery("select vie from troll where idTroll ="+ idTrollAdverse);
						rset.first();
						System.out.println(rset.getInt("vie"));

						proc = conn.prepareCall("{call init_pa(?,?)}");
						proc.setInt(1,idTroll);
						proc.setInt(2,paRestants-4);
						proc.executeUpdate();
						proc.close();

						paRestants = paRestants-4;


					}
					else if (scoreEsquiveAdversaire == scoreAttaque) {
						System.out.println("Esquive de l'adversaire un peu tardive... PAF !!");
						int scoreDegats = score_Des(deg);
						System.out.println("L'adversaire perd " + (scoreDegats/2) + " pts de vie, il lui en reste donc ");
						proc = conn.prepareCall("{? = call decrementer_vie(?,?)}");
						proc.registerOutParameter(1, Types.BOOLEAN);
						proc.setInt(2,idTrollAdverse);
						proc.setInt(3,scoreDegats);
						proc.executeUpdate();
						mort = proc.getBoolean(1);
						proc.close();

						ResultSet rset = stmt.executeQuery("select vie from troll where idTroll ="+ idTrollAdverse);
						rset.first();
						System.out.println(rset.getInt("vie"));

						proc = conn.prepareCall("{call init_pa(?,?)}");
						proc.setInt(1,idTroll);
						proc.setInt(2,paRestants-4);
						proc.executeUpdate();
						proc.close();

						paRestants = paRestants-4;
					}
					else {
						System.out.println("Magnifique esquive de l'adversaire qui ne subira donc aucun dommage");
					}
				}
				catch (SQLException E) {
					System.err.println("SQLException: " + E.getMessage());
					System.err.println("SQLState:     " + E.getSQLState());
				}
			}
		}
		else {
			System.out.println("Le troll adverse n'est pas sur votre case !!");
		}
		return (mort);
	}

	public boolean loi_reussite() {
		int proba =  (int)Math.floor(Math.random() * (100))+1;
		if (proba<=80) {
			System.out.println("Action REUSSIE ! (" + proba + " sur 80%)");
			return (true);
		}
		else {
			System.out.println("Action RATEE ! (" + proba + " sur 80%)");
			return (false);
		}
	}

	public int score_Des(int nbDes) {

		int score = 0;
		for(int i=0 ; i<nbDes ; i++) {
			score = score + (int)Math.floor(Math.random() * (3))+1;
		}
		return (score);
	}

	public int paRestant() {

		return (paRestants);
	}

	public void setPa (int pa) {
		paRestants = pa;
	}

	public String nomTroll () {
		return (nomTroll);
	}

	public void ramasserObjet () {
		boolean vide=true;
		try {
			ResultSet rset = stmt.executeQuery("select idObjet from objet where positionX="+positionX+ " and positionY="+positionY);
			Hashtable<String, String> tabObj = new Hashtable<String, String>();
			while (rset.next()) {
				vide=false;
				String idObj = rset.getString("idObjet");
				System.out.println(idObj);
				tabObj.put(idObj, idObj);
			}
			rset.close();
			if (!vide) {
				System.out.println("Quel objet voulez vous ramasser ? : ");
				String obj = IO.readString();
				if (tabObj.containsKey(obj)) {
					proc = conn.prepareCall("{call rammasser(?,?)}");
					proc.setInt(1,idTroll);
					proc.setString(2,obj);
					proc.executeUpdate();
					proc.close();
					rset = stmt.executeQuery("select typeObjet from objet where idObjet='"+obj+"'");
					rset.first();
					String type = rset.getString("typeObjet");
					if (type.equals("potion")) {
						menu.supprimerPopo(obj);
					}
					else {
						menu.supprimerObjet(obj);
					}
					rset = stmt.executeQuery("select paRestants from troll where idTroll="+idTroll);
					int ancVal=0;
					while (rset.next()){
						ancVal = rset.getInt("paRestants");
					}
					stmt.executeUpdate("update troll SET paRestants = " + (ancVal-1) + " where idTroll=" + idTroll);
					paRestants = paRestants-1;
				}
				else {
					System.out.println("Cet objet ne se trouve pas sur votre case !");
				}
			}
			else {
				System.out.println("Il n'y a aucun objet sur votre case");
			}
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}

	public void tesObjets () {

		try {
			ResultSet rset = stmt.executeQuery("select idObjet from equipement where idTroll="+idTroll + " and estEquipe=false");
			while (rset.next()){
				String idObj = rset.getString("idObjet");
				System.out.println(idObj);
			}
			rset.close();
			System.out.println();
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}

	public void equiperObjet (String idObjet) {

		int val=0;
		int ancVal=0;
		String carac=null;
		try {
			stmt.executeUpdate("update equipement SET estEquipe = true where idObjet ='" + idObjet + "' and idTroll=" + idTroll);
			ResultSet rset = stmt.executeQuery("select caracteristique from carac where idObjet='"+idObjet+"'");
			while (rset.next()){
				carac = rset.getString("caracteristique");
			}

			rset = conn.createStatement().executeQuery("select valeur from carac where idObjet='"+idObjet+"'");
			while (rset.next()){
				val = rset.getInt("valeur");
			}

			rset = stmt.executeQuery("select " + carac + " from troll where idTroll=" + idTroll);
			while (rset.next()){
				ancVal = rset.getInt(carac);
			}
			stmt.executeUpdate("update troll SET " + carac + " = " + (ancVal+val) + "where idTroll=" + idTroll);
			rset = stmt.executeQuery("select paRestants from troll where idTroll="+idTroll);
			while (rset.next()){
				ancVal = rset.getInt("paRestants");
			}
			stmt.executeUpdate("update troll SET paRestants = " + (ancVal-1) + " where idTroll=" + idTroll);
			paRestants = paRestants-1;
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}

	public int positionX() {
		return (positionX);
	}

	public int positionY() {
		return (positionY);
	}

	public void desequiperObjet(String idObjet) {
		int val=0;
		int ancVal=0;
		String carac=null;
		try {
			stmt.executeUpdate("update equipement SET estEquipe = false where idObjet ='" + idObjet + "' and idTroll=" + idTroll);
			ResultSet rset = stmt.executeQuery("select caracteristique from carac where idObjet='"+idObjet+"'");
			while (rset.next()){
				carac = rset.getString("caracteristique");
			}

			rset = conn.createStatement().executeQuery("select valeur from carac where idObjet='"+idObjet+"'");
			while (rset.next()){
				val = rset.getInt("valeur");
			}

			rset = stmt.executeQuery("select " + carac + " from troll where idTroll=" + idTroll);
			while (rset.next()){
				ancVal = rset.getInt(carac);
			}
			stmt.executeUpdate("update troll SET " + carac + " = " + (ancVal-val) + "where idTroll=" + idTroll);
			rset = stmt.executeQuery("select paRestants from troll where idTroll="+idTroll);
			while (rset.next()){
				ancVal = rset.getInt("paRestants");
			}
			stmt.executeUpdate("update troll SET paRestants = " + (ancVal-1) + " where idTroll=" + idTroll);
			paRestants = paRestants-1;
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}

	public void utiliserPopo (objet potion) {
		int val=0;
		int ancVal=0;
		String carac=null;
		try {
			stmt.executeUpdate("update equipement SET estEquipe = true where idObjet ='" + potion.idObjet() + "' and idTroll=" + idTroll);
			ResultSet rset = stmt.executeQuery("select caracteristique from carac where idObjet='"+potion.idObjet()+"'");
			while (rset.next()){
				carac = rset.getString("caracteristique");
			}

			rset = conn.createStatement().executeQuery("select valeur from carac where idObjet='"+potion.idObjet()+"'");
			while (rset.next()){
				val = rset.getInt("valeur");
			}

			rset = stmt.executeQuery("select " + carac + " from troll where idTroll=" + idTroll);
			while (rset.next()){
				ancVal = rset.getInt(carac);
			}
			stmt.executeUpdate("update troll SET " + carac + " = " + (ancVal+val) + "where idTroll=" + idTroll);
			rset = stmt.executeQuery("select paRestants from troll where idTroll="+idTroll);
			while (rset.next()){
				ancVal = rset.getInt("paRestants");
			}
			stmt.executeUpdate("update troll SET paRestants = " + (ancVal-1) + " where idTroll=" + idTroll);
			tabPopoEnCours.put(potion.idObjet(), potion);
			paRestants = paRestants-1;
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}

	public void supprimerPopo(objet popo) {

		String idPopo = popo.idObjet();
		int val=0;
		int ancVal=0;
		String carac=null;
		try {
			ResultSet rset = stmt.executeQuery("select caracteristique from carac where idObjet='"+idPopo+"'");
			while (rset.next()){
				carac = rset.getString("caracteristique");
			}

			rset = conn.createStatement().executeQuery("select valeur from carac where idObjet='"+idPopo+"'");
			while (rset.next()){
				val = rset.getInt("valeur");
			}

			rset = stmt.executeQuery("select " + carac + " from troll where idTroll=" + idTroll);
			while (rset.next()){
				ancVal = rset.getInt(carac);
			}
			stmt.executeUpdate("update troll SET " + carac + " = " + (ancVal-val) + " where idTroll=" + idTroll);
			stmt.executeUpdate("delete from equipement where idObjet='"+idPopo+"'");
			stmt.executeUpdate("delete from carac where idObjet='"+idPopo+"'");
			stmt.executeUpdate("delete from objet where idObjet='"+idPopo+"'");
			tabPopoEnCours.remove(idPopo);
			//menu.supprimerPopo(idPopo);
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
	}

	public Hashtable tesPopoEnCours() {
		return (tabPopoEnCours);
	}

	public String getValeur() {
		return (valeur);
	}

	public void verifTourPopo() {
		Enumeration enumPopo = tabPopoEnCours.elements();
		while (enumPopo.hasMoreElements()) {
			objet popo = (objet) enumPopo.nextElement();
			popo.decrementerTourPopo();
			System.out.println(popo.duree() + " tour(s) restants pour la potion "+popo.idObjet());
			if (popo.duree()==0) {
				this.supprimerPopo(popo);
			}
		}
	}

	public void afficherPopo() {
		Enumeration enumPopo = tabPopoEnCours.elements();
		if (!enumPopo.hasMoreElements()) {
			System.out.println("      *    - Aucune                        *");
		}
		while (enumPopo.hasMoreElements()) {
			objet popo = (objet) enumPopo.nextElement();
			System.out.println("      *    - "+popo.idObjet() +" : " + popo.duree() + " tour(s) restant(s)*");
		}
	}

	public void reinitialiserTroll () {
		String idObjet;
		try {
			ResultSet rset = stmt.executeQuery("select idObjet from equipement where idTroll="+idTroll);
			while (rset.next()){
				idObjet = rset.getString("idObjet");
				this.desequiperObjet(idObjet);
				//int res = stmt.executeUpdate("delete from equipement where idObjet=idObjet");
			}
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}
		Enumeration enumPopo = tabPopoEnCours.elements();
		while (enumPopo.hasMoreElements()) {
			objet popo = (objet) enumPopo.nextElement();
			popo.supprimerTourPopo();
			this.verifTourPopo();
		}
	}
}
