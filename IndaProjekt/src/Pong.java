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
	private static boolean quitting = false;
	private static final String GAMENAME = "Pong";

	// Positions of the bottom, top, left and right edges of the playing field.
	private static final int TOP = 50;
	private static final int BOTTOM = 400;
	private static final int LEFT = 50;
	private static final int RIGHT = 550;

	private static Paddle padLeft, padRight;
	private static final int PAD_HEIGHT = 60;
	private static final int PAD_WIDTH = 20;

	private static Ball ball;
	private static final int BALL_SIZE = 16;

	// initial speed of ball
	private static int ySpeed = 0;
	private static int xSpeed = 3;

	// color theme
	private static Color mainColor = Color.BLACK;
	private static Color secondaryColor = Color.WHITE;
	
	// player points
	private static int pointsPlayer1 = 0;
	private static int pointsPlayer2 = 0;
	/**
	 *
	 */
	public static void main(String[] args) {
		createGameField();
		createGameObjects();
		game();
	}

	private static void createGameField() {
		gameField = new Canvas(GAMENAME, 600, 500, mainColor);
		
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
				
				if (e.getKeyCode() == 81) {
					quit(); //TODO "Are you sure"
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
		gameField.setForegroundColor(secondaryColor);
		gameField.fillRectangle(LEFT - 1, TOP - 1, RIGHT - LEFT + 2, BOTTOM - TOP + 2);
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(LEFT, TOP, RIGHT - LEFT, BOTTOM - TOP);
		drawMidLine();
		
		// draw player points
		drawPoints();
		
		//draw controls
		drawControls();
		
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
		int padxPos = LEFT + 20; // create pad1 to the left
		int pad2xPos = RIGHT - PAD_WIDTH - 20; // create pad2 to the right
		int padyPos = (BOTTOM + TOP - PAD_HEIGHT)/2; // create both pads at mid height
		int width = PAD_WIDTH; // the width of the pads
		int height = PAD_HEIGHT; // the height of the pads
		padLeft = new Paddle(padxPos, padyPos, width, height, secondaryColor, gameField, BOTTOM);
		padRight = new Paddle(pad2xPos, padyPos, width, height, secondaryColor, gameField, BOTTOM);
		padLeft.draw();
		padRight.draw();

		// Create a ball in the mid of game field.
		int xPos = (RIGHT + LEFT - BALL_SIZE)/2;
		int yPos = (BOTTOM + TOP - BALL_SIZE)/2;
		ball = new Ball(xPos, yPos, BALL_SIZE, secondaryColor, BOTTOM, gameField, ySpeed, xSpeed);
		ball.draw();

		// Initialize 'PAUSED'-text to decrease lag when pausing
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		gameField.setForegroundColor(mainColor);
		gameField.drawString("PAUSED", 2*RIGHT, 2*BOTTOM);
	}

	/**
	 * Plays the game.
	 */
	private static void game() {
		while(!quitting) {
			gameField.wait(1000);
			while(!paused) {			// If game is paused or not.
				play();
			}		
			pause();
			while(paused) {
				// do nothing.
			}
			unPause();
		}
		//TODO EXIT GAME
	}

	public static void play() {
		gameField.wait(10); //TODO adjust refresh rate
		ball.receiveY(padLeft.getYPosition(), padRight.getYPosition());
		ball.move();
		drawMidLine();
		padLeft.move();
		padRight.move();
		// Prints current position of ball and paddle for testing purposes. //TODO Remove after testing
		printTestData();
	}

	public static void pause() {
		gameField.setForegroundColor(secondaryColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 180/2, BOTTOM/2 + TOP/2 - 40, 180, 80);
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 176/2, BOTTOM/2 + TOP/2 - 38, 176, 76);
		gameField.setForegroundColor(secondaryColor);
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		
		gameField.drawString("PAUSED", RIGHT/2 + LEFT/2 - 155/2, BOTTOM/2 + TOP/2);  // width is 155
	}

	public static void unPause() {
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 180/2, BOTTOM/2 + TOP/2 - 40, 180, 80);
		gameField.setForegroundColor(secondaryColor);
		drawMidLine();
		ball.draw();
	}
	
	public static void pointPlayer1() {
		pointsPlayer1++;
		drawPoints();
	}
	
	public static void pointPlayer2() {
		pointsPlayer2++;
		drawPoints();
	}
	
	public static void drawPoints() {
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 60));
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(LEFT, BOTTOM + 1 , (RIGHT + LEFT)/2 - LEFT - 80, 100);
		gameField.fillRectangle((RIGHT + LEFT)/2 + 80, BOTTOM + 1 , (RIGHT + LEFT)/2 - 80, 100);
		gameField.setForegroundColor(secondaryColor);
		gameField.drawString(Integer.toString(pointsPlayer1), LEFT + 30, BOTTOM + 60);
		gameField.drawString(Integer.toString(pointsPlayer2), RIGHT - 60, BOTTOM + 60);
	}
	
	public static void drawControls() {
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		gameField.setForegroundColor(secondaryColor);
		gameField.drawString("m - menu (soon)", (RIGHT - LEFT)/2 - 10, BOTTOM + 20);
		gameField.drawString("p - pause", (RIGHT - LEFT)/2 - 10, BOTTOM + 40);
		gameField.drawString("q - quit", (RIGHT - LEFT)/2 - 10, BOTTOM + 60);
	}
	
	public static void quit() {
		System.exit(0);
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
