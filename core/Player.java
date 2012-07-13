package core;
import util.Coordinate;
import util.Ship;
import util.ShipPlacement;

public interface Player {
	/**
	 * Get the next proposed shot of the player
	 * @return
	 */
	public Coordinate[] getNextShots(char[][] currBoard, int numShots);
	
	/**
	 * Get the ship placement
	 * @param size size of the ship
	 * @param currBoard the current state of the board
	 * @return
	 */
	public ShipPlacement getShipPlacement(Ship ship, char[][] currBoard);
	
	/**
	 * Notified of a message by the game.
	 * Can be:
	 * 		"HIT (x,y)"
	 * 		"MISS"
	 * 		"SUNK X" where X is the ship
	 * 		"WIN"
	 * 		"LOSE"
	 * 		"DRAW"
	 * 		"SHIP X RAN AGROUND"
	 * 		"X RAN OVER Y AND SUNK IT" where X and Y are different ships 
	 * @param message
	 */
	public void notify(String message);
	
	/**
	 * Notify player of a new opponent.
	 * @param name
	 */
	public void newOpponent(String name);
}
