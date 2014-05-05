import java.awt.Color;
//import java.awt.Shape;

/**
 * Main class for the Game.
 *
 * @author 
 * @version 
 */
public class Pong {
	private static Canvas gameField;
	private static boolean paused = false;
	private static String GAMENAME = "Pong";

	// Positions of the bottom, top, left and right edges of the playing field.
	private static final int TOP = 50;
	private static final int BOTTOM = 400;
	private static final int LEFT = 50;
	private static final int RIGHT = 550;

	private static Paddle padLeft, padRight;
	private static Ball ball;

	// color theme
	private static Color mainColor = Color.BLACK;
	private static Color secondaryColor = Color.WHITE;
	
	/**
	 * Create a BallDemo object. Creates a fresh canvas and makes it visible.
	 */
	public static void main(String[] args) {
		createGameField();
		createGameObjects();
		playGame();
	}

	private static void createGameField() {
		gameField = new Canvas(GAMENAME, 600, 500, secondaryColor);

		gameField.setVisible(true);

		// draw the game
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(LEFT, TOP, RIGHT - LEFT, BOTTOM - TOP);
		drawMidLine();
	}
	
	private static void drawMidLine() {
		gameField.setForegroundColor(secondaryColor);
		gameField.drawLine(300, TOP, 300, BOTTOM);
	}

	/**
	 * 
	 */
	private static void createGameObjects() {
		// Create 2 paddle objects.
		int padxPos = 70; // create pad1 to the left
		int pad2xPos = 510; // create pad2 to the right
		int padyPos = 250; // create both pads at mid height
		int width = 20; // the width of the pads
		int height = 60; // the height of the pads
		padLeft = new Paddle(padxPos, padyPos, width, height, secondaryColor, gameField, BOTTOM);
		padRight = new Paddle(pad2xPos, padyPos, width, height, secondaryColor, gameField, BOTTOM);
		padLeft.draw();
		padRight.draw();

		// Create a ball in the mid of game field.
		int xPos = 150;
		int yPos = 250;
		ball = new Ball(xPos, yPos, 16, secondaryColor, BOTTOM, gameField);
		ball.draw();
	}

	private static void playGame() {
		// Pause or unpause game.
		// if (onkeypress)
		// paused = true;

		// make the ball move.
		while(!paused) {			// If game is paused of not.
			gameField.wait(50); //TODO increase refresh rate
			ball.receiveY(padLeft.getYPosition(), padRight.getYPosition());
			ball.move();
			drawMidLine();
			padLeft.move();
			padRight.move();
		}
	}
	
	public static int getTop() {
		return TOP;
	}
	
	public static int getBottom() {
		return BOTTOM;
	}
	
	public static int getLeft() {
		return LEFT;
	}
	
	public static int getRight() {
		return RIGHT;
	}
	
	public static Color getMainColor() {
		return mainColor;
	}
	
	public static Color getSecondaryColor() {
		return secondaryColor;
	}
}
