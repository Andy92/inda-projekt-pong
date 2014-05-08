import java.awt.*;
//import java.awt.geom.*;

/**
 * 
 */
public class Paddle {
	
    private Color color;
    private int width;
    private int height;
    private int xPosition;
    private int yPosition;
    private Canvas canvas;
    public int ySpeed;                // initial downward speed
    private int ground;

    /**
     * Constructor
     */
    public Paddle(int xPos, int yPos, int wid, int he, Color ballColor, Canvas drawingCanvas, int bottom) {
        xPosition = xPos;
        yPosition = yPos;
        color = ballColor;
        width = wid;
        height = he;
        canvas = drawingCanvas;
        ySpeed = 0;
        ground = bottom;
    }

    /**
     * Draw this at its current position onto the canvas.
     **/
    public void draw() {
        canvas.setForegroundColor(color);
        canvas.fillRectangle(xPosition, yPosition, width, height);
    }

    /**
     * Set the default background color black.
     * Erase this paddle at its current position.
     **/
    public void erase() {
        canvas.setBackgroundColor(Color.BLACK);
        canvas.eraseRectangle(xPosition, yPosition, width, height);
    }    

    /**
     * If key pressed, moves paddle up or down on x axis.
     **/
    public void move() {
        // remove from canvas at the current position
        erase();

        // compute new position
        yPosition += ySpeed;
        
        if (yPosition >= (ground - (height)) && ySpeed > 0) {
            yPosition = (int)(ground - (height));
        }
        
        if (yPosition <= 50) {
            yPosition = 50;
        }

        // draw again at new position
        draw();
    }    
    
    /**
     * Return the height of the paddle.
     */
    public int getHeight() {
    	return height;
    }

    /**
     * return the horizontal position
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * return the vertical position
     */
    public int getYPosition() {
        return yPosition;
    }
}
