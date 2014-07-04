package troll.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
//~import java.util.*;

public class IO {

	// *******************************
	// Attributes
	// *******************************
	private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Wrapper method to read an integer from stdin.
	 *
	 * @return int
	 */
	public static int readInt()
	{
		int value = 0;
		boolean ok = false;

		try {
			do {
				try {
					value = Integer.parseInt(input.readLine());
					ok = true;
				} catch (NumberFormatException e) {
					System.out.println("Not an integer");
				}
			} while (!ok);
		}
		catch (IOException e) {
			System.out.println(" ERREUR d'E/S : IO.LIRE_ENTIER");
		}

		return value;
	}

	/**
	 * Wrapper method to read a String from stdin.
	 *
	 * @return String
	 */
	public static String readString()
	{
		String value = null;

		try {
			value =  input.readLine();
		} catch (IOException e) {
			System.out.println(" ERREUR d'E/S : IO.LIRE_CHAINE");
		}

		return value;
	}
}
