import java.awt.*;
//import java.awt.geom.*;
//import java.util.Random;

/**
 * Creates a ball that bounces on walls.
 *
 * 
 * @author 
 *
 * @version 
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
	}

	/**
	 * Draw this ball at its current position onto the canvas.
	 **/
	public void draw() {
		canvas.setForegroundColor(color);
		canvas.fillCircle(xPosition, yPosition, diameter);
	}

	/**
	 * Set the default background color black.
	 * Erase this ball at its current position.
	 **/
	private void erase() {
		canvas.setBackgroundColor(Color.BLACK);
		canvas.eraseCircle(xPosition, yPosition, diameter);

	}    

	/**
	 * Move this ball according to its position and speed and redraw.
	 * Also creates a barrier that's a square, when a ball reaches it
	 * will bounce.
	 **/
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
	
	private void checkHitLeftRight() {
		//TODO points on hit
		// check if it has hit the right wall
		if (xPosition >= Pong.getRight() - diameter) {
			xPosition = Pong.getRight() - diameter;
			xSpeed *= -1;
		}

		// check if it has hit the left wall
		if (xPosition <= Pong.getLeft()) {
			xPosition = Pong.getLeft();
			xSpeed *= -1;
		}
	}
	
	/**
	 * Checks if the ball has hit the left paddle,
	 * and which side of the paddle.
	 * (depending if the ball hit bottom or top side, it will accelerate on y-axis).
	 */
	private void checkHitLeftPaddle() {
		// Mid of the paddle.
		if ((yPosition <= (padyPos + 10)) && (yPosition >= (padyPos - 10))
				&& (xPosition <= (80 + diameter)) && (xPosition >= (80 + (diameter/2)))) {
			xPosition = (80 + diameter);
			xSpeed *= -1;
		} // top side of the paddle.
		else if ((yPosition <= (padyPos - 10)) && (yPosition >= (padyPos - 30))
				&& (xPosition <= (80 + diameter)) && (xPosition >= (80 + (diameter/2)))) {
			xPosition = (80 + diameter);
			xSpeed *= -1;
			ySpeed -= 1;
		} // down side of the paddle.
		else if ((yPosition <= (padyPos + 30)) && (yPosition >= (padyPos + 10))
				&& (xPosition <= (80 + diameter)) && (xPosition >= (80 + (diameter/2)))) {
			xPosition = (80 + diameter);
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
		// Mid of the paddle.
		if ((yPosition <= (pad2yPos + 10)) && (yPosition >= (pad2yPos - 10))
				&& (xPosition >= (500 - diameter)) && (xPosition <= (500 - (diameter/2)))) {
			xPosition = (500 - diameter);
			xSpeed *= -1;
		} // top side of the paddle.
		else if ((yPosition <= (pad2yPos - 10)) && (yPosition >= (pad2yPos - 30))
				&& (xPosition >= (500 - diameter)) && (xPosition <= (500 - (diameter/2)))) {
			xPosition = (500 - diameter);
			xSpeed *= -1;
			ySpeed -= 1;
		} // down side of the paddle.
		else if ((yPosition <= (padyPos + 30)) && (yPosition >= (padyPos + 10))
				&& (xPosition >= (500 - diameter)) && (xPosition <= (500 - (diameter/2)))) {
			xPosition = (500 - diameter);
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

	public void receiveY(int y, int y2) {
		padyPos = y;
		pad2yPos = y2;
	}
}
