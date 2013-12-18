package org.galat.empiregame.level.tilestates;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.galat.empiregame.level.Level;
import org.galat.empiregame.level.tiles.BasicMultispriteTile;
import org.galat.empiregame.level.tiles.Tile;
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
	
	// loads the tileId's into the tiles array based on the data stored in image and link the TileStates
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
		
		// setup references on the top and bottom rows		
		if (height > 1) //if there is only 1 row then above/below will still be null for all
		{
			for (int x = 0; x < width; x++) // for each tile along x
			{
				this.tiles[x][0].below = this.tiles[x][1]; // set the top row's below reference 
				this.tiles[x][height-1].above = this.tiles[x][height-2]; // set the bottom row's above reference
			}
		}
		
		// setup references on the first(left) and last(right) columns
		if (width > 1) // if there is only 1 column then left/right will still be null for all
		{
			for (int y = 0; y < height; y++) // for each tile along y
			{
				this.tiles[0][y].right = this.tiles[1][y]; // set the first(left) row's right reference
				this.tiles[width-1][y].left = this.tiles[width-2][y]; // set the last(right) row's left reference
			}
		}
		
		// setup the references on the inner tiles
		if (height > 2) // if there are inner tiles along the rows, at least 3 rows
		{
			for (int y = 1; y < height-1; y++) // for each row in the level, skip top and bottom rows
			{
				if (width > 2) // if there are inner tiles along the columns, at least 3 columns
				{
					for (int x = 1; x < width-1; x++) // for each column in the level, skip first and last columns
					{
						this.tiles[x][y].above = this.tiles[x][y-1];
						this.tiles[x][y].below = this.tiles[x][y+1];
						this.tiles[x][y].left = this.tiles[x-1][y];
						this.tiles[x][y].right = this.tiles[x+1][y];
					} // next column
				}
				else // there are inner tiles along the rows, but not columns, width = 1 or 2, don't need to set left/right
				{
					this.tiles[0][y].above = this.tiles[0][y-1];
					this.tiles[0][y].below = this.tiles[0][y+1];
					if (width==2)
					{
						this.tiles[1][y].above = this.tiles[1][y-1];
						this.tiles[1][y].below = this.tiles[1][y+1];
					}
				}
			} // next row
		}
		else // don't need to set the above/below because there are only 1-2 rows, height = 1 or 2
		{
			if (width > 2) // if there are inner tiles along the columns, at least 3 columns, width = 1 or 2
			{
				for (int x = 1; x < width-1; x++) // for each column in the level, skip first and last columns
				{
					this.tiles[x][0].left = this.tiles[x-1][0];
					this.tiles[x][0].right = this.tiles[x+1][0];
					if (height==2)
					{
						this.tiles[x][1].left = this.tiles[x-1][1];
						this.tiles[x][1].right = this.tiles[x+1][1];
					}
				} // next column
			}
		}
		
		
		// update the appearance on all tiles that have it available
		for (int y = 0; y < height; y++) // for each row in the level
		{
			for (int x = 0; x < width; x++) // for each column in the level
			{
				int i = this.tiles[x][y].getId(); // get the tile Id to reference it in the tileset
				if (levelTileset.tiles[i] != null && levelTileset.tiles[i].appearanceUpdates) // if there is a tile defined and that tile type updates
				{
					Tile curTile = levelTileset.tiles[this.tiles[x][y].getId()];
					int domId = curTile.domTileId;

					if (domId == -2) // if it's a multisprite tile
					{
						BasicMultispriteTile thisTile = (BasicMultispriteTile) curTile;
						this.tiles[x][y].setOffsets(thisTile.randomizeTile(), 0); //thisTile.randomizeTile();  // generate a random xOffset to use one of the sprites
					}
					else // it's a transition tile
					{
						updateTransitions(this.tiles[x][y], domId); // run appearance update checks
					}
				}
			} // check the next column
		} // check the next row		
	}
	
	// temporarily empty until it's needed.  used to update the state of the blocks
	public void tick()
	{ }
	
	// function to change the tile to a new tile in the level
	// x - x tile coordinate
	// y - y tile coordinate
	// newTile - TileState class instance to be put at that location
	public void alterTile(int x, int y, Tile newTile)
	{
		this.tiles[x][y].setTile(newTile); // set the tileId of the new tile in the tiles array
		image.setRGB(x,  y,  newTile.getLevelColor()); // update the image(in memory) representing the level tiles
//		saveLevelToFile(); // update the file, temporary
	}
	
	// function to update the offsets that adjust transition tiles
	public void updateTransitions(TileState thisTile, int domId)
	{		
		if (thisTile.above.getId() == domId) // if the tile above is a dominant tile
		{
			if (thisTile.below.getId() == domId) // if the tile below is a dominant tile also
			{
				if (thisTile.left.getId() == domId) // if the left tile is also dominant
				{
					if (thisTile.right.getId() == domId) // if dominant tiles are on all sides
					{
						thisTile.setOffsets(0, 0); // Tile 1
					}
					else // if all sides but the right side are dominant
					{
						thisTile.setOffsets(1, 0); // Tile 2
					}
				}
				else if (thisTile.right.getId() == domId) // if all sides but the left side is dominant
				{
					thisTile.setOffsets(2, 0); // Tile 3
				}
				else // only the top side and the bottom side are dominant
				{
					thisTile.setOffsets(3, 0); // Tile 4
				}
			}
			else if (thisTile.left.getId() == domId) // if the tile to the left is a dominant tile and the one below is not
			{
				if (thisTile.right.getId() == domId) // if all sides but the bottom are dominant
				{
					thisTile.setOffsets(4, 0); // Tile 5
				}
				else if (thisTile.below.right.getId() == domId) // if the upper side, left side, and the lower right tiles are dominant
				{
					thisTile.setOffsets(5, 0); // Tile 6
				}
				else // if the upper and left sides are the only thing dominant
				{
					thisTile.setOffsets(6, 0); // Tile 7
				}
			}
			else if (thisTile.right.getId() == domId) // if the tile to the right is a dominant tile and the left/below tiles are not
			{
				if (thisTile.below.left.getId() == domId) // if the top and right sides are the only dominant sides and the bottom left is also
				{
					thisTile.setOffsets(7, 0); // Tile 8
				}
				else // if the top and right sides are the only dominant sides
				{
					thisTile.setOffsets(0, 1); // Tile 9
				}
			}
			else if (thisTile.below.right.getId() == domId) // if the bottom right tile is dominant and the tile above is the only side tile dominant
			{
				if (thisTile.below.left.getId() == domId) // if the bottom two corners and the tile above are dominant tiles 
				{
					thisTile.setOffsets(1, 1); // Tile 10
				}
				else // if the bottom right and the above side are dominant, but the bottom right is not
				{
					thisTile.setOffsets(2, 1); // Tile 11
				}
			}
			else if (thisTile.below.left.getId() == domId) // if the bottom left and the above side are dominant, but the bottom right is not
			{
				thisTile.setOffsets(3, 1); // Tile 12
			}
			else // only the top side is dominant
			{
				thisTile.setOffsets(4, 1); // Tile 13
			}
		}
		else if (thisTile.below.getId() == domId) // if the tile below is a dominant tile and the top is not
		{
			if (thisTile.left.getId() == domId) // if the left tile is dominant also
			{
				if (thisTile.right.getId() == domId) // if the left, lower, and right sides are dominant
				{
					thisTile.setOffsets(5, 1); // Tile 14
				}
				else if (thisTile.above.right.getId() == domId) // if the upper right, left side, and bottom sides are dominant
				{
					thisTile.setOffsets(6, 1); // Tile 15
				}
				else // if the left and bottom sides are dominant
				{
					thisTile.setOffsets(7, 1); // Tile 16
				}
			}
			else if (thisTile.right.getId() == domId) // if the right tile is dominant also, but the left is not
			{
				if (thisTile.above.left.getId() == domId) // if the upper left, bottom side, and right sides are dominant
				{
					thisTile.setOffsets(0, 2); // Tile 17
				}
				else // if the bottom and right sides are the only sides dominant
				{
					thisTile.setOffsets(1, 2); // Tile 18
				}
			}
			else if (thisTile.above.right.getId() == domId) // if the upper right is dominant also, but the left and right sides are not
			{
				if (thisTile.above.left.getId() == domId) // if the upper left, upper right and bottom sides are dominant
				{
					thisTile.setOffsets(2, 2); // Tile 19
				}
				else
				{
					thisTile.setOffsets(3, 2); // Tile 20
				}
			}
			else if (thisTile.above.left.getId() == domId) // if the upper left is dominant also, the the left and right sides or upper right are not
			{
				thisTile.setOffsets(4, 2); // Tile 21
			}
			else // only the bottom side is dominant
			{
				thisTile.setOffsets(5, 2); // Tile 22
			}
		}
		else if (thisTile.left.getId() == domId) // if the tile to the left is a dominant tile and the top or bottom are not
		{
			if (thisTile.right.getId() == domId) // if the right side is dominant also
			{
				thisTile.setOffsets(6, 2); // Tile 23
			}
			else if (thisTile.below.right.getId() == domId) // if the lower right is also dominant, but the whole right side is not
			{
				if (thisTile.above.right.getId() == domId) // if the upper right is also dominant
				{
					thisTile.setOffsets(7, 2); // Tile 24
				}
				else // the left side and lower right is dominant
				{
					thisTile.setOffsets(0, 3); // Tile 25
				}
			}
			else if (thisTile.above.right.getId() == domId) // if the upper right is dominant, but nothing else on the right is
			{
				thisTile.setOffsets(1, 3); // Tile 26
			}
			else // the left side is dominant
			{
				thisTile.setOffsets(2, 3); // Tile 27
			}
		}
		else if (thisTile.right.getId() == domId) // if the tile to the right is a dominant tile and the other sides are not
		{
			if (thisTile.above.left.getId() == domId) // if the upper left tile is also dominant
			{
				if (thisTile.below.left.getId() == domId) // if the lower left tile is also dominant
				{
					thisTile.setOffsets(3, 3); // Tile 28
				}
				else // the right side and lower left tile is dominant
				{
					thisTile.setOffsets(4, 3); // Tile 29
				}
			}
			else if (thisTile.below.left.getId() == domId) // if the lower left tile is also dominant, but the upper left is not
			{
				thisTile.setOffsets(5, 3); // Tile 30
			}
			else // the right is the only side dominant
			{
				thisTile.setOffsets(6, 3); // Tile 31
			}
		}
		else if (thisTile.above.left.getId() == domId) // if the upper left tile is dominant and no sides are
		{
			if (thisTile.below.right.getId() == domId) // if the lower right tile is also dominant
			{
				if (thisTile.above.right.getId() == domId) // if the upper right tile is also dominant
				{
					if (thisTile.below.left.getId() == domId) // if all corners are dominant
					{
						thisTile.setOffsets(7, 3); // Tile 32
					}
					else // if all corners except the lower left are dominant
					{
						thisTile.setOffsets(0, 4); // Tile 33
					}
				}
				else if (thisTile.below.left.getId() == domId) // if the lower left tile is also dominant, but the upper right is not
				{
					thisTile.setOffsets(1, 4); // Tile 34
				}
				else // if the upper left and lower right tiles are dominant
				{
					thisTile.setOffsets(2, 4); // Tile 35
				}
			}
			else if (thisTile.above.right.getId() == domId) // if the upper right tile is also dominant, but the lower right is not
			{
				if (thisTile.below.left.getId() == domId)
				{
					thisTile.setOffsets(3, 4); // Tile 36
				}
				else
				{
					thisTile.setOffsets(4, 4); // Tile 37
				}
			}
			else if (thisTile.below.left.getId() == domId) // if the lower left tile is also dominant, but the lower right and upper right are not
			{
				thisTile.setOffsets(5, 4); // Tile 38
			}
			else // upper left is dominant, but the other corners are not
			{
				thisTile.setOffsets(6, 4); // Tile 39
			}
		}
		else if (thisTile.below.right.getId() == domId) // if the lower right is dominant and the sides or upper left are not
		{
			if (thisTile.above.right.getId() == domId) // if the top right is also dominant
			{
				if (thisTile.below.left.getId() == domId) // if the lower left is also dominant
				{
					thisTile.setOffsets(7, 4); // Tile 40
				}
				else // only the upper right and lower right are dominant
				{
					thisTile.setOffsets(0, 5); // Tile 41
				}
			}
			else if (thisTile.below.left.getId() == domId) // if the lower left is also dominant, but top right is not
			{
				thisTile.setOffsets(1, 5); // Tile 42
			}
			else
			{
				thisTile.setOffsets(2, 5); // Tile 43
			}
		}
		else if (thisTile.above.right.getId() == domId) // if the upper right tile is dominant and the upper left or lower right are not
		{
			if (thisTile.below.left.getId() == domId) // if the lower left is also dominant
			{
				thisTile.setOffsets(3, 5); // Tile 44
			}
			else // only the upper right is dominant
			{
				thisTile.setOffsets(4, 5); // Tile 45
			}
		}
		else if (thisTile.below.left.getId() == domId) // if the lower left is dominant and no other tiles nearby are
		{
			thisTile.setOffsets(5, 5); // Tile 46
		}
		else // no adjacent dominant tiles
		{
			thisTile.setOffsets(6, 5); // Tile 47
		}
	}
	
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