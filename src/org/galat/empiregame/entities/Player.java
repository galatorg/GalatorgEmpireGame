package org.galat.empiregame.entities;

import org.galat.empiregame.Game;
import org.galat.empiregame.InputHandler;
import org.galat.empiregame.gfx.Colors;
import org.galat.empiregame.gfx.Font;
import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.Level;
import org.galat.empiregame.net.packet.Packet02Move;

/*****************************************************************************\
 *                                                                           *
 * Player, extends Mob, extends Entity                                       *
 *                                                                           *
 * Class for maintaining Player-type entities.                               *
 *                                                                           *
\*****************************************************************************/

public class Player extends Mob
{

	private InputHandler input; // reference to the input handler
	private int color = Colors.get(-1,  111,  145,  543); // colors to use for the player sprite, 3 colors + transparent
	private int scale = 1; // scale-size of the player
	protected boolean isSwimming = false; // to keep track of if the player is swimming for rendering
	private int tickCount = 0; // keeps track of the number of ticks that have passed
	private String username; // stores the username
	
	// player constructor
	public Player(Level level, int x, int y, InputHandler input, String username, SpriteSheet sheet)
	{
		super(level, "Player", x, y, 1, sheet); // pass in "Player" as the mob name/type
		this.input = input;
		this.username = username;
	}

	// a game tick has passed, update what's needed to be updated
	public void tick()
	{
		int xa = 0;
		int ya = 0;

		if (input!=null) // if an input is connected, other players will have null input references
		{			
			if (input.up.isPressed())
			{
				ya--; // y decreases as you move up
			}
			if (input.down.isPressed())
			{
				ya++; // y increases as you move down
			}
			if (input.left.isPressed())
			{
				xa--; // x decreases as you move left
			}
			if (input.right.isPressed())
			{
				xa++; // x increases as you move right
			}
		}
		
		if (xa != 0 || ya != 0) // if at least one of the movement keys was pressed
		{
			move(xa, ya); // call the function to move the player
			isMoving = true;
			
			Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir); // construct a move packet
			packet.writeData(Game.game.socketClient); // send the packet to the server
		}
		else // player did not move
		{
			isMoving = false; // set the moving flag to false
		}
		
		if (level.getTile(this.x>>level.tilesSheet.bitsNeeded, this.y>>level.tilesSheet.bitsNeeded).getId() == 3) // set the swimming flag if on a water tile
		{
			isSwimming = true;
		}
		if (isSwimming && level.getTile(this.x>>level.tilesSheet.bitsNeeded,  this.y>>level.tilesSheet.bitsNeeded).getId() != 3) // unset the swimming flag if not on a water tile
		{
			isSwimming = false;
		}
		tickCount++;
	}

	// function for when the player needs to be rendered
	public void render(Screen screen)
	{
		int xTile = 0; // x tile coordinate of the base character sprite, default up
		int yTile = 29; // y tile coordinate of the base character sprite
		int walkingSpeed = entitySheet.bitsNeeded - 1; // amount the player should move if walking
		int flipIt = (numSteps >> walkingSpeed) & 1; // to alter the walking animation, default up/down
		
		
		if (movingDir == 1) // if moving down 
		{
			xTile += 1; // adjust the x tile coordinate for the player's down sprite
		}
		else if (movingDir > 1) // if left or right
		{
			int walkFrame = ((numSteps >> walkingSpeed) % 4); // walking animation, 4 frames
			if (walkFrame == 3)
			{
				xTile = 3; // x tile coordinate of step to head back to, the second one
			}
			else
			{
				xTile += 2 + walkFrame; // cycle through the steps
			}
			flipIt = (movingDir - 1) % 2; // 0 for left, 1 for right
		}
		
		int modifier = entitySheet.tileSize * scale; // tile size multiplied by the scale
		int xOffset = x - modifier/2 + entitySheet.tileSize/2; // player offset from the left of the screen
		int yOffset = y - modifier/2; // player offset from the top of the screen
		
		if (!isSwimming) // if not swimming
		{
			screen.render(xOffset, yOffset, xTile + yTile * level.tilesSheet.tileSize, color, flipIt, scale, entitySheet); // render the player
		}
		else // the player is swimming
		{
			int waterColor = 0;
			
			// for animating, swaps colors and move slightly
			if (tickCount%60 < 15)
			{
				waterColor = Colors.get(-1,  -1, 225, -1);
			}
			else if (15 <= tickCount%60 && tickCount%60 < 30)
			{
				waterColor = Colors.get(-1,  225, 115, -1);
				yOffset -= 1;
			}
			else if (30 <= tickCount%60 && tickCount%60 < 45)
			{
				waterColor = Colors.get(-1,  115, -1, 225);
				yOffset -= 2;
			}
			else
			{
				waterColor = Colors.get(-1,  225, 115, -1);
				yOffset -= 1;
			}
			screen.render(xOffset, yOffset, xTile + yTile * level.tilesSheet.tileSize, color, flipIt, scale, entitySheet); // render the player
			screen.render(xOffset,  yOffset, 28 * level.tilesSheet.tileSize, waterColor, 0x00, 1, entitySheet);	 // render the water rings
		}
		
		if (username != null) // if there is a username set
		{
			Font.defaultFont.render(username, screen, xOffset - ((username.length()-1)/2 * Font.defaultFont.getFontSize()), yOffset-20, Colors.get(-1,  -1, -1, 555), 1); // print the player's name near them
		}
	}

	// function to check if a player will be stopped by another tile if they move xa, ya
	// TODO: improve this
	public boolean hasCollided(int xa, int ya)
	{
		// collision box - adjusts the boundaries of the player for collision detection
		int xMin = 10;
		int xMax = 20;
		int yMin = -5;
		int yMax = 13;

	    if (xa != 0) // if there is movement in the x direction
	    {
	            for(int x = xMin; x <= xMax; x++) // check along the x-axis (only need to check the endpoints?)
	            {
	                    if (isSolidTile(xa, ya, x, yMin+1) || isSolidTile(xa, ya, x, yMax-1)) // check at this x for a solid tile on the top and bottom
	                    {
	                            return true;
	                    }
	            }
	    }
	                   
	    if (ya != 0) // if there is movement in the y direction
	    {
	            for(int y = yMin; y <= yMax; y++) // check along the y-axis (only need to check the endpoints?)
	            {
	                    if (isSolidTile(xa, ya, xMin+1, y) || isSolidTile(xa, ya, xMax-1, y)) // check at this y for a solid tile on the left and right
	                    {
	                            return true;
	                    }
	            }
	    }

		return false; // no collision with a solid tile
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setSpriteSheet(SpriteSheet sheet)
	{
		if (sheet==null) sheet = SpriteSheet.defaultPlayer;
		entitySheet = sheet;
	}

}
