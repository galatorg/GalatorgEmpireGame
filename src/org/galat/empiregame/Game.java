package org.galat.empiregame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import org.galat.empiregame.entities.Player;
import org.galat.empiregame.entities.PlayerMP;
import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.Level;
import org.galat.empiregame.net.GameClient;
import org.galat.empiregame.net.GameServer;
import org.galat.empiregame.net.packet.Packet00Login;

/*****************************************************************************\
 *                                                                           *
 * Game class                                                                *
 *                                                                           *
 * Handles the main game loop.                                               *
 *                                                                           *
\*****************************************************************************/

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 400;
	public static final int HEIGHT = WIDTH / 4 * 3; // divide by the aspect ratio of the game
	public static final int SCALE = 2; // scale of the tiles in the game
	public static final String NAME = "The Galatorg Empire";
	public static final Dimension DIMENSIONS = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
	public JFrame frame;
	public static Game game;
	
	private Thread thread;
	
	public boolean running = false;
	public int tickCount = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colors = new int[6*6*6];
	
	private Screen screen;
	public InputHandler input;
	public WindowHandler windowHandler;
	public Level level;
	public Player player;
	
	public GameClient socketClient;
	public GameServer socketServer;
	
	public boolean debug = true;
	public boolean isApplet = false;
	
	public Game() {	}
	
	// initialize the game
	public void init()
	{
		game = this;
		int index = 0;
		for (int r = 0; r < 6; r++) // 6 levels of red
		{
			for (int g = 0; g < 6; g++) // 6 levels of green
			{
				for (int b = 0; b < 6; b++) // 6 levels of blue 
				{
					int rr = (r * 255/5); // 6 levels of red
					int gg = (g * 255/5); // 6 levels of green
					int bb = (b * 255/5); // 6 levels of blue
					
					colors[index++] = rr<<16 | gg<<8 | bb; // set 0xRGB, 8 bits each, to reference each color, 216 colors referenced
				}
			}
		}
		
		screen = new Screen(WIDTH, HEIGHT);
		input = new InputHandler(this);
		windowHandler = new WindowHandler(this);
		level = new Level("/levels/water_level.png", SpriteSheet.defaultTiles);
		//player = new PlayerMP(level, 32, 32, input, JOptionPane.showInputDialog(this, "Please enter a username"), null, -1);
		player = new PlayerMP(level, 32, 32, input, "X", SpriteSheet.defaultPlayer, null, -1);
		level.addEntity(player);
		if (!isApplet)
		{
			Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
			if (socketServer != null)
			{
				socketServer.addConnection((PlayerMP)player, loginPacket);
			}
			loginPacket.writeData(socketClient);
		}
	}
	
	// function to start the game
	public synchronized void start()
	{
		running = true;
		thread = new Thread(this, NAME+"_main");
		thread.start();
		
		if (!isApplet)
		{
			/* TODO: Remove when server functionality added
			if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
				socketServer = new GameServer(this);
				socketServer.start();
			}*/
			
			socketClient = new GameClient(this, "localhost");
			socketClient.start();
		}
	}
	
	// function to stop the game
	public synchronized void stop()
	{
		running = false;
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	// function to make the game clock go
	public void run()
	{
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / 60.0; // number of nanoseconds in a second divided by game ticks per second desired
		
		int ticks = 0; // tick counter
		int frames = 0; // frame counter

		long lastTimer = System.currentTimeMillis();
		double delta = 0; // number of ticks that have passed, keeps track of partial ticks
		
		init(); // initialize the game environment
		
		// main game loop
		while(running)
		{
			long now = System.nanoTime(); // get the time in nanoseconds
			delta += (now - lastTime) / nsPerTick; // increment the delta by the portion of a tick that has passed
			lastTime = now;
			boolean shouldRender = true; // can be used to stop rendering
			
			while(delta >= 1) // while at least one tick has passed
			{
				ticks++; // increment tick counter
				tick(); // call tick to process updates on everything
				delta--; // decrement the number of ticks that needs to be processed
				shouldRender = true; // if the game clock is running it should be rendered
			}
	/*		
			try
			{
				Thread.sleep(2);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}	
*/
			if (shouldRender)
			{
				frames++; // increment frames (this second)
				render();
			}
			
			if (System.currentTimeMillis() - lastTimer >= 1000) // at least 1 second has passed
			{
				lastTimer += 1000; // move up a 1000 milliseconds to process this section again
				if (debug) frame.setTitle("The Galatorg Empire | " + ticks + " ticks, " + frames + " FPS " + tickCount + " total ticks"); // update game title with tick and FPS info if in debug
				frames = 0; // reset the frames counter to keep track of FPS
				ticks = 0; // ~60 ticks should have passed, reset the counter
			}
		}
	}
	
	// the game clock has ticked, perform updates on everything
	public void tick()
	{
		tickCount++; // keeps track of the total gametime that has passed since it has started
		level.tick(); // tell everything in the level to update because the clock has ticked
	}

	// render the game
	public void render()
	{
		BufferStrategy bs = getBufferStrategy(); // get the buffer strategy if it's been created
		if (bs == null) // if the buffer strategy hasn't been setup yet
		{
			createBufferStrategy(3); // create triple buffer strategy
			return; // exit the function, it will pass this the next time through
		}
		
		int xOffset = player.x - (screen.width / 2); // offset of level tiles on the x-axis so that the player is centered
		int yOffset = player.y - (screen.height / 2); // offset of level tiles on the y-axis so that the player is centered
		
		level.renderTiles(screen,  xOffset,  yOffset); // render the tiles on screen taking the offsets into consideration
		level.renderEntities(screen); // render the entities on top of the tiles
		
		for (int y = 0; y < screen.height; y++) // for each pixel in the height
		{
			for (int x = 0; x < screen.width; x++) // for each pixel in the width
			{
				int colorCode = screen.pixels[x+(y*screen.width)];  // get the colorcode reference in our system for the current pixel
				if (colorCode < 255) pixels[x + (y * WIDTH)] = colors[colorCode]; // get the 0xRGB value from the array and set the pixel to it
			}
		}
		
		Graphics g = bs.getDrawGraphics(); // get the canvas to draw on
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null); // draw the now updated pixels array in the game area
		g.dispose();
		bs.show(); // display the current image in the buffer
	}
	
	// function to print out debug messages to the console when debug is on
	public void debug(DebugLevel level, String msg)
	{
		switch(level) // select the level of severity
		{
		default:
		case INFO:
			if (debug)
			{
				System.out.println("[" + NAME + "]" + msg);
			}
			break;
		case WARNING:
			if (debug)
			{
				System.out.println("[" + NAME + "][WARNING]" + msg);
			}
			break;
		case SEVERE:
			if (debug)
			{
				System.out.println("[" + NAME + "][SEVERE]" + msg);				
			}
			this.stop();
			break;
		}
	}
	
	// debug enumeration for levels of severity
	public static enum DebugLevel
	{
		INFO, WARNING, SEVERE;
	}
}
