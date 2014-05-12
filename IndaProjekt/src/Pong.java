import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Main class for the Game.
 *
 * @author	Pontus Nilsson & Andy Yousef 
 * @version 1.0
 */
public class Pong {
	private static Canvas gameField;
	private static boolean paused = false;
	private static boolean quitting = false;
	private static boolean inPauseMenu = false;
	private static boolean inMainMenu = true;
	private static boolean AI = false;		// Artificial intelligence for 1 player.

	private static final String GAMENAME = "Pong";

	// Positions of the bottom, top, left and right edges of the playing field.
	private static final int TOP = 50;
	private static final int BOTTOM = 400;
	private static final int LEFT = 50;
	private static final int RIGHT = 550;

	// Paddles, paddle height and width in pixels
	private static Paddle padLeft, padRight;
	private static final int PAD_HEIGHT = 60;
	private static final int PAD_WIDTH = 20;

	// Ball, ball diameter in pixels
	private static Ball ball;
	private static final int BALL_SIZE = 16;

	// initial speed of ball
	private static int ySpeed = 0;
	private static int xSpeed = 3;

	// color theme
	/**
	 * MainColor determines the color of the background and surroundings.
	 */
	private static final Color mainColor = Color.BLACK;
	/**
	 * SecondaryColor determines the color of the ball, paddles, lines etc. 
	 */
	private static final Color secondaryColor = Color.WHITE;

	// player points
	private static int pointsPlayer1 = 0;
	private static int pointsPlayer2 = 0;

	/**
	 * Main method for the game. Creates the gamefield, then starts the game.
	 */
	public static void main(String[] args) {
		createGameField();
		enableKeyListener();
		createGameObjects();
		initPaused();
		game();
	}

