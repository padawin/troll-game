package troll.gui;

import java.sql.*;
import java.util.Hashtable;

import troll.util.IO;
import troll.model.Connection;

public class Menu
{
	public Menu()
	{
		int choice;

		System.out.println();
		System.out.println();
		System.out.println("      **************************************");
		System.out.println("      *             TrollFight             *");
		System.out.println("      **************************************");
		System.out.println("      * 1 - New game                       *");
		System.out.println("      *                                    *");
		System.out.println("      * 2 - Join game                      *");
		System.out.println("      *                                    *");
		System.out.println("      * 0- Quit                            *");
		System.out.println("      **************************************");
		System.out.println();

		do {
			System.out.print("      Your choice: ");
			choice = IO.readInt();

			switch (choice) {
				case 1: {
					NewGameMenu nGM = new NewGameMenu();
					int result = nGM.run();
					System.out.println("New game");
					break;
				}
				case 2: {
					JoinGameMenu jGM = new JoinGameMenu();
					int result = jGM.run();

					if (result == -1) {
						choice = -1;
					}
					break;
				}
				default:
					break;
			} // switch
			System.out.println();
		} while (choice < 0 || choice > 2);
	}
}
