package troll;

public class Start {

	// ****************************
	// Constructeur
	// ****************************
	public Start() {

		try {
			Class.forName("org.postgresql.Driver");
		}// try
		catch (Exception E) {
			System.err.println("Impossible de Charger le driver.");
			return;
		}// catch

		new Menu();

		// Appel au Menu Principal
		// Sauvegarde des objets de l'application
	} // Fin Constructeur

	// ************************************
	// Programme Principal
	// ************************************

	public static void main(String Args[]) {

		new Start();
	} // Fin main

} // Fin Classe Start
