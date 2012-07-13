package util;

public class Ship {
	private char shipHandle;
	private int height;
	private int width;
	
	public Ship(char shipHandle, int height, int width){
		this.shipHandle = shipHandle;
		this.height = height;
		this.width = width;
	}
	
	public char getShipHandle(){
		return shipHandle;
	}
	
	public int getShipHeight(){
		return height;
	}
	
	public int getShipWidth(){
		return width;
	}
}
