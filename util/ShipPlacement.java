package util;

public class ShipPlacement {
	Ship ship;
	Coordinate beginning;
	Coordinate end;
	
	public ShipPlacement(Ship ship, Coordinate beginning, Coordinate end){
		this.ship = ship;
		this.beginning = beginning;
		this.end = end;
	}
	
	public Coordinate getBeginning(){
		return beginning;
	}
	
	public Coordinate getEnd(){
		return end;
	}
	
	public Ship getShip(){
		return ship;
	}
	
	public String toString() {
		return beginning.toString() + "," + end.toString();
	}
}
