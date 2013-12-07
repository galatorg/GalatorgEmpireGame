package org.galat.empiregame.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.galat.empiregame.entities.Entity;
import org.galat.empiregame.entities.PlayerMP;
import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.tiles.Tile;

/*****************************************************************************\
 *                                                                            *
 * Level class                                                                *
 *                                                                            *
 * The Level class is used to store and generate data pertaining to the world *
 * the player is in                                                           *
 *                                                                            *
\*****************************************************************************/

public class Level
{
	private short[] tiles; // array that stores the tileID of each tile in the level
	public int width; // width of level in tiles
	public int height; // height of level in tiles
	public List<Entity> entities = new ArrayList<Entity>(); // list of players,mobs, etc. in the level.
	private String imagePath; // path to the bitmap(png) that stores the tile info for the level
	private BufferedImage image; // variable that stores the bitmap info from the file
	public final SpriteSheet tilesSheet; // the sheet used to draw the ground tiles
	
	/*****************************************************************************\
	*                                                                             *
	* Level constructor                                                           *                                                                            
	*                                                                             *
	* Takes in a string(imagePath) that is the path to the image containing the   *
	* level data.  If a null value is passed it loads some default values,        *
	* otherwise it stores the path in a private variable and the                  *
	* loadLevelFromFile() is called to load the level.                            *
	*                                                                             *
	\*****************************************************************************/
	
	public Level(String imagePath, SpriteSheet sheet)
	{
		if (imagePath != null) // if there was something passed in 
		{
			this.imagePath = imagePath; // store the path
			this.loadLevelFromFile(); // load the level using the path that was passed in
		}
		else // a null value was passed into the constructor
		{
			this.width = 64; // default width of 64
			this.height = 64; // default height of 64
			tiles = new short[width * height]; // generate array to hold the IDs of all the tiles
			this.generateLevel(); // call generateLevel to fill in default IDs in the tiles array
		}
		
		if (sheet != null)
		{
			tilesSheet = sheet;
		}
		else
		{
			tilesSheet = SpriteSheet.defaultTiles; // default sheet
		}
	}
	
	// load the level data from the file at imagePath
	private void loadLevelFromFile()
	{
		try // try, in case the file cannot be read from
		{
			this.image = ImageIO.read(Level.class.getResource(this.imagePath)); // read the file into the image variable
			this.width = image.getWidth(); // get the width of the image and save the width in a variable
			this.height = image.getHeight(); // get the height of the image and save the height in a variable
			tiles = new short[width * height]; // create the array of the proper size to store the tile Ids of each tile in the level
			this.loadTiles(); // call the function to load the tileId's into the tiles array from the image
			
		}
		catch (IOException e) // failed to read from the file at imagePath
		{
			e.printStackTrace();
		}
	}
	
