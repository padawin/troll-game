package troll.gui;

import troll.model.Game;
import troll.util.IO;
import java.util.ArrayList;

class JoinGameMenu
{
	public int run()
	{
		System.out.println();
		System.out.println();
		System.out.println("      **************************************");
		System.out.println("      *              Join game             *");
		System.out.println("      **************************************");
		ArrayList<Game> games = Game.getGames();
		int i, nbGames, choice;
		nbGames = games.size();

		if (nbGames == 0) {
			System.out.println("No game");
			return -1;
		}
		else {

			System.out.println("      * List of existing games:            *");
			for (i = 0; i < nbGames; i++) {
				System.out.println("      * - " + games.get(i).getName());
			}

			do {
				System.out.print("      Your choice: ");
				choice = IO.readInt();
				System.out.println();
			} while (choice < 0 || choice >= nbGames);

			return 1;
		}
	}
}
