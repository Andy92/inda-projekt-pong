
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {
	public Game(String title) { super(title); }

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Game("Test Game"));
		app.start();
	}

	@Override
	public void render(GameContainer container, Graphics g)	throws SlickException {
		g.drawString("Hello, World!", 0, 100);
	}

	@Override
	public void init(GameContainer container) throws SlickException {

	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {

	}
}