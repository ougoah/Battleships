//hguo1339

package core;

import java.util.Collection;
import java.util.ArrayList;
import hguo1339.*;
import util.Coordinate;
import util.Ship;
import util.ShipPlacement;

/**
 * Game class.
 * 
 * Runs a game with a given board size with 2 players and a collection of ships.
 * 
 * @author Alex
 * 
 */
public class BattleShips { //fix up the 0, 1 stuff corresponding to players 1 and 2
	
	/**
	 * Constructor - set up the game.
	 * 
	 * @param boardHeight
	 * @param boardWidth
	 * @param players
	 *            - an array of players. Must be size 2 (0 for player 1 and 1
	 *            for player 2)
	 * @param ships
	 *            - a collection of ships that will be used in the game.
	 */
	char[][] b1;
	char[][] b2;
	Player p1;
	Player p2;
	Collection<Ship> s;
	int activeplayer = 0; //this is player 1
	ShipPlacement sp1, sp2;
	
	public BattleShips(int boardHeight, int boardWidth, Player[] players, Collection<Ship> ships) {
		b1 = new char[boardHeight][boardWidth];
		b2 = new char[boardHeight][boardWidth];
		p1 = players[0];
		p2 = players[1];
		s = ships;
	}

	/**
	 * Run the game.
	 * 
	 * You MUST: 
	 * 		1. Notify the players of who they're playing. (names are based
	 * from the class). You may find .getClass() and .getCanonicalName() useful.
	 * 		2. Ask the players for the ship placements and resolve them. 
	 * If a player places a ship on top of another ship, the old ship sinks.
	 * 		3. Ask each player for a shot. When you have both player's responses, you will
	 * resolve the action and notify the player of the outcome. Possible outcomes are:
	 * 			*	"HIT (x,y)" where x and y are the coordinates
	 *	 		*	"MISS"
	 *	 		*	"SUNK X" where X is the ship
	 * 			*	"WIN"
	 * 			*	"LOSE"
	 *	 		*	"DRAW"
	 * 			*	"SHIP X RAN AGROUND"
	 * 			*	"X RAN OVER Y AND SUNK IT" where X and Y are different ships 
	 * 		4. Check if any player has had all of their ships sunk.
	 * 
	 * @return the winning player
	 * @return null - for a draw
	 */
	public Player run() {
		//fill board with . by default
		for (int i = 0; i < b1.length; i++) {
			for (int j = 0; j < b1[0].length; j++) {
				b1[i][j] = '.';
				b2[i][j] = '.';
			}
		}

		//ask for ship placements
		for (Ship sh : s) {
			sp1 = p1.getShipPlacement(sh, b1);
			placeShip(sp1);
			nextPlayer();
			sp2 = p2.getShipPlacement(sh, b2);
			placeShip(sp2);
			nextPlayer();
		}
		generateView(1);
		System.out.println();
		generateView(2);
		System.out.println();
		
		while (!isPlayerDead(0) && !isPlayerDead(1)) { //while both players are alive, continue the game; otherwise, notify players of result
			Coordinate[] player1shots = p1.getNextShots(b2, getNumShots(1));
			Coordinate[] player2shots = p2.getNextShots(b1, getNumShots(2));
			for (Coordinate s : player1shots) {
				int x = s.getX();
				int y = s.getY();
				if (Character.isUpperCase(b2[y][x])) {
					p1.notify("HIT" + "(" + x + "," + y + ")");
				}
				else {
					p1.notify("MISS");
				}
				shoot(s);
			}
			nextPlayer();
			for (Coordinate s : player2shots) {
				int x = s.getX();
				int y = s.getY();
				if (Character.isUpperCase(b1[y][x])) {
					p2.notify("HIT" + "(" + x + "," + y + ")");
				}
				else {
					p2.notify("MISS");
				}
				shoot(s);
			}
			nextPlayer();
		}
		if (getWinner() == 1) {
			p1.notify("WIN");
			p2.notify("LOSE");
		}
		else if (getWinner() == 2) {
			p1.notify("LOSE");
			p2.notify("WIN");
		}
		else if (getWinner() == 5) {
			p1.notify("DRAW");
			p2.notify("DRAW");
		}
		System.out.println("PLAYER " + getWinner() + " WINS.");
		System.out.println("__________");

		resetNextPlayer();
		if (getWinner() == 1) {
			return p1;
		}
		else if (getWinner() == 2) {
			return p2;
		}
		return null;
	}

	/**
	 * Function to check if the shot is valid
	 * 
	 * A shot is valid if and only if it is inside the bounds of the board.
	 * Players can shoot in the same place twice if they wish so.
	 * 
	 * @param shot - coordinates of the shot
	 * @return true - the shot is valid
	 * @return false - the shot is invalid
	 */
	protected boolean isValidShot(Coordinate shot) {
		if (shot.getX() < 10 && shot.getY() < 10) {
			return true;
		}
		return false;
	}

	protected void shoot(Coordinate shot) {
		int x = shot.getX();
		int y = shot.getY();
		if (isValidShot(shot)) {
			if (activeplayer == 0) {
				b2[y][x] = '.';
			}
			else if (activeplayer == 1){
				b1[y][x] = '.';
			}
		}
		else {
			return;
		}
	}

