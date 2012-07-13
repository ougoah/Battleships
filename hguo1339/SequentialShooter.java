package hguo1339;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import util.Coordinate;
import util.Ship;
import util.ShipPlacement;
import core.*;

public class SequentialShooter implements Player {
	
	public String name = "SequentialShooterII";
	private char[][] myboard = new char[10][10]; //my own board
	private char[][] enemyboard = new char[10][10]; //opponent's board, as far as is known
	private Random rand = new Random();
	private int lastusedrow = 6;
	private ArrayList<Ship> orderedships= new ArrayList<Ship>();
	private ArrayList<Integer> shipheights = new ArrayList<Integer>();
		
	private Coordinate nextcoord = new Coordinate(0,0); //variable storing the next coordinate to strike
	private Coordinate originalcoord = nextcoord;
	private ArrayList<String> hitlog = new ArrayList<String>(); //keeps track of the hits and misses
	
	private void orderShipsByHeight(ArrayList<Ship> s) {
		for (Ship sh : s) {
			int h = sh.getShipHeight();
			shipheights.add(h);
		}
		Collections.sort(shipheights);
	}
	
	private void addShip(Ship sh) {
		orderedships.add(sh);
	}
	private void resetRow() {
		lastusedrow = 6;
	}
	private void resetCoords() {
		nextcoord = originalcoord;
	}

	@Override
	public Coordinate[] getNextShots(char[][] currBoard, int numShots) {
		Coordinate[] nextshot = new Coordinate[numShots];
		for (int i = 0; i < numShots; i++) {
			nextshot[i] = nextcoord;
		}
		return nextshot;
	}

	@Override
	public ShipPlacement getShipPlacement(Ship ship, char[][] currBoard) { //positions not length-dependent yet
		int r = rand.nextInt(6);
		Coordinate beginning = new Coordinate(r, lastusedrow);
		Coordinate end = new Coordinate(r + ship.getShipHeight() - 1, lastusedrow);
		ShipPlacement sp = new ShipPlacement(ship, beginning, end);
		lastusedrow = (lastusedrow + 1) % 10; //
		updateMyBoard(sp);
		return sp;
	}
	
	public void updateMyBoard(ShipPlacement sp) {
		char handle = sp.getShip().getShipHandle();
		Coordinate beginning = sp.getBeginning();
		Coordinate end = sp.getEnd();
		int begx = beginning.getX();
		int begy = beginning.getY();
		int endx = end.getX();
		int endy = end.getY();
		
		for (int i = begy; i <= endy; i++) { //this checks for both vertical and horizontal placements
			for (int j = begx; j <= endx; j++) {
				myboard[i][j] = handle;
			}
		}
	}
	public void updateMyBoard(String message, Coordinate c) { //do i want to use this information?
		int x = c.getX();
		int y = c.getY(); //if it's my board I don't need to distinguish between hits from the enemy and empty spots; not so for the enemy's board
	}
	
	@Override
	public void notify(String message) {
		hitlog.add(message);
		int currentx = nextcoord.getX();
		int currenty = nextcoord.getY();
		if (currentx == 9) {
			nextcoord = new Coordinate(0, (currenty + 1) % 10);
		}
		else {
			nextcoord = new Coordinate(currentx + 1, currenty);
		}
		if (message.equals("LOSE") || message.equals("WIN")) {
			resetRow();
			resetCoords();
		}
	}

	@Override
	public void newOpponent(String name) {
	}
//	public ArrayList<String> getLog() {
//		return hitlog;
//	}
}
