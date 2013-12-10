package org.galat.empiregame.level.tilestates;

/*****************************************************************************\
 *                                                                            *
 * TileState class                                                            *
 *                                                                            *
 * An element of the Tilemap with references to the adjacent tiles.  Used to  *
 * modify specific tiles in the level.                                        *
 *                                                                            *
\*****************************************************************************/

public class TileState
{
	private int tileId;  // id of the tile type in this tile
	public TileState above, below, left, right; // to reference adjacent tiles

	public TileState(int tileId)
	{
		this.tileId = tileId;
		this.above = null; // initialize to null, will be set after all tiles in the level are loaded if needed
		this.below = null; // initialize to null, will be set after all tiles in the level are loaded if needed
		this.left = null; // initialize to null, will be set after all tiles in the level are loaded if needed
		this.right = null; // initialize to null, will be set after all tiles in the level are loaded if needed
	}

	public int getId()
	{
		return tileId;
	}
	
	// to update this specific block on a clock tick
	public void tick()
	{
		
	}
}
