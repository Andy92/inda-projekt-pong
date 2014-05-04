import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

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
	private final int groundPosition;      // y position of ground
	private Canvas canvas;
	private int ySpeed;                // initial downward speed
	private int xSpeed;
	private Pong pong = new Pong();

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
	public Ball(int xPos, int yPos, int ballDiameter, Color ballColor,
			int groundPos, Canvas drawingCanvas)
	{
		xPosition = xPos;
		yPosition = yPos;
		color = ballColor;
		diameter = ballDiameter;
		groundPosition = groundPos;
		canvas = drawingCanvas;
		ySpeed = 10;
		xSpeed = 20;
	}

	/**
	 * Draw this ball at its current position onto the canvas.
	 **/
	public void draw()
	{
		canvas.setForegroundColor(color);
		canvas.fillCircle(xPosition, yPosition, diameter);
	}

	/**
	 * Set the default background color black.
	 * Erase this ball at its current position.
	 **/
	public void erase()
	{
		canvas.setBackgroundColor(Color.BLACK);
		canvas.eraseCircle(xPosition, yPosition, diameter);

	}    

	/**
	 * Move this ball according to its position and speed and redraw.
	 * Also creates a barrier that's a square, when a ball reaches it
	 * will bounce.
	 **/
	public void move()
	{
		// remove from canvas at the current position
		erase();

		// compute new position
		yPosition += ySpeed;
		xPosition += xSpeed;

		// check if it has hit the ground
		if (yPosition >= (groundPosition - diameter) && ySpeed > 0) {
			yPosition = (int)(groundPosition - diameter);
			ySpeed = -ySpeed;
		}

		if (yPosition <= 50) {
			yPosition = 50;
			ySpeed *= -1;
		}

		if (xPosition >= 550 - diameter) {
			xPosition = 550 - diameter;
			xSpeed *= -1;
		}

		if (xPosition <= 50) {
			xPosition = 50;
			xSpeed *= -1;
		}

		// If ball hits paddle, bounce.
		//if (pong.pad1.xPosition()) {
		// TODO
		//}

		// draw again at new position
		draw();
	}

	/**
	 * return the horizontal position of this ball
	 */
	public int getXPosition()
	{
		return xPosition;
	}

	/**
	 * return the vertical position of this ball
	 */
	public int getYPosition()
	{
		return yPosition;
	}
}
