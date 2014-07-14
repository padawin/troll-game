package troll.gui;

import troll.model.Game;
import troll.util.IO;
import java.util.ArrayList;

class NewGameMenu
{
	public int run()
	{
		String gameName;
		boolean nameOk;

		System.out.println();
		System.out.println();
		System.out.println("      **************************************");
		System.out.println("      *              New game              *");
		System.out.println("      **************************************");

		do {
			System.out.println("        Game name:");
			gameName = IO.readString();
			Game g = Game.loadFromName(gameName);
			nameOk = true;
			if (g != null) {
				System.out.println("A game named " + gameName
					+ " already exists, please choose another name");
				nameOk = false;
			}
		} while (!nameOk);
		System.out.println("The game name is " + gameName);
		return 1;
	}
}
