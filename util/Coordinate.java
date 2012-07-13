package util;

public class Coordinate {
	private int x;
	private int y;
	
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public String toString() {
		return Integer.toString(x) + "," + Integer.toString(y);
	}
}
