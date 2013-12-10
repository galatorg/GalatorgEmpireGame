package org.galat.empiregame.level.tilestates;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.galat.empiregame.level.Level;
import org.galat.empiregame.level.tileset.Tileset;

/*****************************************************************************\
 *                                                                            *
 * Tilemap class                                                              *
 *                                                                            *
 * The Tilemap class is used to store the set of tiles representing each tile *
 * in the level and what state it is in.                                      *
 *                                                                            *
\*****************************************************************************/

public class Tilemap
{
	public final TileState[][] tiles; // tiles[width][height] array representing the tiles in the level
	public final int width, height; // width and height of the level in # of tiles
	private String imagePath; // path to the bitmap(png) that stores the tile info for the level
	private BufferedImage image; // variable that stores the bitmap info from the file
	private Tileset levelTileset; // variable to reference the tile set used for this mapping
	
	public Tilemap(String imagePath, Tileset levelTileset)
	{
		this.levelTileset = levelTileset;
		
		if (imagePath != null) // if there was something passed in 
		{
			this.imagePath = imagePath; // store the path
			try // try, in case the file cannot be read from
			{
				this.image = ImageIO.read(Level.class.getResource(this.imagePath)); // read the file into the image variable
			}
			catch (IOException e) // failed to read from the file at imagePath
			{
				e.printStackTrace();				
			}
			this.width = image.getWidth(); // get the width of the image and save the width in a variable
			this.height = image.getHeight(); // get the height of the image and save the height in a variable
			tiles = new TileState[this.width][this.height]; // create the array of the proper size to store the tile Ids of each tile in the level
			this.loadTiles(); // call the function to load the tileId's into the tiles array from the image
		}
		else // a null value was passed into the constructor
		{
			this.width = 64; // default width of 64
			this.height = 64; // default height of 64
			tiles = new TileState[this.width][this.height];
			//this.generateLevel(); // call generateLevel to fill in default IDs in the tiles array
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
				for (int i = 0; i < levelTileset.getTilesetSize(); i++) // for each tile in the array of Id's
				{
					if (levelTileset.tiles[i] != null && levelTileset.tiles[i].getLevelColor() == tileColors[x + y * width]) // if there is a tile defined and the color matches that tile
					{
						this.tiles[x][y] = new TileState(levelTileset.tiles[i].getId()); // create a new Tilestate here for that tile type
						break; // break the loop checking each tile type for this pixel in the level, goto next pixel
					}
				}
			} // check the next column
		} // check the next row
	}
	
	// temporarily empty until it's needed.  used to update the state of the blocks
	public void tick()
	{ }
	
	
/* Not needed right now, but keep it in mind	
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
	public void alterTile(int x, int y, TileState newTile)
	{
		this.tiles[x + y * width] = newTile; // set the tileId of the new tile in the tiles array
		image.setRGB(x,  y,  newTile.getLevelColor()); // update the image(in memory) representing the level tiles
		saveLevelToFile(); // update the file, temporary
	}
	
	// TODO: upgrade to be better....create another that also does this with parameters.
	// fills the tiles array with default IDs
	public void generateLevel()
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
					if (x * y % 10 < 7 )
					{
						tiles[x + y * width] = 1;
					}
					else
					{
						tiles[x + y * width] = 2;
					}
			}
		}
	}
*/
}