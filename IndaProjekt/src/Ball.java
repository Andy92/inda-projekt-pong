import java.awt.*;
import java.util.Random;

/**
 * Creates a ball that bounces on walls, and paddles.
 *
 * 
 * @author Pontus Nilsson & Andy Yousef
 *
 * @version 1.0
 */
public class Ball {
	private Color color;
	private int diameter;
	private int xPosition;
	private int yPosition;
	private Canvas canvas;
	private int ySpeed;                // initial downward speed
	private int xSpeed;
	private int padyPos, pad2yPos;
	private Random rand;

	/**
	 * Constructor
	 *
	 * @param xPos  the horizontal coordinate of the ball
	 * @param yPos  the vertical coordinate of the ball
	 * @param ballDiameter  the diameter (in pixels) of the ball
	 * @param ballColor  the color of the ball
	 * @param groundPos  the position of the ground (where the wall will bounce)
	 * @param drawingCanvas  the canvas to draw this ball on
	 * @param ySpeed  the vertical speed of the ball
	 * @param xSpeed  the horizontal speed of the ball
	 */
	public Ball(int xPos, int yPos, int ballDiameter, Color ballColor, int groundPos, Canvas drawingCanvas, int ySpeed, int xSpeed) {
		xPosition = xPos;
		yPosition = yPos;
		color = ballColor;
		diameter = ballDiameter;
		canvas = drawingCanvas;
		this.ySpeed = ySpeed;
		this.xSpeed = xSpeed;
		rand = new Random();
	}

	/**
	 * Draw this ball at its current position onto the canvas.
	 */
	public void draw() {
		canvas.setForegroundColor(color);
		canvas.fillCircle(xPosition, yPosition, diameter);
	}

	/**
	 * Set the default background color to the mainColor of the game.
	 * Erase this ball at its current position.
	 */
	private void erase() {
		canvas.setBackgroundColor(Pong.getMainColor());
		canvas.eraseCircle(xPosition, yPosition, diameter);

	}    

	/**
	 * Move this ball according to its position and speed and redraw.
	 * Also creates a barrier that's a square, when a ball reaches it
	 * will bounce.
	 */
	public void move() {
		// remove from canvas at the current position
		erase();

		// compute new position
		yPosition += ySpeed;
		xPosition += xSpeed;
		
		// If ball hits wall, bounce.
		checkHitTopBottom();
		checkHitLeftRight();
		
		// If ball hits paddle, bounce.
		checkHitRightPaddle();
		checkHitLeftPaddle();
		
		// draw again at new position
		draw();
	}
	
	/**
	 * Check if the ball has hit the top or bottom edge. If it has,
	 * it will bounce.
	 */
	private void checkHitTopBottom() {
		// check if it has hit the ground
		if (yPosition >= (Pong.getBottom() - diameter) && ySpeed > 0) {
			yPosition = (int)(Pong.getBottom() - diameter);
			ySpeed *= -1;
		}

		// check if it has hit the roof
		if (yPosition <= Pong.getTop()) {
			yPosition = Pong.getTop();
			ySpeed *= -1;
		}
	}
	
	/**
	 * Check if the ball has hit the left or right wall. If it has, 
	 * a point will be awarded.
	 */
	private void checkHitLeftRight() {
		// check if it has hit the right wall
		if (xPosition >= Pong.getRight() - diameter) {
			xPosition = Pong.getRight() - diameter;
			Pong.pointPlayer1();
			resetBall();
		}

		// check if it has hit the left wall
		if (xPosition <= Pong.getLeft()) {
			xPosition = Pong.getLeft();
			Pong.pointPlayer2();
			resetBall();
		}
	}
	
	/**
	 * ResetBall makes the ball blink to indicate a point. It then resets the ball to the starting position, 
	 * and sleeps for 0.8 seconds before continuing the game.
	 */
	private void resetBall() {
		blink(); 
		xPosition = Pong.getRight()/2 + Pong.getLeft()/2 - diameter/2;
		yPosition = Pong.getBottom()/2 + Pong.getTop()/2 - diameter/2;
		ySpeed = rand.nextInt(3) - 1; // Give random initial y-axis speed between -1 and 1
		xSpeed *= -1;
		draw();
		sleep(800);
	}
	
	/**
	 * Makes the ball blink a few times. Is typically called to indicate a point.
	 */
	private void blink() {
		for (int i = 0; i < 3; i++) {
			sleep(300);
			draw();
			sleep(300);
			erase();
		}		
	}
	
	/**
	 * Checks if the ball has hit the left paddle,
	 * and which side of the paddle.
	 * (depending if the ball hit bottom or top side, it will accelerate on y-axis).
	 */
	private void checkHitLeftPaddle() {
		//TODO needs more work, add top mid and bottom mid
		// Mid of the paddle.
		if ((yPosition <= (padyPos + 40)) && (yPosition >= (padyPos + 20 - diameter))
				&& (xPosition <= 90)) {
			xPosition = 90;
			xSpeed *= -1;
		} // top side of the paddle.
		else if ((yPosition <= (padyPos + 20 - diameter)) && (yPosition >= (padyPos - diameter))
				&& (xPosition <= 90)) {
			xPosition = 90;
			xSpeed *= -1;
			ySpeed -= 1;
		} // down side of the paddle.
		else if ((yPosition <= (padyPos + 60)) && (yPosition >= (padyPos + 40))
				&& (xPosition <= 90)) {
			xPosition = 90;
			xSpeed *= -1;
			ySpeed += 1;
		}
	}
	
	/**
	 * Checks if the ball has hit the right paddle,
	 * and which side of the paddle.
	 * (depending if the ball hit bottom or top side, it will accelerate on y-axis).
	 */
	private void checkHitRightPaddle() {
		//TODO needs more work, add top mid and bottom mid
		// Mid of the paddle.
		if ((yPosition <= (pad2yPos + 40)) && (yPosition >= (pad2yPos + 20 - diameter))
				&& (xPosition >= (510 - diameter))) {
			xPosition = (510 - diameter);
			xSpeed *= -1;
		} // top side of the paddle.
		else if ((yPosition <= (pad2yPos + 20 - diameter)) && (yPosition >= (pad2yPos - diameter))
				&& (xPosition >= (510 - diameter))) {
			xPosition = (510 - diameter);
			xSpeed *= -1;
			ySpeed -= 1;
		} // down side of the paddle.
		else if ((yPosition <= (pad2yPos + 60)) && (yPosition >= (pad2yPos + 40))
				&& (xPosition >= (510 - diameter))) {
			xPosition = (510 - diameter);
			xSpeed *= -1;
			ySpeed += 1;
		}
	}

	/**
	 * return the horizontal position of this ball
	 */
	public int getXPosition() {
		return xPosition;
	}

	/**
	 * return the vertical position of this ball
	 */
	public int getYPosition() {
		return yPosition;
	}

	/**
	 * ReceiveY is used by the Pong-class to send the y-coordinates of the paddles to this class..
	 */
	public void receiveY(int y, int y2) {
		padyPos = y;
		pad2yPos = y2;
	}
	
	/**
	 * Sleep makes the thread sleep for the specified time in ms.
	 */
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} 
		catch (InterruptedException e) {
			// ignoring exception at the moment
		}
	}
}