	/**
	 * Sets up and draws basic background objects for the game.
	 */
	private static void createGameField() {
		gameField = new Canvas(GAMENAME, 600, 500, mainColor);
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

	/**
	 * Enables the gameField to listen for keyboard actions.
	 * Action happens when specific keys are pressed.
	 */
	private static void enableKeyListener() {
		gameField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) { 
				// Left Paddle keys. (Player 1)
				if (e.getKeyCode() == 87) { // If W is pressed
					padLeft.setySpeed(-3);		// move upwards
				} else if (e.getKeyCode() == 83) { // If S is pressed.
					padLeft.setySpeed(3);			// move down
				}
				// Right Paddle keys. (Player 2)
				if ((e.getKeyCode() == 38) && (!AI)) { // If Up-arrow is pressed and AI not enabled.
					padRight.setySpeed(-3);
				} else if ((e.getKeyCode() == 40) && (!AI)) { // If down-arrow is pressed.
					padRight.setySpeed(3);
				}

				if (e.getKeyCode() == 80) { // P-key for pause
					if (!paused && !quitting && !inPauseMenu && !inMainMenu) {
						paused = true;
					} else {
						paused = false;
					}
				}

				if (e.getKeyCode() == 81) { // Q-key for quit menu
					if (!quitting && !paused && !inPauseMenu && !inMainMenu) {
						quitting = true;
					}
				}
				if (quitting) { // Y/N-key for quit menu
					if (e.getKeyCode() == 89) {
						quit();
					} else if (e.getKeyCode() == 78)  {
						quitting = false;
					}
				}

				if (e.getKeyCode() == 77) { // M-key for menu access.
					if (!inPauseMenu && !paused && !quitting && !inMainMenu) {
						inPauseMenu = true;
					}
				}
				if (inPauseMenu) {
					if (e.getKeyCode() == 67) {
						inPauseMenu = false;
					} else if (e.getKeyCode() == 50) { // "2" key - for 2 player game
						AI = false;
						restartGame();
					} else if (e.getKeyCode() == 49) { // "1" key - for 1 player game
						AI = true;
						restartGame();
					}
				}

				if (inMainMenu) {
					 if (e.getKeyCode() == 50) { // "2" key - for 2 player game
						AI = false;
						restartGame();
						inMainMenu = false;
					} else if (e.getKeyCode() == 49) { // "1" key - for 1 player game
						AI = true;
						restartGame();
						inMainMenu = false;
					}
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
				if ((e.getKeyCode() == 38) && (!AI)) {
					padRight.setySpeed(0);
				} else if ((e.getKeyCode() == 40) && (!AI)) {
					padRight.setySpeed(0);
				}
			}
			public void keyTyped(KeyEvent e) { 
				// Do nothing.
			}
		});
	}

	/**
	 * DrawMidLine draws a vertical line through the mid of the gamefield.
	 */
	private static void drawMidLine() {
		gameField.setForegroundColor(secondaryColor);
		gameField.drawLine(300, TOP, 300, BOTTOM);
	}

	/**
	 * Creates objects that are used for playing the game itself.
	 */
	private static void createGameObjects() {
		createPaddles();
		createBall();
	}

	/**
	 * InitPaused loads the font and prints an invisible 'paused'-sign to decrease lag in the game.
	 */
	private static void initPaused() {
		// Initialize 'PAUSED'-text to decrease lag when pausing
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		gameField.setForegroundColor(mainColor);
		gameField.drawString("PAUSED", 2*RIGHT, 2*BOTTOM);
	}

	/**
	 * Draws paddles for the players to control.
	 */
	public static void createPaddles() {
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
	}

	/**
	 * Creates and draws a ball object.
	 */
	public static void createBall() {
		// Create a ball in the mid of game field.
		int xPos = (RIGHT + LEFT - BALL_SIZE)/2;
		int yPos = (BOTTOM + TOP - BALL_SIZE)/2;
		ball = new Ball(xPos, yPos, BALL_SIZE, secondaryColor, BOTTOM, gameField, ySpeed, xSpeed);
		ball.draw();
	}

	/**
	 * Plays the game, organizing the methods to be called at the right time.
	 */
	private static void game() {
		while(true) {
			gameField.wait(1000);
			while(!paused && !quitting && !inPauseMenu && !inMainMenu) {	// While the game is not paused in any way.
				play();
			}
			if (paused) {
				pause();
				while(paused) {
					// do nothing.
				}
				unPause();
			}
			if (quitting) {
				quitMenu();
				while (quitting) {
					// do nothing
				}
				dontQuit();
			}
			if (inPauseMenu) {
				showMenu();
				while (inPauseMenu) {
					// do nothing
				}
				closeMenu();
			}
			if (inMainMenu) {
				showMainMenu();
				while (inMainMenu) {
					// do nothing
				}
				closeMainMenu();
			}
		}
	}

	/**
	 * Is looped from the game(), makes the ball and paddles move.
	 */
	public static void play() {
		gameField.wait(10); 
		ball.receiveY(padLeft.getYPosition(), padRight.getYPosition());
		ball.move();
		drawMidLine();
		padLeft.move();
		padRight.move();
		playAI();
		// Prints current position of ball and paddle for testing purposes. //TODO Remove after testing
		printTestData();
	}

	/**
	 * This simple-minded AI is only taking the balls y position into account, and moves after it.
	 * Therefore is this AI considered to be on level medium.
	 */
	private static void playAI() {
		if (AI) {
			if (ball.getYPosition() >= padRight.getYPosition()) {
				padRight.setySpeed(3);
			}
			if (ball.getYPosition() <= padRight.getYPosition()) {
				padRight.setySpeed(-3);
			}
		}
	}

	/**
	 * Pauses the game. Also creates a sign in the middle of the gamefield,
	 * that indicates the game is paused.
	 */
	public static void pause() {
		gameField.setForegroundColor(secondaryColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 180/2, BOTTOM/2 + TOP/2 - 40, 180, 80);
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 176/2, BOTTOM/2 + TOP/2 - 38, 176, 76);
		gameField.setForegroundColor(secondaryColor);
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 40));

		gameField.drawString("PAUSED", RIGHT/2 + LEFT/2 - 155/2, BOTTOM/2 + TOP/2 + 52/4);  // width is 155, height is 52
	}

	/**
	 * Unpauses the game. Redraws the gamefield for continued play.
	 */
	public static void unPause() {
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 180/2, BOTTOM/2 + TOP/2 - 40, 180, 80);
		gameField.setForegroundColor(secondaryColor);
		drawMidLine();
		ball.draw();
	}

	/**
	 * Increases points for player 1 (left player) with 1.
	 */
	public static void pointPlayer1() {
		pointsPlayer1++;
		drawPoints();
	}

	/**
	 * Increases points for player 2/AI (right player) with 1.
	 */
	public static void pointPlayer2() {
		pointsPlayer2++;
		drawPoints();
	}

	/**
	 * Visualizes the points for the players to see. Should only be needed to call when points are changed.
	 */
	public static void drawPoints() {
		// Improve visual appearance when points > 9 or points > 99
		int offset1 = 0;
		int offset2 = 0;
		if (pointsPlayer1 > 9) {
			offset1 += 20;
		}
		if (pointsPlayer1 > 99) {
			offset1 += 10;
		}
		if (pointsPlayer2 > 9) {
			offset2 += 20;
		}
		if (pointsPlayer2 > 99) {
			offset2 += 20;
		}
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 60));
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(LEFT, BOTTOM + 1 , (RIGHT + LEFT)/2 - LEFT - 80, 100);
		gameField.fillRectangle((RIGHT + LEFT)/2 + 80, BOTTOM + 1 , (RIGHT + LEFT)/2 - 80, 100);
		gameField.setForegroundColor(secondaryColor);
		gameField.drawString(Integer.toString(pointsPlayer1), LEFT + 30 - offset1, BOTTOM + 60);
		gameField.drawString(Integer.toString(pointsPlayer2), RIGHT - 60 - offset2, BOTTOM + 60);
	}

	/**
	 * Draws the help-key controls on the gameField.
	 */
	public static void drawControls() {
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		gameField.setForegroundColor(secondaryColor);
		gameField.drawString("m - menu", (RIGHT - LEFT)/2 - 10, BOTTOM + 20);
		gameField.drawString("p - pause", (RIGHT - LEFT)/2 - 10, BOTTOM + 40);
		gameField.drawString("q - quit", (RIGHT - LEFT)/2 - 10, BOTTOM + 60);
	}

	/**
	 * Displays a small square menu on mid screen of the game and asks player if hen is sure about quitting.
	 */
	public static void quitMenu() {
		gameField.setForegroundColor(secondaryColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 270/2, BOTTOM/2 + TOP/2 - 40, 270, 80);
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 266/2, BOTTOM/2 + TOP/2 - 38, 266, 76);
		gameField.setForegroundColor(secondaryColor);
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		gameField.drawString("Do You Really Want To QUIT?", RIGHT/2 + LEFT/2 - 251/2, BOTTOM/2 + TOP/2 - 15);  // width is 251, height is 27
		gameField.drawString("Yes - Y / No - N", RIGHT/2 + LEFT/2 - 133/2, BOTTOM/2 + TOP/2 + 25); // width is 133
	}

	/**
	 * Aborts the quit menu and continues the game play.
	 */
	public static void dontQuit() {
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 270/2, BOTTOM/2 + TOP/2 - 40, 270, 80);
		gameField.setForegroundColor(secondaryColor);
		drawMidLine();
		ball.draw();
	}

	/**
	 * Displays the menu frame with options to select.
	 */
	public static void showMenu() {
		//TODO needs improved appearance
		gameField.setForegroundColor(secondaryColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 260/2, BOTTOM/2 + TOP/2 - 40, 260, 120);
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 256/2, BOTTOM/2 + TOP/2 - 38, 256, 116);
		gameField.setForegroundColor(secondaryColor);
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		gameField.drawString("Continue - press C", RIGHT/2 + LEFT/2 - 251/2, BOTTOM/2 + TOP/2 - 20);  // width is X, height is 27
		gameField.drawString("1 Player - press 1", RIGHT/2 + LEFT/2 - 251/2, BOTTOM/2 + TOP/2 + 20);  // width is X, height is 27
		gameField.drawString("2 Players - press 2", RIGHT/2 + LEFT/2 - 251/2, BOTTOM/2 + TOP/2 + 60);  // width is X, height is 27
	}

	/**
	 * "closes" the menu frame and redraws the gamefield.
	 */
	public static void closeMenu() {
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 260/2, BOTTOM/2 + TOP/2 - 40, 260, 120);
		gameField.setForegroundColor(secondaryColor);
		drawMidLine();
		ball.draw();
	}

	public static void showMainMenu() {
		gameField.setForegroundColor(secondaryColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 260/2, BOTTOM/2 + TOP/2 - 40, 260, 120);
		gameField.setForegroundColor(mainColor);
		gameField.fillRectangle(RIGHT/2 + LEFT/2 - 256/2, BOTTOM/2 + TOP/2 - 38, 256, 116);
		gameField.setForegroundColor(secondaryColor);
		gameField.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		gameField.drawString("1 Player - press 1", RIGHT/2 + LEFT/2 - 200/2, BOTTOM/2 + TOP/2);  // width is X, height is 27
		gameField.drawString("2 Players - press 2", RIGHT/2 + LEFT/2 - 200/2, BOTTOM/2 + TOP/2 + 50);  // width is X, height is 27
	}

	public static void closeMainMenu() {
		closeMenu();
		inMainMenu = false;
	}

	/**
	 * Resets the game by redrawing it's components again and reseting points.
	 */
	public static void restartGame() {
		resetPoints();
		drawNewGameField();
		drawNewGameObjects();
		inPauseMenu = false;
	}

	/**
	 * Resets both players points to 0.
	 */
	public static void resetPoints() {
		pointsPlayer1 = 0;
		pointsPlayer2 = 0;
	}

	/**
	 * Draws a new gameField.
	 */
	public static void drawNewGameField() {
		// draw gamefield
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

	/**
	 * Draws the objects.
	 */
	public static void drawNewGameObjects() {
		createPaddles();
		createBall();
	}

	/**
	 * Terminates the game.
	 */
	public static void quit() {
		System.exit(0);
	}

	/**
	 * Returns the value of the top edge of the gamefield box.
	 */
	public static int getTop() {
		return TOP;
	}

	/**
	 * Returns the value of the bottom edge of the gamefield box.
	 */
	public static int getBottom() {
		return BOTTOM;
	}

	/**
	 * Returns the value of the left edge of the gamefield box.
	 */
	public static int getLeft() {
		return LEFT;
	}

	/**
	 * Returns the value of the right edge of the gamefield box.
	 */
	public static int getRight() {
		return RIGHT;
	}

	/**
	 * Returns the mainColor (the color that is used for the background etc).
	 */
	public static Color getMainColor() {
		return mainColor;
	}

	/**
	 * Returns the secondaryColor (the color that is used for ball, paddles, lines etc).
	 */
	public static Color getSecondaryColor() {
		return secondaryColor;
	}

	/**
	 * Returns true if the game is paused, otherwise false.
	 */
	public static boolean getPaused() {
		return paused;
	}

	/**
	 * Sets the game to be paused or unpaused.
	 */
	public static void setPaused(boolean bool) {
		paused = bool;
	}

	/**
	 * Prints data used for testing the game.
	 */
	public static void printTestData() {
		int ballX = ball.getXPosition();
		int ballY = ball.getYPosition();
		int padPos = padLeft.getYPosition();
		int pad2Pos = padRight.getYPosition();
		System.out.printf("Ball: x: %d y: %d\n Paddle1: x: 70 y: %d\n Paddle2: x: 510 y: %d\n", ballX, ballY, padPos, pad2Pos);
	}
}
