package org.galat.empiregame.level.tileset;

import org.galat.empiregame.level.tiles.Tile;

/*****************************************************************************\
 *                                                                            *
 * Tileset class                                                              *
 *                                                                            *
 * This class contains the array of possible tile types in a level.  Defines  *
 * the appearance of the tiles in the level                                   *
 *                                                                            *
\*****************************************************************************/

public abstract class Tileset
{
	public final Tile[] tiles; // a Tile[] to store all the types of tiles
	private int tilesetSize; // number of tiles this set can hold
	
	// constructor
	public Tileset(int tilesetSize)
	{
		this.tiles = new Tile[tilesetSize]; // create new Tile[] to hold each tile type 
		this.tilesetSize = tilesetSize; // store the size
	}
	
	// addTile - adds a tile to the current Tileset
	public void addTile(Tile newTile)
	{
		if (tiles[newTile.getId()] != null) throw new RuntimeException("Duplicate tile id on " + newTile.getId()); // if the tile id already exists in the tiles Tile[]
		tiles[newTile.getId()] = newTile; // put this tile in the tiles Tile[]
	}
	
	public int getTilesetSize()
	{
		return this.tilesetSize;
	}
	
	// call tick on each tile type.  currently used to update animated tiles
	public void tick()
	{
		for (int i = 0; i < tilesetSize; i++)
		{
			if (tiles[i] != null)
			{
				tiles[i].tick();
			}
		}
	}
}
