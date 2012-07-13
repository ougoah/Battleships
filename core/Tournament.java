package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import util.Ship;

public class Tournament {

	/**
	 * Main method for the tournament.
	 * 
	 * @param arg1
	 *            - Location for the ship configuration
	 */
	public static void main(String[] args) {

		// make sure you have the right parameters
		if (args.length < 1) {
			System.exit(2);
		}

		// load Config

		// defaults
		int width = 10;
		int height = 10;
		int repetitions = 100;

		ArrayList<Ship> ships = new ArrayList<Ship>();
		try {
			Scanner configReader = new Scanner(new File(args[0]));
			while (configReader.hasNextLine()) {
				Scanner read = new Scanner(configReader.nextLine());
				read.useDelimiter("=");
				String tag = read.next();
				// load height
				if (tag.equals("height")) {
					height = Integer.parseInt(read.next());
				}
				// load width
				else if (tag.equals("width")) {
					width = Integer.parseInt(read.next());
				}
				// load repetitions
				else if (tag.equals("repetitions")) {
					repetitions = Integer.parseInt(read.next());
				}
				// load ships
				else if (tag.equals("ships")) {
					String players = read.next();
					players = players.replace("{", "");
					players = players.replace("}", "");
					Scanner shipReader = new Scanner(players);
					shipReader.useDelimiter(",");
					while (shipReader.hasNext()) {
						String newlist = shipReader.next();
						Scanner line = new Scanner(newlist);
						line.useDelimiter(":*x*");
						if (newlist.matches("[a-z]:[0-9]+x[0-9]+")) {
							ships.add(new Ship(line.next().charAt(0), line.nextInt(), line.nextInt()));
						}
					}
				}
			}
			configReader.close();
		} catch (FileNotFoundException e) {
			System.exit(3);
		}

		// Get the list of players
		HashMap<Player, Integer> players = new HashMap<Player, Integer>();
		File directoryOfPlayers = new File("src"); // Directory is just a list
													// of files

		if (directoryOfPlayers.isDirectory()) { // check to make sure it is a
												// directory
			ArrayList<String> playersList = new ArrayList<String>();
			String filenames[] = directoryOfPlayers.list();// make array of
															// filenames.
			for (String temp : filenames) {
				if (temp.matches("[a-z]{4}[0-9]{4}")) {
					File directoryOfPlayerClasses = new File("src/" + temp);
					if (directoryOfPlayerClasses.isDirectory()) { // check to
																	// make sure
																	// it is a
																	// directory
						String playerClasses[] = directoryOfPlayerClasses.list();// make
																					// array
																					// of
																					// filenames.
						for (String temp2 : playerClasses) {
							if (temp2.endsWith(".java")) {
								try {
									Scanner fileScanner = new Scanner(new File("src/" + temp + "/" + temp2));
									while (fileScanner.hasNextLine()) {
										// public class MyPlayer implements
										// Player
										if (fileScanner.nextLine().matches(".*?public class \\w+ implements Player.*")) {
											playersList.add(temp + "." + temp2.replaceAll(".java", ""));
										}
									}
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			if (playersList.size() == 0) {
				System.out.println("No players could be found.");
				System.exit(1);
			}
			if (playersList.size() == 1) {
				playersList.add(playersList.get(0));
			}
			for (int i = 0; i < playersList.size(); ++i) {
				try {
					players.put((Player) Class.forName(playersList.get(i)).newInstance(), 0);
				} catch (InstantiationException e) {
					System.err.println("Could not instantiate class of type: " + playersList.get(i));
					System.exit(-1);
				} catch (IllegalAccessException e) {
					System.err.println(playersList.get(i) + "does not seem to implement core.Player.");
					System.exit(-1);
				} catch (ClassNotFoundException e) {
					System.err.println("Could not find class " + playersList.get(i));
					System.exit(-1);
				}
			}
		}

		// Let the games begin !!!
		ArrayList<Player> allPlayers = new ArrayList<Player>();
		allPlayers.addAll(players.keySet());
		for (int i = 0; i < players.size(); ++i) {
			for (int j = i + 1; j < players.size(); ++j) {
				Player player1 = allPlayers.get(i);
				Player player2 = allPlayers.get(j);
				for (int turns = 0; i < repetitions; ++i) {
					Player[] currentPlayers = new Player[2];
					currentPlayers[turns % 2] = player1;
					currentPlayers[(turns + 1) % 2] = player2;
					BattleShips game = new BattleShips(height, width, currentPlayers, ships);
					Player winner = game.run();
					if (winner == null) {
						players.put(player1, players.get(player1) + 1);
						players.put(player2, players.get(player2) + 1);
					} else {
						players.put(winner, players.get(winner) + 2);
					}
				}
			}
		}

		for (Entry<Player, Integer> entry : players.entrySet()) {
			System.out.println(entry.getKey().getClass().getCanonicalName() + " : " + entry.getValue());
		}
	}
}
