import java.awt.Color;
import java.awt.Shape;

/**
 * Main class for the Game.
 *
 * @author 
 * @version 
 */

public class Pong {
	private static Canvas gameField;
	private static boolean paused =  false;
	/**
	 * Create a BallDemo object. Creates a fresh canvas and makes it visible.
	 */
	public static void main(String[] args) {
		gameField = new Canvas("Plan", 600, 500, Color.white);
		ballPhy();
	}

	/**
	 * 
	 */
	public static void ballPhy() {
		int bottom = 400;   // position of the ground line
		int top = 50;		// position of the top line

		gameField.setVisible(true);

		// draw the game
		gameField.fillRectangle(50, 50, 500, 350);
		gameField.drawLine(150, 50, 300, 300);
		

		// Create 2 paddle objects.
		int padxPos = 70;
		int pad2xPos = 510;
		int padyPos = 250;
		int width = 20;
		int height = 3 * width;
		Paddle pad1 = new Paddle(padxPos, padyPos, width, height, Color.white, gameField, bottom);
		Paddle pad2 = new Paddle(pad2xPos, padyPos, width, height, Color.white, gameField, bottom);
		pad1.draw();
		pad2.draw();

		// Create a ball in the mid of game field.
		int xPos = 150;
		int yPos = 250;
		Ball ball = new Ball(xPos, yPos, 16, Color.white, bottom, gameField);
		ball.draw();

		// Pause or unpause game.
		// if (onkeypress)
		// paused = true;

		// make the ball move.
		while(!paused) {			// If game is paused of not.
			gameField.wait(50);
			ball.receiveY(pad1.getYPosition(), pad2.getYPosition());
			ball.move();
			pad1.move();
			pad2.move();
		}
	}
}
