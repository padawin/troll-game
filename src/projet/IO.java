package projet;

import java.io.*;
import java.util.*;

public class IO {

	// *******************************
	// Attributs
	// *******************************
	private static BufferedReader entree = new BufferedReader(
			new InputStreamReader(System.in));

	// *******************************
	// Constructeur
	// *******************************
	public IO() {
	}

	// ********************************
	// Méthodes Publiques
	// ********************************
	public static int lireEntier() {
		boolean ok = false;
		int valentiere = 0;

		try {
			do {
				try {
					valentiere = Integer.parseInt(entree.readLine());
					ok = true;
				} catch (NumberFormatException e) {
					System.out.println("Non un entier. Recommencez.");
				}
			} while (!ok);

			return valentiere;
		} catch (IOException e) {
			System.out.println(" ERREUR d'E/S : IO.LIRE_ENTIER");
			return 0;
		}
	} // Fin de lireEntier

	// ***********************************************
	public static float lireFlottant() {
		boolean ok = false;
		float valflottant = 0;
		Float flot;
		try {
			do {
				try {
					flot = Float.valueOf(entree.readLine());
					valflottant = flot.floatValue();
					ok = true;
				} catch (NumberFormatException e) {
					System.out.println("Non un flottant. Recommencez.");
				}
			} while (!ok);
			return valflottant;
		} catch (IOException e) {
			System.out.println(" ERREUR d'E/S : IO.LIRE_ENTIER");
			return 0;
		}
	} // Fin lireFlottant

	// ***********************************************
	public static String lireChaine() {
		try {
			return entree.readLine();
		} catch (IOException e) {
			System.out.println(" ERREUR d'E/S : IO.LIRE_CHAINE");
			return "";
		}
	} // Fin lireChaine
} // Fin de classe IO
