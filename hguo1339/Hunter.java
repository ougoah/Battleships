package hguo1339;

import java.util.ArrayList;
import util.Coordinate;
import util.Ship;
import util.ShipPlacement;
import core.*;
import java.util.Random;

public class Hunter implements Player {
	
	public String name = "SequentialShooter";
	private char[][] myboard = new char[10][10]; //my own board
	private int lastusedrow = 5;
	private int originalrow = lastusedrow;
	private Random rand = new Random();
	//coordinate definitions
	private Coordinate originalcoord = new Coordinate(0,0);
	private Coordinate searchcoord = new Coordinate(0,0);
	private Coordinate nexthuntingcoord = new Coordinate(0,0);
	private Coordinate nextcoord = originalcoord; //the next coordinate to strike
	private ArrayList<Coordinate> strikelog = new ArrayList<Coordinate>(); //keeps track of the hits and misses
	private ArrayList<Coordinate> hitlog = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> available = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> preferrable = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> whitesquares = new ArrayList<Coordinate>();
	
	private void fillWhite() {
		for (int i = 0; i < 10; i += 2) {
			for (int j = 0; j < 10; j += 2) {
				whitesquares.add(new Coordinate(i,j));
			}
		}
		for (int i = 1; i < 11; i += 2) {
			for (int j = 1; j < 11; j += 2) {
				whitesquares.add(new Coordinate(i,j));
			}
		}
	}
	private void fillAvailable() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				available.add(new Coordinate(i,j));
			}
		}
	}
	
	private void addToPreferrable(Coordinate c) { //execute this method when you have a hit
		if (!isInPreferrable(left(c))) {
			preferrable.add(left(c));
		}
		if (!isInPreferrable(up(c))) {
			preferrable.add(up(c));
		}
		if (!isInPreferrable(right(c))) {
			preferrable.add(right(c));
		}
		if (!isInPreferrable(down(c))) {
			preferrable.add(down(c));
		}
	}
	
	private boolean isInPreferrable(Coordinate c) {
		for (Coordinate co : preferrable) {
			if (co.equals(c)) {
				return true;
			}
		}
		return false;
	}
	private boolean isAvailable(Coordinate c) {
		for (Coordinate co : available) {
			if (co.equals(c)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPreferrable(Coordinate c) {
		for (Coordinate co : preferrable) {
			if (co.equals(c)) {
				return true;
			}
		}
		return false;
	}
	
	private void resetRow() {
		lastusedrow = originalrow;
	}
	
	private void resetCoords() {
		nextcoord = originalcoord;
		nexthuntingcoord = originalcoord;
		searchcoord = originalcoord;
	}

	@Override
	public Coordinate[] getNextShots(char[][] currBoard, int numShots) {
		Coordinate[] nextshot = new Coordinate[1]; //only handles the single shot case so far
		for (Coordinate co1 : available) {
			for (Coordinate co2 : preferrable) {
				if (co2.equals(co1)) {
					nextshot[0] = co1;
					available.remove(co1);
					return nextshot;
				}
			}
			for (Coordinate co2: whitesquares) {
				if (co2.equals(co1)) {
					nextshot[0] = co1;
					available.remove(co1);
					return nextshot;
				}
			}
			nextshot[0] = co1;
			available.remove(co1);
			return nextshot;
		}
		return nextshot;
	}
	public boolean isValidCoord(Coordinate c) {
		if (c.getX() >= 10 || c.getY() >= 10) {
			return false;
		}
		return true;
	}
	public Coordinate left(Coordinate c) {
		if (isValidCoord(new Coordinate(c.getX() - 1, c.getY()))) {
			return new Coordinate(c.getX() - 1, c.getY());
		}
		else {
			return up(c);
		}
	}
	public Coordinate right(Coordinate c) {
		if (isValidCoord(new Coordinate(c.getX() + 1, c.getY()))) {
			return new Coordinate(c.getX() + 1, c.getY());
		}
		else {
			return down(c);
		}
	}
	public Coordinate up(Coordinate c) {
		if (isValidCoord(new Coordinate(c.getX(), c.getY() - 1))) {
			return new Coordinate(c.getX(), c.getY() - 1);
		}
		else {
			return right(c);
		}
	}
	public Coordinate down(Coordinate c) {
		if (isValidCoord(new Coordinate(c.getX(), c.getY() + 1))) {
			return new Coordinate(c.getX(), c.getY() + 1);
		}
		else {
			return left(c);
		}
	}
	
	@Override
	public ShipPlacement getShipPlacement(Ship ship, char[][] currBoard) {
		fillAvailable();
		fillWhite();
		int r = rand.nextInt(6);
		Coordinate beginning = new Coordinate(r, lastusedrow);
		Coordinate end = new Coordinate(r + ship.getShipHeight() - 1, lastusedrow);
		ShipPlacement sp = new ShipPlacement(ship, beginning, end);
		lastusedrow = (lastusedrow + 2) % 10; //
		return sp;
	}
	
//	public void updateMyBoard(ShipPlacement sp) {
//		char handle = sp.getShip().getShipHandle();
//		Coordinate beginning = sp.getBeginning();
//		Coordinate end = sp.getEnd();
//		int begx = beginning.getX();
//		int begy = beginning.getY();
//		int endx = end.getX();
//		int endy = end.getY();
//		
//		for (int i = begy; i <= endy; i++) { //this checks for both vertical and horizontal placements
//			for (int j = begx; j <= endx; j++) {
//				myboard[i][j] = handle;
//			}
//		}
//	}
	
	public boolean searchStrikeLog(Coordinate c) {
		for (Coordinate str : strikelog) {
			if (str.equals(c)) {
				return true;
			}
		}
		return false;
	}
//	public void updateMyBoard(String message, Coordinate c) { //do i want to use this information?
//		int x = c.getX();
//		int y = c.getY(); //if it's my board I don't need to distinguish between hits from the enemy and empty spots; not so for the enemy's board
//	}
	
	@Override
	public void notify(String message) {
		String msg = message;
		if (msg.substring(0,3).equals("HIT")) {
			hitlog.add(nextcoord);
			addToPreferrable(nextcoord);
//			nextSearch(nextcoord);
		}
//		else if (msg.equals("MISS")) {
//			nextHunt();
//		}
		else if (msg.equals("LOSE") || msg.equals("WIN") || msg.equals("DRAW")) {
			resetRow();
			resetCoords();
			fillAvailable();
		}
	}

	@Override
	public void newOpponent(String name) {
		System.out.println("New Challenger");
	}
}
