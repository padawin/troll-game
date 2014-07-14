package troll;

import troll.gui.Menu;

public class Start {

	public Start() {

		try {
			Class.forName("org.postgresql.Driver");
		}// try
		catch (Exception E) {
			System.err.println("Impossible de Charger le driver.");
			return;
		}// catch

		new Menu();
	}

	public static void main(String Args[]) {

		new Start();
	}
}
