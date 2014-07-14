package troll.model;

import troll.model.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Game
{
	protected int _id;
	protected String _name;

	public static ArrayList<Game> getGames()
	{
		Connection connection = new Connection();
		Statement stmt = connection.getStatement();

		ArrayList<Game> games = new ArrayList<Game>();
		try {
			ResultSet rset = stmt.executeQuery("SELECT id, name FROM game");
			while (rset.next()) {
				Game g = new Game();
				g.setId(rset.getInt("id"));
				g.setName(rset.getString("name"));
				games.add(g);
			}
			rset.close();
		}
		catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}

		return games;
	}

	public void setId(int id)
	{
		this._id = id;
	}

	public void setName(String name)
	{
		this._name = name;
	}

	public int getId()
	{
		return this._id;
	}

	public String getName()
	{
		return this._name;
	}
}
