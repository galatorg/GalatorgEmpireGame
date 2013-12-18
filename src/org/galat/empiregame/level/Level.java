package org.galat.empiregame.level;

import java.util.ArrayList;
import java.util.List;

import org.galat.empiregame.entities.Entity;
import org.galat.empiregame.entities.PlayerMP;
import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.tileset.Tileset;
import org.galat.empiregame.level.tileset.DefaultTileset;
import org.galat.empiregame.level.tilestates.Tilemap;

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
	public List<Entity> entities = new ArrayList<Entity>(); // list of players,mobs, etc. in the level.
	public final SpriteSheet tilesSheet; // the sheet used to draw the ground tiles
	public final Tileset levelTileset; // holds the set of possible tiles, holds common info such as appearance
	public final Tilemap levelTilemap; // holds the set of tiles in the level, holds state info pertaining to each tile in the level
	
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
	
	public Level(String imagePath, SpriteSheet sheet, Tileset levelTiles)
	{
		if (levelTiles != null) // if there is a tileset to be used
		{
			levelTileset = levelTiles;
		}
		else // use the default tileset
		{
			levelTileset = new DefaultTileset(); // default Tileset to use
		}

		if (sheet != null) // if there is a spritesheet that was passed in
		{
			tilesSheet = sheet;
		}
		else // use the default spritesheet
		{
			tilesSheet = SpriteSheet.defaultTiles; // default sheet
		}		

		levelTilemap = new Tilemap(imagePath, levelTileset); // setup the level tile map based on the image and using the set of tiles in levelTileset
	}

	// returns the list of entities in the level
	public synchronized List<Entity> getEntities()
	{
		return this.entities;
	}
	
	// update the entities and tiles in the level
	public void tick()
	{	
		for (Entity e : getEntities()) // for each entity in the level
		{
			e.tick(); // call the tick function of the entity to update it
		}
		
		levelTileset.tick(); // cycles the appearance on the tiles(animated tiles)
		levelTilemap.tick(); // update the tiles in the level - TODO: limit to a certain range?
	}
	
	// function to render the current tiles on the screen based on the xOffset and yOffset
	public void renderTiles(Screen screen, int xOffset, int yOffset)
	{
		// these prevent (non)rendering outside the visible area
		if(xOffset < 0) xOffset = 0; // xOffset cannot be negative, if so, set to 0
		if(xOffset > ((levelTilemap.width<<tilesSheet.bitsNeeded) - screen.width)) xOffset = ((levelTilemap.width<<tilesSheet.bitsNeeded)-screen.width); // xOffset cannot go past the right edge of the level
		if(yOffset < 0) yOffset = 0; // yOffset cannot be negative, if so, set to 0
		if(yOffset > ((levelTilemap.height<<tilesSheet.bitsNeeded) - screen.height)) yOffset = ((levelTilemap.height<<tilesSheet.bitsNeeded)-screen.height); // yOffset cannot go past the bottom edge of the level
		
		screen.setOffset(xOffset, yOffset); // set the offsets after they have been checked
		
		for (int y = (yOffset>>tilesSheet.bitsNeeded); y < ((yOffset + screen.height)>>tilesSheet.bitsNeeded)+1; y++) // render each row that's on the screen
		{
			for (int x = (xOffset>>tilesSheet.bitsNeeded); x < ((xOffset + screen.width)>>tilesSheet.bitsNeeded)+1; x++) // render each column that's on the screen
			{
				int xOff=0, yOff=0;
				if ((x < levelTilemap.width) && (y < levelTilemap.height))
				{
					xOff = levelTilemap.tiles[x][y].getXTileOffset();
					yOff = levelTilemap.tiles[x][y].getYTileOffset();
				}
				levelTileset.tiles[getTileId(x,y)].render(screen, x<<tilesSheet.bitsNeeded, y<<tilesSheet.bitsNeeded, xOff, yOff); // get the current tile and tell it to render
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
	
	// function to get the id of the tile located at x,y(in tile coordinates)
	public int getTileId(int x, int y)
	{
		if (0 > x || x >= levelTilemap.width || 0 > y || y >= levelTilemap.height) return 0; // if the x or y value is out of bounds return the first tile in the set
		return levelTilemap.tiles[x][y].getId();
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
}