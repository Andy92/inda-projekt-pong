import java.awt.Color;
import java.awt.Font;
//import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//import javax.swing.JFrame;

/**
 * Main class for the Game.
 *
 * @author 
 * @version 
 */
public class Pong {
	private static Canvas gameField;
	private static boolean paused = false;
	private static final String GAMENAME = "Pong";

	// Positions of the bottom, top, left and right edges of the playing field.
	private static final int TOP = 50;
	private static final int BOTTOM = 400;
	private static final int LEFT = 50;
	private static final int RIGHT = 550;

	private static Paddle padLeft, padRight;
	private static Ball ball;
	
	// initial speed of ball
	private static int ySpeed = 0;
	private static int xSpeed = 3;

	// color theme
	private static Color mainColor = Color.BLACK;
	private static Color secondaryColor = Color.WHITE;
	/**
	 *
	 */
	public static void main(String[] args) {
		createGameField();
		createGameObjects();
		playGame();
	}

	private static void createGameField() {
		gameField = new Canvas(GAMENAME, 600, 500, secondaryColor);
		
		/**
		 * Action happens when specific keys are pressed.
		 * Focus is set on Jframe in class Canvas.
		 */
		gameField.addKeyListener(new KeyListener() {
			    public void keyPressed(KeyEvent e) { 
			    	// Left Paddle keys.
			    	if (e.getKeyCode() == 87) { // If W is pressed.
			    		padLeft.setySpeed(-3);		// move upwards
			    	} else if (e.getKeyCode() == 83) { // If S is pressed.
			    		padLeft.setySpeed(3);			// move down
			    	}
			    	// Right Paddle keys.
			    	if (e.getKeyCode() == 38) { // If Up-arrow is pressed.
			    		padRight.setySpeed(-3);
			    	} else if (e.getKeyCode() == 40) { // If down-arrow is pressed.
			    		padRight.setySpeed(3);
			    	}
			    	
			    	if (e.getKeyCode() == 80) { // P-key for pause
			    		if (!paused) {
			    			paused = true;
			    		} else {
			    			paused = false;
			    		}
			    	}
			    	if (e.getKeyCode() == 77) { // M-key for menu access.
			    		// TODO
			    	}
			    }

			    public void keyReleased(KeyEvent e) {
			    	// Left paddle keys.
			    	if (e.getKeyCode() == 87) {
			    		padLeft.setySpeed(0);
			    	} else if (e.getKeyCode() == 83) {
			    		padLeft.setySpeed(0);
			    	}
			    	// Right paddle keys.
			    	if (e.getKeyCode() == 38) {
			    		padRight.setySpeed(0);
			    	} else if (e.getKeyCode() == 40) {
			    		padRight.setySpeed(0);
			    	}
			    }
			    public void keyTyped(KeyEvent e) { 
			    	// Do nothing.
			    }
		});
		// TODO Add buttons
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
		int padyPos = 220; // create both pads at mid height
		int width = 20; // the width of the pads
		int height = 60; // the height of the pads
		padLeft = new Paddle(padxPos, padyPos, width, height, secondaryColor, gameField, BOTTOM);
		padRight = new Paddle(pad2xPos, padyPos, width, height, secondaryColor, gameField, BOTTOM);
		padLeft.draw();
		padRight.draw();

		// Create a ball in the mid of game field.
		int xPos = RIGHT/2 + LEFT/2;
		int yPos = BOTTOM/2 + TOP/2;
		ball = new Ball(xPos, yPos, 16, secondaryColor, BOTTOM, gameField, ySpeed, xSpeed);
		ball.draw();
	}

	/**
	 * Plays the game.
	 */
	private static void playGame() {
		while(!paused) {			// If game is paused or not.
			gameField.wait(10); //TODO adjust refresh rate
			ball.receiveY(padLeft.getYPosition(), padRight.getYPosition());
			ball.move();
			drawMidLine();
			padLeft.move();
			padRight.move();
			// Prints current position of ball and paddle for testing purposes. //TODO Remove after testing
			printTestData();
		}
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		gameField.drawString("PAUSED", RIGHT/2 + LEFT/2, BOTTOM/2 + TOP/2);
		while(paused) {
			// do nothing.
		}
		gameField.eraseString("PAUSED", RIGHT/2 + LEFT/2, BOTTOM/2 + TOP/2);
		playGame();
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
	
	public static void setPaused(boolean bool) {
		paused = bool;
	}
	
	public static boolean getPaused() {
		return paused;
	}
	
	public static void printTestData() {
		int ballX = ball.getXPosition();
		int ballY = ball.getYPosition();
		int padPos = padLeft.getYPosition();
		int pad2Pos = padRight.getYPosition();
		System.out.printf("Ball: x: %d y: %d\n Paddle1: x: 70 y: %d\n Paddle2: x: 510 y: %d\n", ballX, ballY, padPos, pad2Pos);
	}
}
