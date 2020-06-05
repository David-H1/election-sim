package election.graphic;

import java.awt.Color;
/*
 * This class stores the location, color, and name of candidates in a
 * Yee Picture.
 * 
 */
public class Candidate2D {
	int xPosition;
	int yPosition;
	Color candColor;
	String name;
	
	public Candidate2D(int xPos, int yPos, Color col, String candName) {
		xPosition = xPos;
		yPosition = yPos;
		candColor = col;
		name = candName;
	}
	
	public int getXPosition() {
		return xPosition;
	}
	
	public int getYPosition() {
		return yPosition;
	}
	
	public Color getColor() {
		return candColor;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name + ": (" + xPosition + ", " + yPosition + ")";
	}
}