	// loads the tileId's into the tiles array based on the data stored in image
	private void loadTiles()
	{
		int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width); // get the RGB of each pixel in the image and store it in an int[]
		for (int y = 0; y < height; y++) // for each row in the level
		{
			for (int x = 0; x < width; x++) // for each column in the level
			{
				tileCheck: for (Tile t : Tile.tiles) // for each tile in the array of Id's
				{
					if (t != null && t.getLevelColor() == tileColors[x + y * width]) // if there is a tile defined and the color matches that tile
					{
						this.tiles[x + y * width] = t.getId(); // store the tile id
						break tileCheck; // break the loop checking each tile type
					}
				}
			} // check the next column
		} // check the next row
	}	
	
	// write out the current image representing the level to the file at imagePath
	private void saveLevelToFile()
	{
		try
		{
			ImageIO.write(image,"png", new File(Level.class.getResource(this.imagePath).getFile()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// function to change the tile to a new tile in the level
	// x - x tile coordinate
	// y - y tile coordinate
	// newTile - Tile class instance to be put at that location
	public void alterTile(int x, int y, Tile newTile)
	{
		this.tiles[x + y * width] = newTile.getId(); // set the tileId of the new tile in the tiles array
		image.setRGB(x,  y,  newTile.getLevelColor()); // update the image(in memory) representing the level tiles
		saveLevelToFile(); // update the file, temporary
	}
	
	// fills the tiles array with default IDs
	public void generateLevel()
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
					if (x * y % 10 < 7 )
					{
						tiles[x + y * width] = Tile.GRASS.getId();
					}
					else
					{
						tiles[x + y * width] = Tile.STONE.getId();
					}
			}
		}
	}

	// returns the list of entities in the level
	public synchronized List<Entity> getEntities()
	{
		return this.entities;
	}
	
	// update the entities and tiles in the level
	// TODO: improve this eventually to update only the items that should be 
	public void tick()
	{
		for (Entity e : getEntities()) // for each entity in the level
		{
			e.tick(); // call the tick function of the entity to update it
		}
		
		for (Tile t : Tile.tiles) // for each tile in the level
		{
			if (t == null)
			{
				break; // skip this tile if it's null
			}
			t.tick(); // call the tick function of the tile to update it
		}
	}
	
	// function to render the current tiles on the screen based on the xOffset and yOffset
	public void renderTiles(Screen screen, int xOffset, int yOffset)
	{
		// these prevent (non)rendering outside the level
		if(xOffset < 0) xOffset = 0; // xOffset cannot be negative, if so, set to 0
		if(xOffset > ((width<<tilesSheet.bitsNeeded) - screen.width)) xOffset = ((width<<tilesSheet.bitsNeeded)-screen.width); // xOffset cannot go past the right edge of the level
		if(yOffset < 0) yOffset = 0; // yOffset cannot be negative, if so, set to 0
		if(yOffset > ((height<<tilesSheet.bitsNeeded) - screen.height)) yOffset = ((height<<tilesSheet.bitsNeeded)-screen.height); // yOffset cannot go past the bottom edge of the level
		
		screen.setOffset(xOffset, yOffset); // set the offsets after they have been checked
		
		for (int y = (yOffset>>tilesSheet.bitsNeeded); y < ((yOffset + screen.height)>>tilesSheet.bitsNeeded)+1; y++) // render each row that's on the screen
		{
			for (int x = (xOffset>>tilesSheet.bitsNeeded); x < ((xOffset + screen.width)>>tilesSheet.bitsNeeded)+1; x++) // render each column that's on the screen
			{
				getTile(x,y).render(screen, this, x<<tilesSheet.bitsNeeded, y<<tilesSheet.bitsNeeded); // get the current tile and tell it to render
			}
		}
 	}

	// function to render the entities in the level
	public void renderEntities(Screen screen)
	{
		for (Entity e : getEntities()) // for each entity in the list
		{
			e.render(screen); // call the render function for this entity
		}
	}
	
	// function to get the tile located at x,y(in tile coordinates)
	public Tile getTile(int x, int y)
	{
		if (0 > x || x >= width || 0 > y || y >= height) return Tile.VOID; // if the x or y value is out of bounds return a VOID tile
		return Tile.tiles[tiles[x + y * width]];
	}

	// add a new entity to the list of entities in the level
	public void addEntity(Entity entity)
	{
		this.getEntities().add(entity);
	}

	// remove a PlayerMP instance from the list based on username
	// TODO: use some other identifier that may not have a duplicate value
	public void removePlayerMP(String username)
	{
		int index = 0; // start at the beginning of the list
		for (Entity e : getEntities()) // for each entity in the list
		{
			if (e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username)) // if this entity is a PlayerMP class and the username is the same
			{
				break;
			}
			index++; // next entity
		}
		this.getEntities().remove(index); //remove the PlayerMP it found, TODO: if not found?
	}
	
	// return the index in the entity list where the PlayerMP's name = username
	// TODO: use some other identified that may not have a duplicate value
	private int getPlayerMPIndex(String username)
	{
		int index = 0; // start at the beginning of the list
		for (Entity e : getEntities()) // for each entity in the list
		{
			if (e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username)) // if this entity is a PlayerMP class and the username is the same
			{
				break;
			}
			index++; // next entity
		}
		return index; // return the index value where the PlayerMP was found
	}
	
	// move the player based on username, for moving other players in multiplayer
	public void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir)
	{
		int index = getPlayerMPIndex(username); // get the index of the player
		PlayerMP player = (PlayerMP) this.getEntities().get(index); // reference the player
		player.x = x;
		player.y = y;
		player.setMoving(isMoving);
		player.setNumSteps(numSteps);
		player.setMovingDir(movingDir);
		this.getEntities().get(index).x = x; // is this needed?
		this.getEntities().get(index).y = y; // is this needed?
	}

	// print out a list of players in the level
	// TODO: can probably just be removed
	public void listPlayers()
	{
		for (Entity e : getEntities())
		{
			if (e instanceof PlayerMP)
			{
				System.out.println (((PlayerMP)e).getUsername());
			}
		}
	}
}