	protected boolean isValidPlacement(ShipPlacement placement) { //need to change this
		int beginningx = placement.getBeginning().getX();
		int beginningy = placement.getBeginning().getY();
		int endx = placement.getEnd().getX();
		int endy = placement.getEnd().getY();
		if (beginningx < 10 && endx < 10 && beginningy < 10 && endy < 10) {
			return true;
		}
		return false;
	}

	/**
	 * Resolve the ship placement.
	 */
	protected void placeShip(ShipPlacement placement) {
		int beginningx = placement.getBeginning().getX();
		int beginningy = placement.getBeginning().getY();
		int endx = placement.getEnd().getX();
		int endy = placement.getEnd().getY();
		char handle = placement.getShip().getShipHandle();
		if (activeplayer == 0) {
			if (beginningy == endy) {
				for (int i = beginningx; i <= endx; i++) {
					if (isValidPlacement(placement)) {
						if (!Character.isLetter(b1[beginningy][i])) {
							b1[beginningy][i] = handle;
						}
						else {
							p1.notify("SHIP " + handle + "RAN OVER SHIP " + b1[beginningy][i] + " AND SANK IT");
						}
					}
					else {
						p1.notify("SHIP" + handle + "RAN AGROUND");
					}
				}
			}
			else {
				for (int i = beginningy; i <= endy; i++) {
					if (isValidPlacement(placement)) {
						if (!Character.isLetter(b1[beginningy][i])) {
							b1[i][beginningx] = handle;
						}
						else {
							p1.notify("SHIP " + handle + "RAN OVER SHIP " + b1[i][beginningx] + " AND SANK IT");
						}
					}
					else {
						p1.notify("SHIP" + handle + "RAN AGROUND");
					}
				}
			}
		}
		if (activeplayer == 0) {
			if (beginningy == endy) {
				for (int i = beginningx; i <= endx; i++) {
					if (isValidPlacement(placement)) {
						if (!Character.isLetter(b2[beginningy][i])) {
							b2[beginningy][i] = handle;
						}
						else {
							p2.notify("SHIP " + handle + "RAN OVER SHIP " + b2[beginningy][i] + " AND SANK IT");
						}
					}
					else {
						p2.notify("SHIP" + handle + "RAN AGROUND");
					}
				}
			}
			else {
				for (int i = beginningy; i <= endy; i++) {
					if (isValidPlacement(placement)) {
						if (!Character.isLetter(b2[beginningy][i])) {
							b2[i][beginningx] = handle;
						}
						else {
							p2.notify("SHIP " + handle + "RAN OVER SHIP " + b2[i][beginningx] + " AND SANK IT");
						}
					}
					else {
						p2.notify("SHIP" + handle + "RAN AGROUND");
					}
				}
			}
		}
	}
	/**
	 * Function to check whether a player has had all of their ships sunk
	 * @param player - the player being checked (0 or 1)
	 * @return true - the player has no more living ships
	 * @return false - the player has living ships
	 */
	protected boolean isPlayerDead(int player) {
		if (player == 0) {
			for (int i = 0; i < b1.length; i++) {
				for (int j = 0; j < b1[0].length; j++) {
					if (b1[i][j] != '.') {
						return false;
					}
				}
			}
		}
		if (player == 1) {
			for (int i = 0; i < b2.length; i++) {
				for (int j = 0; j < b2[0].length; j++) {
					if (b2[i][j] != '.') {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Change the current player to the next player
	 */
	protected void nextPlayer() {
		activeplayer = (++activeplayer)%2; //changes player
	}

	/**
	 * Generate a view of the board for a player.
	 * 
	 * 	'.' - denotes a miss
	 *  'H' - denotes a hit
	 * 
	 * @param player - the player for who the view is generated ( 0 or 1)
	 * @return the two dimensional array representation of the view of the board.
	 */
	protected char[][] generateView(int player) { //generates the view of the player's OWN board
		for (int i = 0; i < b1.length; i++) {
			for (int j = 0; j < b1[0].length; j++) {
				if (player == 1) {
					System.out.print(b1[i][j]);
				}
				else if (player == 2) {
					System.out.print(b2[i][j]);
				}
			}
			System.out.println();
		}
		return null;
	}

	/**
	 * (OPTIONAL)
	 * Print the board state for a player
	 * @param player - the player for who the view is generated ( 0 or 1)
	 * @return a string representation of the board.
	 */
	protected String printBoard(int player) {
		return null;
	}

	/**
	 * Sink an entire ship
	 * 
	 * the ship is removed from the board and replaced with 0.
	 * 
	 * @param handle - the handle of the ship
	 */
	protected void sinkShip(char handle) {
		for (int i = 0; i < b1.length; i++) {
			for (int j = 0; j < b1[0].length; j++) {
				if (b1[i][j] == handle) {
					b1[i][j] = 0;
				}
				if (b2[i][j] == handle) {
					b2[i][j] = 0;
				}
			}
		}
	}

	protected int getNumShots(int player) {
		return 1;
	}
	protected int getWinner() {
		if (isPlayerDead(0) && !isPlayerDead(1)) {
			return 2; //p2 wins
		}
		else if (isPlayerDead(1) && !isPlayerDead(0)) {
			return 1; //p1 wins
		}
		else {
			return 5; //draw
		}
	}
	private void resetNextPlayer() {
		activeplayer = 0; //at the end of each round, this is executed to reset the order of the players
	}
}
