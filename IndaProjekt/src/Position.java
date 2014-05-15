/**
 * This class handles the positioning of the class paddle and ball.
 * Can also change their respective positions.
 */
public class Position {
	private int xPosition, yPosition;
	
	public Position(int xPos, int yPos) {
		xPosition = xPos;
		yPosition = yPos;
	}
	
	public int getYPos() {
		return yPosition;
	}
	
	public int getXPos() {
		return xPosition;
	}
	
	public void setYPos(int yPos) {
		yPosition = yPos;
	}
	
	public void setXPos(int xPos) {
		xPosition = xPos;
	}
	
	public void setPos(int xPos, int yPos) {
		xPosition = xPos;
		yPosition = yPos;
	}
	
	public void changeXPos(int change) {
		xPosition += change;
	}
	
	public void changeYPos(int change) {
		yPosition += change;
	}
}
